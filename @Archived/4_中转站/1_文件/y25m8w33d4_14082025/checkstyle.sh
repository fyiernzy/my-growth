# For debugging purpose
#set -x
#export PS4='[$(date +%H:%M:%S)] ${0##*/}:${LINENO}: 'LINENO
set -Eeuo pipefail

# --- Helper methods ---
curl_dl() {
  curl --retry 3 --retry-all-errors --connect-timeout 10 --max-time 90 -fSL "$1" -o "$2"
}

verify_sha() {
  local file="$1"
  local url="$2"
  local sha_tool=""
  local sha_ext=""

  if command -v sha256sum >/dev/null 2>&1; then
    sha_tool="sha256sum"
    sha_ext="sha256"
  elif command -v sha1sum >/dev/null 2>&1; then
    sha_tool="sha1sum"
    sha_ext="sha1"
  fi

  if [ -n "$sha_tool" ] && curl -fsSL "${url}.${sha_ext}" -o "${file}.${sha_ext}"; then
    local tmp
    tmp="$(mktemp 2>/dev/null || echo "/tmp/verify.$$")"
    awk '{print $1"  '"$file"'"}' "${file}.${sha_ext}" > "$tmp"
    $sha_tool -c "$tmp" >/dev/null 2>&1 || \
      echo "[WARN] Checksum verification failed for ${file}; continuing without enforcement"
    rm -f "$tmp"
    return 0
  else
    echo "[WARN] No usable checksum or tool for ${file}; continuing without verification"
    return 0
  fi
}

is_valid_commit() {
  git cat-file -e "$1^{commit}" 2>/dev/null;
}

ensure_commit_available() {
  local sha="$1"
  [ -n "$sha" ] || return 1
  is_valid_commit "$sha" && return 0
  git fetch --no-tags --depth=200 origin "$sha" >/dev/null 2>&1 && is_valid_commit "$sha"
}

rev_origin_branch() {
  local branch="$1"
  [ -n "$branch" ] || return 1
  git fetch --no-tags --depth=200 origin \
    "refs/heads/${branch}:refs/remotes/origin/${branch}" >/dev/null 2>&1 || true
  git rev-parse "refs/remotes/origin/${branch}" 2>/dev/null || return 1
}

