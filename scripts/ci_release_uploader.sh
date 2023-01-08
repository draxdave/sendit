#!/bin/bash

FILE_PATH=$1
REFRESH_TOKEN=$2
PROJECT_APP_ID=$3
RELEASE_NOTE=$4

echo "1$1"
echo "2$2"
echo "3$3"
echo "4$4"
#echo "5$5"

curl -sL firebase.tools | sed 's/sudo //g' | bash

firebasese login:ci

firebase appdistribution:distribute "$FILE_PATH" \
        --app "$PROJECT_APP_ID" \
        --token "$REFRESH_TOKEN" \
        --release-notes "$RELEASE_NOTE" \
        --groups "pre-release-qa" \
        --debug

#
#FILE_PATH=$1
#REFRESH_TOKEN=$2
#PROJECT_APP_ID=$3
#RELEASE_ID="7caah0g12si1o"
#CLIENT_ID=$4
#CLIENT_SECRET=$5
#ACCESS_TOKEN=""
#
#echo "1$1"
#echo "2$2"
#echo "3$3"
#echo "4$4"
#echo "5$5"
#
#function upload() {
#  local APP_NAME=$1
#  local ACCESS_TOKEN=$2
#  local FILE_PATH=$3
#
#  curl --location --request POST "https://firebaseappdistribution.googleapis.com/upload/v1/projects/$APP_NAME/releases:upload" \
#    --header "Authorization: Bearer $ACCESS_TOKEN" \
#    --header 'X-Goog-Upload-Protocol: raw' \
#    --header 'X-Goog-Upload-File-Name: release.aab' \
#    --header 'Content-Type: application/x-authorware-bin' \
#    --data-binary "@$FILE_PATH"
#}
#
#function distribute() {
#  local APP_NAME=$1
#  local ACCESS_TOKEN=$2
#  local APP_RELEASE_ID=$3
#
#  curl --location --request POST "https://firebaseappdistribution.googleapis.com/upload/v1/projects/$APP_NAME/releases/$APP_RELEASE_ID:distribute" \
#    --header "Authorization: Bearer $ACCESS_TOKEN" \
#    --header 'X-Goog-Upload-Protocol: raw' \
#    --header 'X-Goog-Upload-File-Name: release.aab' \
#    --header 'Content-Type: application/x-authorware-bin' \
#    --data '{"groupAliases":["pre-release-qa"]}'
#}
#
#function getAccessToken() {
#  local REFRESH_TOKEN=$1
#  local CLIENT_ID=$2
#  local CLIENT_SECRET=$3
#
#  curl -s --location --request POST 'https://oauth2.googleapis.com/token' \
#    --header 'content-type: application/x-www-form-urlencoded' \
#    --data-urlencode "refresh_token=$REFRESH_TOKEN" \
#    --data-urlencode 'redirect_uri=http://localhost:8080/' \
#    --data-urlencode "client_id=$CLIENT_ID" \
#    --data-urlencode "client_secret=$CLIENT_SECRET" \
#    --data-urlencode 'grant_type=refresh_token' \
#    --data-urlencode 'scope=https://www.googleapis.com/auth/cloud-platform'
#
#}
#
#function extract_access_token_from_json() {
#  local JSON=$1
#  local array=($(echo "$JSON" | sed -nr 's/.*"(.*)".*"(.*)".*/\1:\2/p'))
#  local access_token_arr=(${array[0]//:/ })
#  echo "${access_token_arr[1]}"
#}
#
#function extract_operation_id_from_json() {
#  local JSON=$1
#  array=(`echo "$JSON" | sed 's/\//\n/g'`)
#  op_id=${array[9]}
#  array=(`echo "$op_id" | sed 's/"/\n/g'`)
#  op_id=${array[0]}
#  echo "$op_id"
#}
#
#ACCESS_TOKEN_JSON=$(getAccessToken "$REFRESH_TOKEN" "$CLIENT_ID" "$CLIENT_SECRET")
#ACCESS_TOKEN=$(extract_access_token_from_json "$ACCESS_TOKEN_JSON")
#echo "ACCESS_TOKEN $ACCESS_TOKEN"
#
#UPLOAD_JSON=$(upload "$PROJECT_APP_ID" "$ACCESS_TOKEN" "$FILE_PATH")
#OP_ID=$(extract_operation_id_from_json "$UPLOAD_JSON")
#
#upload "$PROJECT_APP_ID" "$ACCESS_TOKEN" "$FILE_PATH"
##distribute "$PROJECT_APP_ID" "$ACCESS_TOKEN" "$RELEASE_ID"
#firebase appdistribution:distribute \
#        --app "$PROJECT_APP_ID" \
#        --release-binary-file \
#        --token "$REFRESH_TOKEN" \
#        --release-notes "Text of release notes" \
#        --groups "pre-release-qa" \
#        --debug
#
#
