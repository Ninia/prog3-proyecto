#!/bin/bash
LOG="/usr/local/proyectopbd/tmp/cpu-usage.log"
grep 'cpu ' /proc/stat | awk '{usage=($2+$4)*100/($2+$4+$5)} END {print usage}' > ${LOG} && cat ${LOG}