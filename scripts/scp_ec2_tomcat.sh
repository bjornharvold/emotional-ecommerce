#!/bin/sh

LOCAL_DIR=`pwd`

echo "Currently in directory: $LOCAL_DIR"

# first we need to copy the artifacts to our EC2 server
scp -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -i /mnt/bamboo-ebs/id-lela ./lela-web/target/lela.war root@ec2-107-22-183-117.compute-1.amazonaws.com:dl
scp -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -i /mnt/bamboo-ebs/id-lela ./lela-ingest/target/ingest.war root@ec2-107-22-183-117.compute-1.amazonaws.com:dl
scp -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -i /mnt/bamboo-ebs/id-lela ./lela-data/target/data.war root@ec2-107-22-183-117.compute-1.amazonaws.com:dl

# default exit call and we can call it a day
exit 0