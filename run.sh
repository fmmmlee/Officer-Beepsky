#!/bin/bash
BRANCH="master"
OPTIND=1

while getopts "hd" opt; do
    case "$opt" in
    h)
        echo "Usage: $0 [-h <for help>] [-d <for dev branch>]"
        exit 0
        ;;
    d)  BRANCH="dev"
        ;;
    esac
done

rm -rf Officer-Beepsky
git clone -b $BRANCH https://github.com/CorruptComputer/Officer-Beepsky
cd Officer-Beepsky

echo "What is the Discord Token? "
read token

echo "What is the Discord ID of the owner? "
read owner

for((i = 1; i > 0; i=$?)){
    git pull origin $BRANCH
    chmod +x gradlew
    ./gradlew fatJar
    java -jar build/libs/Officer-Beepsky-*.jar "$token" "$owner"
}

exit 0