pick_diff_base() {
  local empty_tree="4b825dc642cb6eb9a060e54bf8d69288fbee4904"
  local zero_commit="0000000000000000000000000000000000000000"

  # 1) MR pipelines first
  if [[ "${CI_PIPELINE_SOURCE:-}" == "merge_request_event" || -n "${CI_MERGE_REQUEST_IID:-}" ]]; then
    # Prefer GitLab-provided diff base
    if [[ -n "${CI_MERGE_REQUEST_DIFF_BASE_SHA:-}" ]]; then
      if ensure_commit_available "${CI_MERGE_REQUEST_DIFF_BASE_SHA}"; then
        echo "${CI_MERGE_REQUEST_DIFF_BASE_SHA}"
        return 0
      fi
      # Best effort: fetch target branch and re-try (may include the base)
      if [[ -n "${CI_MERGE_REQUEST_TARGET_BRANCH_NAME:-}" ]]; then
        rev_origin_branch "${CI_MERGE_REQUEST_TARGET_BRANCH_NAME}" >/dev/null || true
      fi
      if is_valid_commit "${CI_MERGE_REQUEST_DIFF_BASE_SHA}"; then
        echo "${CI_MERGE_REQUEST_DIFF_BASE_SHA}"
        return 0
      fi
      echo "[WARN] MR diff-base not retrievable; computing merge-base." >&2
    fi

    # Fallback in MR context: compute merge-base from target name + source (commit)
    local target_sha="" source_sha=""

    if [[ -n "${CI_MERGE_REQUEST_TARGET_BRANCH_SHA:-}" ]]; then
      target_sha="${CI_MERGE_REQUEST_TARGET_BRANCH_SHA}"
      ensure_commit_available "$target_sha" || echo "[WARN] Target SHA fetch failed: $target_sha" >&2
    elif [[ -n "${CI_MERGE_REQUEST_TARGET_BRANCH_NAME:-}" ]]; then
      target_sha="$(rev_origin_branch "${CI_MERGE_REQUEST_TARGET_BRANCH_NAME}" 2>/dev/null || true)"
      if [[ -z "$target_sha" ]]; then
        echo "[WARN] Could not resolve target branch ${CI_MERGE_REQUEST_TARGET_BRANCH_NAME} from origin." >&2
      fi
    fi

    if [[ -n "${CI_MERGE_REQUEST_SOURCE_BRANCH_SHA:-}" ]]; then
      source_sha="${CI_MERGE_REQUEST_SOURCE_BRANCH_SHA}"
      ensure_commit_available "$source_sha" || echo "[WARN] Source SHA fetch failed: $source_sha" >&2
    elif [[ -n "${CI_MERGE_REQUEST_SOURCE_BRANCH_NAME:-}" ]]; then
      source_sha="$(rev_origin_branch "${CI_MERGE_REQUEST_SOURCE_BRANCH_NAME}" 2>/dev/null || true)"
      if [[ -z "$source_sha" ]]; then
        source_sha="${CI_COMMIT_SHA:-}"
      fi
    fi

    if [[ -n "$target_sha" && -n "$source_sha" ]] && is_valid_commit "$target_sha" && is_valid_commit "$source_sha"; then
      local merge_base
      merge_base="$(git merge-base "$target_sha" "$source_sha" 2>/dev/null || true)"
      if [[ -n "$merge_base" ]]; then
        if [[ "$merge_base" != "$target_sha" ]]; then
          echo "[WARN] Target branch has commits not in source; diffing against common ancestor $merge_base." >&2
        fi
        echo "$merge_base"
        return 0
      else
        echo "[WARN] Could not compute merge-base; falling back." >&2
      fi
    else
      echo "[WARN] MR context detected but could not resolve both SHAs; falling back." >&2
    fi
  fi

  if [[ -n "${CI_COMMIT_BEFORE_SHA:-}" && \
  "${CI_COMMIT_BEFORE_SHA}" != "$empty_tree" && \
  "${CI_COMMIT_BEFORE_SHA}" != "$zero_commit" ]]; then
    if is_valid_commit "$CI_COMMIT_BEFORE_SHA" || ensure_commit_available "$CI_COMMIT_BEFORE_SHA"; then
      echo "$CI_COMMIT_BEFORE_SHA"
      return 0
    fi
  fi

  # --- Local fallback: previous commit if it exists ---
  if git rev-parse --verify HEAD~1 >/dev/null 2>&1; then
    git rev-parse HEAD~1
    return 0
  fi

  echo "[INFO] Could not determine a base commit. Comparing against the empty tree (all files will be treated as new)." >&2
  echo "$empty_tree"
}

# ------------- 1. locate newest Violations CLI -----
MAVEN_META_URL="https://repo1.maven.org/maven2/se/bjurr/violations/violations-command-line/maven-metadata.xml"

meta="$(curl -fsSL "$MAVEN_META_URL")" || { echo "[ERROR] Fetching maven-metadata.xml failed"; exit 1; }

# Try <release> first
VCL_VER="$(printf '%s' "$meta" | sed -n 's:.*<release>\([^<][^<]*\)</release>.*:\1:p' | head -n1)"

# Fallback to <latest>
if [ -z "$VCL_VER" ]; then
  VCL_VER="$(printf '%s' "$meta" | sed -n 's:.*<latest>\([^<][^<]*\)</latest>.*:\1:p' | head -n1)"
fi

# Fallback to the last <version> entry
if [ -z "$VCL_VER" ]; then
  VCL_VER="$(printf '%s\n' "$meta" | awk -F'[<>]' '/<version>/{v=$3} END{if(v) print v}')"
fi

if [ -z "$VCL_VER" ]; then
  VCL_VER="3.1.1"
