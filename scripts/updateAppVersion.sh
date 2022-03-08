#!/usr/bin/env bash
set -eo pipefail

function main {

  local version=$(git describe --tags)
  local versionParts=(${version//./ })
  local versionCode=$(($((versionParts[0] * 10000)) + $((versionParts[1] * 100)) + $((versionParts[2] * 1))))

  local fileName="./app/build.gradle.kts"

  local versionNameS="versionName = \".*\""
  local versionNameR="versionName = \"$version\""

  local versionCodeS="versionCode = [0-9]*"
  local versionCodeR="versionCode = $versionCode"

  sed -i.bak "s/$versionNameS/$versionNameR/g" $fileName
  sed -i.bak "s/$versionCodeS/$versionCodeR/g" $fileName
  rm $fileName.bak
}

main
