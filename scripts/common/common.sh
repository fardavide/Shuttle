#!/bin/bash

#
# Common environment variables and functions used across all the scripts.
#

COLOR_NULL="\033[0m"
COLOR_B_NULL="\033[1m"
COLOR_B_GREEN="\033[1;32m"
COLOR_B_RED="\033[1;31m"

print_info() {
  local tag=$1
  local msg=$2
  echo -e "[$tag] ${COLOR_B_NULL}INFO:${COLOR_NULL} $msg"
}

print_success() {
  local tag=$1
  local msg=$2
  echo -e "[$tag] ${COLOR_B_GREEN}SUCCESS:${COLOR_NULL} $msg"
}

print_error() {
  local tag=$1
  local msg=$2
  echo -e "[$tag] ${COLOR_B_RED}ERROR:${COLOR_NULL} $msg"
}

prompt_yes_no() {
  local tag=$1
  local question=$2
  while true; do
    read -r -p "$(__print_prompt "$tag" "$question") [y/n] " yn
    case $yn in
    [Yy]) return 0 ;;
    [Nn]) return 1 ;;
    *) __print_prompt "$tag" "please answer \"y\" or \"n\"." ;;
    esac
  done
}

arr_join_to_str() {
  local input_arr=("$@")
  IFS=,
  printf "%s" "${input_arr[*]}"
}

__print_prompt() {
  local tag=$1
  local msg=$2
  echo -e "[$tag] ${COLOR_B_NULL}PROMPT:${COLOR_NULL} $msg"
}