fi

VCL_JAR="violations-command-line-${VCL_VER}.jar"
VCL_URL="https://repo1.maven.org/maven2/se/bjurr/violations/violations-command-line/${VCL_VER}/${VCL_JAR}"  # directory exists :contentReference[oaicite:1]{index=1}

vcl_ok=false
if [[ ! -f "$VCL_JAR" ]]; then
  echo "[INFO] Downloading $VCL_JAR (Version: ${VCL_VER})"
  if curl_dl "$VCL_URL" "$VCL_JAR" && verify_sha "$VCL_JAR" "$VCL_URL"; then
    vcl_ok=true
  else
    echo "[ERROR] Failed to download or verify $VCL_JAR"
  fi
else
  vcl_ok=true
fi

# ------------------ main --------------------
cecho() {
  printf "%b%s%b\n" "$1" "$2" "\033[0m"
}

print_commit_info() {
  branch="$1"
  sha="$2"
  subject="$(git show -s --format=%s "$sha")"
  author="$(git show -s --format='%an <%ae>' "$sha")"
  date="$(git show -s --date=iso-strict --format=%ad "$sha")"

  echo "Branch  : $branch"
  echo "Commit  : $sha"
  echo "Subject : $subject"
  echo "Author  : $author"
  echo "Date    : $date"
}

YELLOW_BOLD="\033[1;33m"

BASE_COMMIT="$(pick_diff_base)"
HEAD_SHA=${CI_COMMIT_SHA-}
if [ -z "$HEAD_SHA" ]; then
  HEAD_SHA=$(git rev-parse HEAD 2>/dev/null || echo "")
fi
if [ -z "$HEAD_SHA" ]; then
  echo "[WARN] Cannot determine HEAD commit; creating empty reports and exiting 0"
  mkdir -p build/reports/checkstyle
  echo '[]' > gl-code-quality-report.json
  printf '%s' '<?xml version="1.0" encoding="UTF-8"?><testsuites name="checkstyle" tests="0" failures="0"/>' \
    > build/reports/checkstyle/junit-report.xml
  exit 0
fi

BASE_BRANCH="${CI_MERGE_REQUEST_TARGET_BRANCH_NAME:-${CI_COMMIT_BRANCH:-'-'}}"
HEAD_BRANCH="${CI_MERGE_REQUEST_SOURCE_BRANCH_NAME:-${CI_COMMIT_BRANCH:-'-'}}"

cecho "$YELLOW_BOLD" "=== TARGET Commit ==="
print_commit_info "$BASE_BRANCH" "$BASE_COMMIT"
echo

cecho "$YELLOW_BOLD" "=== SOURCE Commit ==="
print_commit_info "$HEAD_BRANCH" "$HEAD_SHA"
echo

CHANGED="$(git diff --name-only --diff-filter=ACMRTUXB "$BASE_COMMIT" "$HEAD_SHA" -- ':(top,glob)**/*.java')"

if [[ -z "$CHANGED" ]]; then
  echo "No changed Java files detected. Skipping Checkstyle."
  mkdir -p build/reports/checkstyle
  echo '[]' > gl-code-quality-report.json
  printf '<?xml version="1.0" encoding="UTF-8"?><testsuites name="checkstyle" tests="0" failures="0"/>' > build/reports/checkstyle/junit-report.xml
  exit 0
else
  echo "Changed Java files detected: "
  REPO_NAME="$(basename "$(git rev-parse --show-toplevel)")"

  # Derive the module affected
  module_list="$(
      printf '%s\n' "$CHANGED" \
        | awk -v repo="$REPO_NAME" '
            {
              p=$0
              if (p ~ /\/src\/main\/java\//) {
                sub(/\/src\/main\/java\/.*/, "", p); print p
              } else if (p ~ /\/src\/java\/main\//) {
                sub(/\/src\/java\/main\/.*/, "", p); print p
              } else {
                print repo
              }
            }
          ' \
        | sort -u
    )"

  # Print the module/files affected
  printf '%s\n' "$module_list" \
      | sort \
      | uniq -c \
      | awk '{ c=$1; $1=""; sub(/^ +/,""); printf "%s %d\n", $0, c }'

    total_changed=$(printf '%s\n' "$CHANGED" | wc -l | awk '{print $1}')
  echo
  echo "Total changed Java files: $total_changed"
