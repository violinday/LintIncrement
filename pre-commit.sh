#!/usr/bin/env bash

REPO_ROOT_DIR="$(git rev-parse --show-toplevel)"

files=$(git diff --name-only --diff-filter=ACMRTUXB HEAD~0)

#echo ${files}

./gradlew LintIncrementTaskDebug

OUTPUT=$(./gradlew LintIncrementTaskDebug | grep "Lint found" 2>&1)

ERROR=$(echo $OUTPUT|grep -o '[0-9]\+' 2>&1)

ARR=($ERROR)

for DATA in ${ARR[@]}
do
    if [ "$DATA" -ne 0 ];
    then
        open "${REPO_ROOT_DIR}/app/build/reports/lint-results-debug.html"
        exit 1
    fi
done