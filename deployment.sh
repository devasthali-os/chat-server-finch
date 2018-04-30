#!/bin/bash

echo "=========================="
echo "Running chat-server in $APP_ENVIRONMENT"
echo "=========================="

java -jar -DAPP_ENVIRONMENT=$APP_ENVIRONMENT /home/chatServerApi*.jar
