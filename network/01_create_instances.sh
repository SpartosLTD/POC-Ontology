#!/usr/bin/env bash

GROUP=$1
COUNT=$2
TYPE=${3:-n1-standard-4}

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

    echo "Creating instance ${INSTANCE_NAME} in zone ${ZONE}"
    gcloud beta -q compute --project=spartos-shared instances create $INSTANCE_NAME \
    --zone=$ZONE \
    --machine-type=${TYPE} --subnet=default \
    --network-tier=PREMIUM --maintenance-policy=MIGRATE \
    --service-account=568102141205-compute@developer.gserviceaccount.com \
    --scopes=https://www.googleapis.com/auth/devstorage.read_only,https://www.googleapis.com/auth/logging.write,https://www.googleapis.com/auth/monitoring.write,https://www.googleapis.com/auth/servicecontrol,https://www.googleapis.com/auth/service.management.readonly,https://www.googleapis.com/auth/trace.append \
    --tags=http-server,https-server --image=ubuntu-1604-xenial-v20180831 --image-project=ubuntu-os-cloud --boot-disk-size=20GB --boot-disk-type=pd-ssd \
    --boot-disk-device-name=$INSTANCE_NAME
done