fi

# Create comma-separated param for Gradle
files_param=$(printf '%s\n' "$CHANGED" | paste -sd ',' - | sed 's/,$//')

# --- 3. Run Checkstyle on Changed Files ---
gradle_cmd="gradle"; [[ -f ./gradlew ]] && gradle_cmd="./gradlew"

# Capture exit code without stopping the script, to allow report generation
checkstyle_exit_code=0
if $vcl_ok; then
  # Run Gradle, capture output, then filter out only Checkstyle WARN lines
  tmp_out="$(mktemp)"
  if "${gradle_cmd}" --no-daemon --console=plain --warning-mode=fail \
      -Pfiles="${files_param}" checkstyleFiles >"$tmp_out" 2>&1; then
    checkstyle_exit_code=0
  else
    checkstyle_exit_code=$?
  fi

  # Print everything except the specific Checkstyle WARN lines
  # Example WARN line format:
  # [ant:checkstyle] [WARN] /path/File.java:line:col: message [Rule]
  sed '/^\[ant:xslt\] Loading stylesheet/,/<\/xsl:stylesheet>/d' "$tmp_out" \
  | awk 'index($0,"[ant:checkstyle] [WARN]")==1 { next } { print }'

  rm -f "$tmp_out"
else
  # No VCL -> show warnings as usual
  "${gradle_cmd}" --no-daemon --console=plain --warning-mode=fail \
    -Pfiles="${files_param}" checkstyleFiles || checkstyle_exit_code=$?
fi

# --- 4. Merge Checkstyle Reports & Generate Code Quality Artifact ---
merged_xml="merged-checkstyle.xml"
merged_xml_dir="build/reports/checkstyle"
merged_xml_path="$merged_xml_dir/$merged_xml"
mkdir -p "$(dirname "$merged_xml_path")"

# Find all checkstyle XML files, excluding our merged file
if find . -type f -name "*.xml" | grep -E "/build/reports/checkstyle/" | grep -v "merged-checkstyle.xml" | grep -q .; then
    # Start the merged XML file
    printf '<?xml version="1.0" encoding="UTF-8"?>\n<checkstyle version="8.0">\n' > "$merged_xml_path"

    # Process all checkstyle XML files in one go
    find . -type f -name "*.xml" | grep -E "/build/reports/checkstyle/" | grep -v "merged-checkstyle.xml" | \
    xargs -I {} sh -c 'echo "Processing: {}"; sed -n "/<file /,/<\/file>/p" "{}"' >> "$merged_xml_path"

    # Close the merged XML file
    echo '</checkstyle>' >> "$merged_xml_path"
    echo "Merged Checkstyle report created at: $merged_xml_path"
else
    echo "No Checkstyle XML files found."
fi

# Convert the merged Checkstyle report to GitLab's Code Quality format
if $vcl_ok && [[ -f "$VCL_JAR" ]]; then
  if [[ -s $merged_xml_path ]]; then
    java -jar "$VCL_JAR" \
         -cc gl-code-quality-report.json \
         -v "CHECKSTYLE" "." ".*${merged_xml_path}" "Checkstyle"
  else
    echo "No Checkstyle report was generated. Creating an empty code quality report."
    echo '[]' > gl-code-quality-report.json
  fi
else
  echo "[INFO] VCL not available or jar missing, skipping Checkstyle report conversion."
  echo '[]' > gl-code-quality-report.json
fi

# -------- HTML paths for easy click-through ----------
find . -type f -name '*.html' -path '*/reports/*' | sort

echo "Checkstyle job finished. Exiting with code: ${checkstyle_exit_code}"
exit "${checkstyle_exit_code}"
