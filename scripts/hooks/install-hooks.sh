#!/bin/bash

#
# Installs all the Git hooks symlinking them into `.git/hooks`.
#

SCRIPT_DIR="$(dirname "${BASH_SOURCE[0]}")"
source "$SCRIPT_DIR/../common/common.sh"

P_TAG="install-hooks"

main() {
  if prompt_yes_no $P_TAG "do you want to install our shared Git hooks?"; then
    install_hooks
  else
    print_info $P_TAG "skipping installation of Git hooks"
  fi
}

install_hooks() {
  local git_hooks_dir="$SCRIPT_DIR/../../.git/hooks"
  mkdir -p "$git_hooks_dir"
  # Symlinks the Git hooks (the symlink is resolved relative to its location).
  ln -s -f "../../scripts/hooks/commit-msg.sh" "$git_hooks_dir/commit-msg"
  ln -s -f "../../scripts/hooks/pre-commit.sh" "$git_hooks_dir/pre-commit"
  print_success $P_TAG "Git hooks successfully installed"
}

main
