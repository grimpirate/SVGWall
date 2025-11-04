#!/bin/sh

# Storage percentage
sed -Ei "s/const used[^;]+/const used = '$(df -h --output=used $1 | tail -n 1 | tr -d '[:space:]')'/g" $2
sed -Ei "s/const available[^;]+/const available = '$(df -h --output=avail $1 | tail -n 1 | tr -d '[:space:]')'/g" $2
sed -Ei "s/const percent[^;]+/const percent = $(df --output=pcent $1 | tail -n 1 | tr -dc '[:digit:]')\/100\.0/g" $2
# Battery percentage
sed -Ei "s/const battery[^;]+/const battery = $(cat /sys/class/power_supply/BAT1/capacity)/g" $2
