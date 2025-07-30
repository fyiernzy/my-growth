#!/usr/bin/env bash
set -Eeuo pipefail

# =========================================================
# Colors (used only for headers/footers)
# =========================================================
init_colors() {
  if [[ -t 1 && -z "${NO_COLOR:-}" ]] && command -v tput >/dev/null 2>&1; then
    local n; n="$(tput colors 2>/dev/null || echo 0)"
    if (( n >= 8 )); then
      BLUE="$(tput setaf 4)"; GREEN="$(tput setaf 2)"
      BOLD="$(tput bold)"; RESET="$(tput sgr0)"; return
    fi
  fi
  BLUE=''; GREEN=''; BOLD=''; RESET=''
}

header()  { printf '%b--- %s%s%s ---%b\n' "$BLUE" "$BOLD" "$1" "$RESET$BLUE" "$RESET"; }
footer()  { printf '%b%s%b\n' "${GREEN}${BOLD}" "$1" "$RESET"; }

# Plain (no color) lines during setup/validation
info()    { printf '  - %s\n' "$1"; }
success() { printf '  - %s\n' "$1"; }
warn()    { printf '  - Warning: %s\n' "$1"; }
error()   { printf '  - Error: %s\n' "$1"; }

# =========================================================
# Constants
# =========================================================
readonly GITHOOKS_DIR_NAME=".githooks"
readonly HOOKS=(pre-commit prepare-commit-msg pre-push)

# =========================================================
# Utilities
# =========================================================
resolve_path() {
  local base="$1" p="$2"
  if [[ "$p" = /* ]]; then
    printf '%s\n' "$p"
  else
    printf '%s\n' "$base/$p"
  fi
}

# =========================================================
# Setup helpers
# =========================================================
# Ensure each named hook is executable if present
setup_hooks_executable() {
  local hooks_dir="$1"; shift
  local -a names=("$@")

  for h in "${names[@]}"; do
    local path="$hooks_dir/$h"
    if [[ -f "$path" ]]; then
      chmod +x "$path" || true
      success "Setup '$h' successfully"
    else
      warn "Git hook '$h' not found at '$path'. Skipping chmod."
    fi
  done
}

# Ensure core.hooksPath points to .githooks for this repo
setup_core_hooks_path() {
  local repo_root="$1" hooks_dir="$2"
  local configured desired=".githooks"

  configured="$(git config --get core.hooksPath || true)"

  if [[ -n "$configured" ]]; then
    local resolved want got
    resolved="$(resolve_path "$repo_root" "$configured")"
    want="$(cd "$hooks_dir" 2>/dev/null && pwd -P)"
    got="$(cd "$resolved" 2>/dev/null && pwd -P || echo '')"

    if [[ -n "$got" && "$got" == "$want" ]]; then
      success "core.hooksPath already set ('$configured' → $got)"
      return 0
    else
      warn "core.hooksPath currently '$configured' (→ $got); updating to '$desired'."
    fi
  else
    info "core.hooksPath is not set; configuring to '$desired'."
  fi

  if git config --local core.hooksPath "$desired"; then
    success "Configured core.hooksPath to '$desired'."
    return 0
  else
    error "Failed to set core.hooksPath to '$desired'."
    return 1
  fi
}

# =========================================================
# Validation helpers
# =========================================================
validate_hooks_executable() {
  local hooks_dir="$1"; shift
  local -a names=("$@")
  local missing=0 not_exec=0

  for h in "${names[@]}"; do
    local path="$hooks_dir/$h"
    if [[ ! -f "$path" ]]; then
      error "Missing hook '$h' at '$path'"
      ((missing++))
      continue
    fi
    if [[ ! -x "$path" ]]; then
      error "Hook exists but is not executable: '$path' (run: chmod +x '$path')"
      ((not_exec++))
      continue
    fi
    success "Validated executable hook '$h'"
  done

  (( missing == 0 && not_exec == 0 ))
}

# @param repo_root: absolute path to repository root
# @param hooks_dir: expected hooks directory path (e.g., /path/to/repo/.githooks)
validate_core_hooks_path() {
  local repo_root="$1" hooks_dir="$2"
  local configured resolved want got

  configured="$(git config --get core.hooksPath || true)"
  if [[ -z "$configured" ]]; then
    error "core.hooksPath is not set."
    printf '      Set it with: %s\n' "git config core.hooksPath .githooks"
    return 1
  fi

  resolved="$(resolve_path "$repo_root" "$configured")"
  want="$(cd "$hooks_dir" 2>/dev/null && pwd -P)"
  got="$(cd "$resolved" 2>/dev/null && pwd -P || echo '')"

  if [[ -n "$got" && "$got" == "$want" ]]; then
    success "core.hooksPath is set correctly ('$configured' → $got)"
    return 0
  fi

  error "core.hooksPath mismatch. Configured: '$configured' (→ $got), expected: '$want'"
  printf '      Fix with: %s\n' "git config core.hooksPath .githooks"
  return 1
}

# =========================================================
# Final tips
# =========================================================
print_success_tips() {
  cat <<'EOF'

Next steps (to test your setup):
  • Verify where hooksPath comes from:
      git config --show-origin --get core.hooksPath
  • List hooks and permissions:
      ls -l .githooks
  • Run a quick empty commit to see hooks fire:
      git commit --allow-empty -m "chore: validate hooks"
  • (If you use a commit message hook) Try a message without IssueID to confirm it fails, then with one to confirm success:
      git commit --allow-empty -m "test: no IssueID"    # expect fail
      git commit --allow-empty -m "IssueID: PMS_1234"  # expect pass

EOF
}

print_error_tips() {
  cat <<'EOF'

Troubleshooting tips:
  • Check current hooksPath and source:
      git config --show-origin --get core.hooksPath
  • Ensure hooks exist and are executable:
      ls -l .githooks
      chmod +x .githooks/*
  • Run a hook manually with debug:
      bash -x .githooks/pre-commit
  • Normalize line endings on Windows (CRLF can break shebang):
      git config core.autocrlf input
      dos2unix .githooks/*
  • Reset hooksPath (local/global) if needed:
      git config --unset core.hooksPath
      git config --global --unset core.hooksPath
      git config --local core.hooksPath .githooks

EOF
}

# =========================================================
# Main
# =========================================================
main() {
  init_colors

  local project_root githooks_dir
  project_root="$(git rev-parse --show-toplevel 2>/dev/null || true)"
  [[ -n "$project_root" ]] || { printf '%s\n' 'Not a git repository.' >&2; exit 1; }
  githooks_dir="$project_root/$GITHOOKS_DIR_NAME"

  # --- Initial Git Hooks Setup ---
  header 'Initial Git Hooks Setup'
  setup_hooks_executable "$githooks_dir" "${HOOKS[@]}"
  setup_core_hooks_path "$project_root" "$githooks_dir"

  # --- Initial Setup Complete ---
  header 'Initial Setup Complete'
  footer 'All hooks and git core.hooksPath are all set.'
  printf '\n'

  # --- Validating configuration ---
  header 'Validating Configuration'
  local ok=0
  validate_core_hooks_path "$project_root" "$githooks_dir" && ok=$((ok+1))
  validate_hooks_executable "$githooks_dir" "${HOOKS[@]}" && ok=$((ok+1))

  # --- Validation Complete ---
  header 'Validation Complete'
  if (( ok == 2 )); then
    footer 'All hooks and git core.hooksPath are all validated.'
    print_success_tips
    exit 0
  else
    error 'Validation failed. See messages above.'
    print_error_tips
    exit 1
  fi
}

main "$@"
