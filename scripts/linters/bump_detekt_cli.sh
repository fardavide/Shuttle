#!/bin/bash

#
# Downloads or generates the necessary Detekt jars to perform checks without invoking Gradle.
# This script will output in `detekt/bin/` which is excluded in our .gitignore.
# The output is cached and will be invalidated only upon version changes.
# The remote jars versions are extracted from our Gradle Version Catalog (`gradle/libs.versions.toml`).
# To force the invalidation you can invoke this script with the `force` argument:
# `./scripts/detekt/bump_detekt_cli.sh force`
#

SCRIPT_DIR="$(dirname "${BASH_SOURCE[0]}")"
source "$SCRIPT_DIR/../common/common.sh"

P_TAG="bump-detekt-cli"

ROOT_DIR="$SCRIPT_DIR/../.."
VERSION_CATALOG_PATH="$ROOT_DIR/gradle/libs.versions.toml"
DETEKT_BIN_DIR="$ROOT_DIR/detekt/bin"

main() {
  force_bump="$1"

  local new_detekt_version new_detekt_twitter_compose_version
  new_detekt_version=$(extract_version_from_catalog "detekt")
  new_detekt_twitter_compose_version=$(extract_version_from_catalog "detektRulesCompose")

  local pids=()
  bump_detekt_cli "$new_detekt_version" &
  pids+=($!)
  bump_detekt_formatting "$new_detekt_version" &
  pids+=($!)
  bump_detekt_twitter_compose "$new_detekt_twitter_compose_version" &
  pids+=($!)
  bump_local_jars &
  pids+=($!)

  local failed_processes=0
  for pid in "${pids[@]}"; do
    # If the command fails, increment the failed_processes count.
    wait "$pid" || ((failed_processes++))
  done

  # If at least one process failed, reflect the failure with a non-zero exit status.
  exit "$failed_processes"
}

bump_detekt_cli() {
  local version=$1
  bump_detekt_jar "detekt_cli" \
    "$version" \
    "https://github.com/detekt/detekt/releases/download/v$version/detekt-cli-$version-all.jar"
}

bump_detekt_formatting() {
  local version=$1
  bump_detekt_jar "detekt_formatting" \
    "$version" \
    "https://github.com/detekt/detekt/releases/download/v$version/detekt-formatting-$version.jar"
}

bump_detekt_twitter_compose() {
  local version=$1
  bump_detekt_jar "detekt_twitter_compose" \
    "$version" \
    "https://github.com/twitter/compose-rules/releases/download/v$version/detekt-twitter-compose-$version-all.jar"
}

bump_detekt_jar() {
  local id=$1
  local version=$2
  local url=$3
  local jar_file="$DETEKT_BIN_DIR/$id.jar"
  local version_file="$DETEKT_BIN_DIR/${id}_version.txt"

  mkdir -p "$DETEKT_BIN_DIR"

  local current_version
  if [[ $force_bump != "force" && -f "$jar_file" && -f "$version_file" ]]; then
    read -r current_version <"$version_file"
  fi
  if [[ "$version" == "$current_version" ]]; then
    print_info $P_TAG "$id $version up-to-date"
  else
    print_info $P_TAG "downloading $id $version"
    download_file "$url" "$jar_file"
    echo "$version" >"$version_file"
    print_success $P_TAG "$id $version successfully downloaded"
  fi
}

bump_local_jars() {
  local local_rules_dir="$ROOT_DIR/detekt/rules/"
  local local_rules_hash_file="$DETEKT_BIN_DIR/detekt_local_rules_hash.txt"
  local local_rules_jar="$local_rules_dir/build/libs/rules.jar"

  mkdir -p "$DETEKT_BIN_DIR"

  local cached_commit_hash
  if [[ $force_bump != "force" && -f "$local_rules_jar" && -f "$local_rules_hash_file" ]]; then
    read -r cached_commit_hash <"$local_rules_hash_file"
  fi

  local local_rules_commit_hash
  local_rules_commit_hash="$(git log --pretty=format:'%H' -n 1 "$local_rules_dir")"
  if [[ "$local_rules_commit_hash" == "$cached_commit_hash" ]]; then
    print_info $P_TAG "local rules jars up-to-date"
  else
    print_info $P_TAG "generating local rules jars"
    (cd "$ROOT_DIR" && ./gradlew :detekt:rules:jar) || {
      print_error $P_TAG "failed to generate local rules jars"
      exit 1
    }
    echo "$local_rules_commit_hash" >"$local_rules_hash_file"
    print_success $P_TAG "local rules jars successfully generated"
  fi
}

extract_version_from_catalog() {
  local dep_name=$1
  # Greps lines starting with `dep_name = "` and extracts the text between the double quotes.
  grep "$dep_name = \".*\"" "$VERSION_CATALOG_PATH" | cut -d'"' -f2
}

download_file() {
  local url=$1
  local output=$2
  if command -v curl >/dev/null 2>&1; then
    curl -sSL "$url" -o "$output"
  elif command -v wget >/dev/null 2>&1; then
    wget -q "$url" -O "$output"
  else
    print_error $P_TAG "no 'curl' or 'wget' installed"
    exit 1
  fi
}

main "$@"
