#!/usr/bin/env bash

REPO_ROOT_DIR="$(git rev-parse --show-toplevel)"
files=$(git diff --name-only --diff-filter=ACMRTUXB HEAD~0)
lint_result="$(./gradlew LintIncrementTaskDebug | awk '/Lint found|Wrote HTML/' | sed 's/[[:space:]]//g' 2>&1)"
echo ${lint_result}
arr_lint_result=($lint_result)
length_lint_result=${#arr_lint_result[@]}
echo ${length_lint_result}
echo ${arr_lint_result[0]}
echo ${arr_lint_result[1]}
echo ${arr_lint_result[2]}
echo ${arr_lint_result[3]}
html=""
for((i=0;i<$length_lint_result;i++));
do
    data=${arr_lint_result[i]}
    echo ${data}
    if [[ $data == W* ]]; then
        html=${data##*file://}
        echo ${html}
    else
        error=$(echo ${data}|grep -o '[0-9]\+' 2>&1)
        ARR=($error)
        for data_error in ${ARR[@]}
        do
            if [ "$data_error" -ne 0 ];
            then
                open ${html}
                break
            fi
        done
    fi

done