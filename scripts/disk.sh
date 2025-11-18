#!/bin/sh

# Storage percentage
sed -Ei "s/const used[^;]+/const used = '$(df -h $1 | awk '{print $3}' | tail -n 1 | tr -d '[:space:]')'/g" $2
sed -Ei "s/const available[^;]+/const available = '$(df -h $1 | awk '{print $4}' | tail -n 1 | tr -d '[:space:]')'/g" $2
sed -Ei "s/const percent[^;]+/const percent = $(df $1 | awk '{print $5}' | tail -n 1 | tr -dc '[:digit:]')\/100\.0/g" $2
# Battery percentage
sed -Ei "s/const battery[^;]+/const battery = $(cat /sys/class/power_supply/$3/capacity)/g" $2
