#!/usr/bin/env bash

GROUP=$1
COUNT=$2
COMMAND=$3

declare -A zones

zones[0]="europe-west3-b"
zones[1]="europe-west3-a"
zones[2]="europe-west3-b"
zones[3]="europe-west3-c"
zones[4]="europe-west2-b"
zones[5]="europe-west2-c"
zones[6]="europe-west1-b"
zones[7]="europe-west1-c"

for (( i=0; i<$COUNT; i++ ))
do
    let ZONE_INDEX=$i%7
    ZONE=${zones[$ZONE_INDEX]}
    INSTANCE_NAME=${GROUP}-${i}
    echo "Executing $COMMAND on instance $INSTANCE_NAME"
    gcloud compute instances $COMMAND $INSTANCE_NAME --zone=$ZONE &
done