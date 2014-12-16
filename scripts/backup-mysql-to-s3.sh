#!/bin/sh

# e.g. 20120623
NOW=$(date +"%Y%m%d")

# go into the mysql backup directory
cd /mnt/backup

# compress the mysql backup directory
tar cvzf db-backup-$NOW.tar.gz db

# upload gzip to s3
s3cmd put db-backup* s3://qa-mysql-backup

# remove tar
rm -f db-backup*

# done

