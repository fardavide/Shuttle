#!/usr/bin/env bash
set -eo pipefail

function main {

  # Run on if last commit is not a release
  local lastCommitMessage=$(git log -1 --pretty=%B)
  if [[ $lastCommitMessage == [release]* ]]; then
    echo "skipping, last commit is already a release"
    exit 0
  fi

  # Set Git credentials
  if [ -z "$GIT_EMAIL" ] || [ -z "$GIT_USERNAME" ] || [ -z "$GITHUB_TOKEN" ]; then
    echo "=> You must set the variables GIT_EMAIL, GIT_USERNAME and GITHUB_TOKEN to be able to commit and create release"
    exit 1
  fi

  git config --global user.email "$GIT_EMAIL"
  git config --global user.name "$GIT_USERNAME"

  git remote set-url origin "https://$GITHUB_TOKEN@github.com/$GIT_USERNAME/Shuttle.git/"

  ## COMMIT
  # Force releases.txt and build.gradle.kts
  git add -f ./app/build.gradle.kts;

  git status;

  git commit -m "[release] $CIRCLE_TAG"
  git push origin main;
}

main
