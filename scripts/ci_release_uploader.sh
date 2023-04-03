#!/bin/bash

FILE_PATH=$1
REFRESH_TOKEN=$2
PROJECT_APP_ID=$3
RELEASE_NOTE=$4

curl -sL firebase.tools | sed 's/sudo //g' | bash
firebasese login:ci

firebase appdistribution:distribute "$FILE_PATH" \
        --app "$PROJECT_APP_ID" \
        --token "$REFRESH_TOKEN" \
        --release-notes "$RELEASE_NOTE" \
        --groups "pre-release-qa" \
        --debug