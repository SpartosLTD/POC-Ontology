#!/usr/bin/env bash

GROUP=${1:-ethnode}
COUNT=${2:-3}
NETWORK_FOLDER=${3:-$GROUP}

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
    echo "Zone index $ZONE_INDEX"
    ZONE=${zones[$ZONE_INDEX]}
    INSTANCE_NAME=${GROUP}-${i}
    echo "Bootstrapping node $INSTANCE_NAME"
    gcloud compute scp --zone=$ZONE --recurse $NETWORK_FOLDER $INSTANCE_NAME:~/
done