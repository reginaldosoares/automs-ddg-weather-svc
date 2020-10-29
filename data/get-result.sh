#!/bin/bash

today=$(date +%Y%m%d) # or: printf -v today '%(%Y%m%d)T' -1
number=0

fname=$today-output

while [ -e "$fname" ]; do
  printf -v fname '%s-%02d-output' "$today" "$((++number))"
done

mkdir "$fname"

docker cp app-automation:sample-automation-asset.html "$fname"/.
docker cp app-automation:sample-automation-asset.png "$fname"/.
docker cp app-automation:sample-automation-asset.json "$fname"/.
