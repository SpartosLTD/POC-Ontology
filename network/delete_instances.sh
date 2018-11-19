#!/usr/bin/env bash
GROUP=$1
COUNT=$2

echo "Deleteing network $GROUP"
./run_command_for_every_instance.sh $GROUP $COUNT delete
