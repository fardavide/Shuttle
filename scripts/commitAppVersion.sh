#!/usr/bin/env bash
set -eo pipefail

function main {

  # Set Git credentials
  if [ -z "$GIT_EMAIL" ] || [ -z "$GIT_USERNAME" ]; then
    echo "=> You must set the variables GIT_EMAIL and GIT_USERNAME to be able to push to repository"
    exit 1
  fi

  git config --global user.email "$GIT_EMAIL"
  git config --global user.name "$GIT_USERNAME"

  ## COMMIT
  # Force releases.txt and build.gradle.kts
  git add -f ./app/build.gradle.kts;

  git status;

  local version=$APP_VERSION
  git commit -m "[release] $version"
}

main
