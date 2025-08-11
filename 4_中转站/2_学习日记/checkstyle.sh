# For debugging purpose
# set -x
export PS4='[$(date +%H:%M:%S)] ${0##*/}:${LINENO}: '
set -Eeuo pipefail

# --- Helper methods ---
curl_dl() {
  curl --retry 3 --retry-all-errors --connect-timeout 10 --max-time 120 -fSL "$1" -o "$2"
}

verify_sha() {
  local file="$1" url="$2"
  if curl -fsSL "${url}.sha256" -o "${file}.sha256"; then
    sha256sum -c <(awk '{print $1"  '"$file"'"}' "${file}.sha256")
  elif curl -fsSL "${url}.sha1" -o "${file}.sha1"; then
    sha1sum -c <(awk '{print $1"  '"$file"'"}' "${file}.sha1")
  else
    echo "[ERROR] No checksum available for ${file}; proceeding without verification"
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

if [[ ! -f "$VCL_JAR" ]]; then
  echo "[INFO] Downloading $VCL_JAR (Version: ${VCL_VER})"
  curl_dl "$VCL_URL" "$VCL_JAR" || { echo "[ERROR] Failed to download $VCL_JAR"; exit 1; }
  verify_sha "$VCL_JAR" "$VCL_URL" || { echo "[ERROR] checksum failed for $VCL_JAR"; exit 1; }
fi

# ------------------ main --------------------
BASE_COMMIT="$(pick_diff_base)"
echo "Diffing against base commit: $BASE_COMMIT"

CHANGED="$(git diff --name-only --diff-filter=ACMRTUXB "$BASE_COMMIT" "$CI_COMMIT_SHA" -- ':(top,glob)**/*.java')"

if [[ -z "$CHANGED" ]]; then
  echo "No changed Java files detected. Skipping Checkstyle."
  mkdir -p build/reports/checkstyle
  echo '[]' > gl-code-quality-report.json
  printf '<?xml version="1.0" encoding="UTF-8"?><testsuites name="checkstyle" tests="0" failures="0"/>' > build/reports/checkstyle/junit-report.xml
  exit 0
fi

# Create comma-separated param for Gradle
files_param=$(printf '%s\n' "$CHANGED" | paste -sd ',' - | sed 's/,$//')

# --- 3. Run Checkstyle on Changed Files ---
gradle_cmd="gradle"; [[ -f ./gradlew ]] && gradle_cmd="./gradlew"

# Capture exit code without stopping the script, to allow report generation
checkstyle_exit_code=0
"${gradle_cmd}" --no-daemon --console=plain --warning-mode=fail --info \
  -Pfiles="${files_param}" checkstyleFiles || checkstyle_exit_code=$?

# --- 4. Merge Checkstyle Reports & Generate Code Quality Artifact ---
merged_xml="merged-checkstyle.xml"
merged_xml_dir="build/reports/checkstyle"
merged_xml_path="$merged_xml_dir/$merged_xml"
mkdir -p "$(dirname "$merged_xml_path")"

# Find and merge individual XML reports into one
if find . -type f -path '*/build/reports/checkstyle/*.xml' \
-not -name "$(basename "$merged_xml_path")" | grep -q .; then
  printf '<?xml version="1.0" encoding="UTF-8"?><checkstyle version="8.0">\n' > "$merged_xml_path"
  find . -type f -path '*/build/reports/checkstyle/*.xml' -not -name "$(basename "$merged_xml_path")" \
    -exec sed -n '/<file /,/<\/file>/p' {} \; >> "$merged_xml_path"
  echo '</checkstyle>' >> "$merged_xml_path"
fi

# Convert the merged Checkstyle report to GitLab's Code Quality format
if [[ -s $merged_xml_path ]]; then
  java -jar "$VCL_JAR" \
       -cc gl-code-quality-report.json \
       -v "CHECKSTYLE" "." ".*${merged_xml_path}" "Checkstyle"
else
  echo "No Checkstyle report was generated. Creating an empty code quality report."
  echo '[]' > gl-code-quality-report.json
fi

# -------- HTML paths for easy click-through ----------
find . -type f -name '*.html' -path '*/reports/*' | sort

echo "Checkstyle job finished. Exiting with code: ${checkstyle_exit_code}"
exit "${checkstyle_exit_code}"