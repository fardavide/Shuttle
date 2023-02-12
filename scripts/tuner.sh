#!/bin/bash

#
# Entry point for all the configuration scripts.
#

SCRIPT_DIR="$(dirname "${BASH_SOURCE[0]}")"
source "$SCRIPT_DIR/common/common.sh"

P_TAG="tuner"

# Scripts that need to be executed (the order is respected).
TUNER_SCRIPTS=(
  "preconditions.sh"
  "linters/bump_detekt_cli.sh"
  "hooks/install-hooks.sh"
)

main() {
  print_header
  local script_name
  for script_name in "${TUNER_SCRIPTS[@]}"; do
    if ! execute_script "$script_name"; then
      print_divider
      break
    fi
    print_divider
  done
}

execute_script() {
  local script_name=$1
  local script="$SCRIPT_DIR/$script_name"
  if [[ -x "${script}" ]]; then
    print_info $P_TAG "running script '$script_name'"
    "${script}" || return 1
  else
    print_error $P_TAG "script '$script_name' does not exist or is not executable"
    return 1
  fi
}

print_header() {
  cat <<EOF
╔════════════════════════════╗
║   Starting Shuttle tuner   ║
╚════════════════════════════╝

EOF
}

print_divider() {
  echo "═════════════════════════════════"
}

main
