#!/bin/bash

today=$(date +%Y%m%d) # or: printf -v today '%(%Y%m%d)T' -1
number=0

fname=$today-output

while [ -e "$fname" ]; do
  printf -v fname '%s-%02d-output' "$today" "$((++number))"
done

mkdir "$fname"

docker cp app-automation:ddg-weather-svc-asset.html "$fname"/.
docker cp app-automation:ddg-weather-svc-asset.png "$fname"/.
docker cp app-automation:ddg-weather-svc-asset.json "$fname"/.
