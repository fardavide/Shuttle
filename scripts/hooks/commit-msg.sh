#!/bin/bash

#
# Git commit-msg hook used to enforce the same commit convention (https://www.conventionalcommits.org/).
#
# Examples of valid commit messages:
# - chore: this is the message
# - feat(news): this is the message
# - refactor(news)!: this is the message
#
# Examples on invalid commit messages:
# - chore:
# - fix(): this is the message
# - docs: This is the message
# - random_keyword: this is the message
#

if [[ "$OSTYPE" == "darwin"* ]]; then
  SCRIPT_DIR="$(dirname "$(perl -MCwd -e "print Cwd::abs_path shift" "${BASH_SOURCE[0]}")")"
else
  SCRIPT_DIR="$(dirname "$(readlink -f "${BASH_SOURCE[0]}")")"
fi
source "$SCRIPT_DIR/../common/common.sh"

P_TAG="commit-msg hook"

COMMIT_MSG_FILE=$1

main() {
  local lines_count
  lines_count=$(grep -c ^ "$COMMIT_MSG_FILE")
  if [[ $lines_count -gt 1 ]]; then
    print_error "$P_TAG" "the commit message should be max 1 line"
    exit 1
  fi

  local pattern="^(build|chore|ci|docs|feat|fix|perf|refactor|style|test)+(\([a-z0-9\-_\/]+\))?!?:\s[a-z][a-zA-Z0-9 -_\/\`]+$"
  if ! grep -q -E "$pattern" "$COMMIT_MSG_FILE"; then
    print_error "$P_TAG" "the commit message does not follow https://www.conventionalcommits.org/"
    exit 1
  fi

  print_success "$P_TAG" "the commit message is valid"
}

main
