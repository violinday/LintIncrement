#!/usr/bin/env bash

REPO_ROOT_DIR="$(git rev-parse --show-toplevel)"

files=$(git ls-files -m | grep -Ei "\.java")

echo ${files}