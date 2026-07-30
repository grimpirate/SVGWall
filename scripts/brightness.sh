#!/bin/sh

DIR="$(dirname "$(readlink -f "${0}")")"

d="/sys/class/backlight/*"
for path in $d; do
	if [ -d "$path" ]; then
		curr=$(cat "$path/actual_brightness")
		max=$(cat "$path/max_brightness")
		percent=$(( curr * 100 / max ))
		echo "  ${percent}%"
		exit 0
	fi
done
exit 1
