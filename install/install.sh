#!/usr/bin/env bash

if [ "$EUID" -ne 0 ]
  then echo "Script must be run as root :/"
  exit
fi

INSTALL_DIR="/usr/local/proyectopbd"

echo "[i] Creating directory $INSTALL_DIR..."
mkdir ${INSTALL_DIR}
mkdir ${INSTALL_DIR}/tmp

echo "[i] Generating CPU usage script..."
cat scripts/cpu-usage.sh > ${INSTALL_DIR}/cpu-usage.sh
echo "[i] Generating memory usage script..."
cat scripts/mem-usage.sh  > ${INSTALL_DIR}/mem-usage.sh

echo "[i] Setting up permissions..."
chmod +755 ${INSTALL_DIR}/*.sh
chmod +777 ${INSTALL_DIR}/tmp

echo "done! Files can be found at $INSTALL_DIR"

echo "The following dependencies will be installed:"
echo "   InfluxDB v1.2.0"
echo "   Grafana v4.1.2"

echo ""

# Ask for dependency installation
while true; do
    read -p "Install influxdb? (y/n):  " yn
    case $yn in
        [Yy]* )
            echo "[i] Downloading influxdb..."
            wget https://dl.influxdata.com/influxdb/releases/influxdb_1.2.0_amd64.deb
            echo " [i] done."

            echo "[i] Installing influxdb..."
            dpkg -i influxdb_1.2.0_amd64.deb
            echo "[i] done."
            break;;
        [Nn]* ) echo "[i] Skipping influxdb."; break;;
        * ) echo "[i] Answer was not understood.";;
    esac
done

echo ""

while true; do
    read -p "Install grafana? (y/n):  " yn
    case $yn in
        [Yy]* )
            echo "[i] Downloading grafana..."
            wget https://grafanarel.s3.amazonaws.com/builds/grafana_4.1.2-1486989747_amd64.deb
            echo "[i] done."

            echo "[i] Installing grafana..."
            dpkg -i grafana_4.1.2-1486989747_amd64.deb
            echo "[i] done."
            break;;
        [Nn]* ) echo "Skipping grafana."; break;;
        * ) echo "Answer was not understood.";;
    esac
done

