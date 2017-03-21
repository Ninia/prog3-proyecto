#!/bin/bash

#!/usr/bin/env bash

if [ "$EUID" -ne 0 ]
  then echo "Script must be run as root :/"
  exit
fi

INSTALL_DIR="/usr/local/p3p"

echo "[i] Creating directory $INSTALL_DIR..."
mkdir ${INSTALL_DIR}
mkdir ${INSTALL_DIR}/tmp

echo "[i] Generating CPU usage script..."
cat scripts/cpu-usage.sh > ${INSTALL_DIR}/cpu-usage.sh
echo "[i] Generating CPU usage script..."
cat scripts/cpu-usage.sh > ${INSTALL_DIR}/cpu-temperature.sh
echo "[i] Generating memory usage script..."
cat scripts/mem-usage.sh  > ${INSTALL_DIR}/mem-usage.sh

echo "[i] Setting up permissions..."
chmod +755 ${INSTALL_DIR}/*.sh
chmod +777 ${INSTALL_DIR}/tmp

echo "[i] done! Files can be found at $INSTALL_DIR"

echo "Installation process ended."
