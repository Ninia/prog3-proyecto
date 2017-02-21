#!/usr/bin/env bash

if [ "$EUID" -ne 0 ]
  then echo "Script must be run as root :/"
  exit
fi

INSTAL_DIR="/usr/local/proyectopbd"

echo "Creating directory $INSTAL_DIR..."
mkdir ${INSTAL_DIR}
mkdir ${INSTAL_DIR}/tmp

echo "Generating CPU usage script..."
cat scripts/cpu-usage.sh > ${INSTAL_DIR}/cpu-usage.sh
echo "Generating memory usage script..."
cat scripts/mem-usage.sh  > ${INSTAL_DIR}/mem-usage.sh

echo "Setting up permissions..."
chmod +755 ${INSTAL_DIR}/*.sh
chmod +777 ${INSTAL_DIR}/tmp

echo "done! Files can be found at $INSTAL_DIR"