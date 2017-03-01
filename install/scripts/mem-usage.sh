#!/bin/bash
# taken from https://stackoverflow.com/questions/10730066/bash-memory-usage#10744520
function mem_per_user {

    local user=$1
    pids=`ps aux | awk -v username=${user} '{if ($1 == username) {print $2}}'`

    local totalmem=0
    for pid in ${pids}
    do
        mem=`pmap ${pid} | tail -1 | \
            awk '{pos = match($2, /([0-9]*)K/, mem); if (pos > 0) print mem[1]}'`
        # when variable properly set
        if [ ! -z ${mem} ]
        then
            totalmem=$(( totalmem + $mem))
        fi
    done

    echo ${totalmem}
}

total_mem=0
for username in `ps aux | awk '{ print $1 }' | tail -n +2 | sort | uniq`
do
    per_user_memory=0
    per_user_memory=$(mem_per_user ${username})
    if [ "$per_user_memory" -gt 0 ]
    then
       total_mem=$(( ${total_mem} + ${per_user_memory}))

    fi
done
echo ${total_mem} # in kB