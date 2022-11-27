#!/bin/bash
java -jar server-a-1.0.jar >/dev/null &
echo "STEP A"
url="localhost:8080"
echo "STEP B"
while true
do
  if [ "$(curl -sL -w '%{http_code}' ${url} -o /dev/null)" = "200" ];then
    java -jar client-a-1.0.jar >/dev/null &
    echo "Success"
    break
  fi
done
