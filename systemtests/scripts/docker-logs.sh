#!/usr/bin/env bash
LOG_DIR=$1

mkdir -p $LOG_DIR

while [ true ]; do
    sudo rsync -av /var/log/containers/* $LOG_DIR/
    sleep 5
done
