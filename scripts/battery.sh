#!/bin/sh

DIR="$(dirname "$(readlink -f "${0}")")"

STATUS_PATH="/sys/class/power_supply/$1/status"
STATUS=$(if [ -f "$STATUS_PATH" ]; then cat "$STATUS_PATH"; else echo "Unknown"; fi)

case "$STATUS" in
	"Charging")
	echo "󱟠"
	;;
	"Discharging")
	echo "󱟞"
	;;
	"Full")
	echo "󱟢"
	;;
	"Not charging")
	echo "󱟨"
	;;
	*)
	echo "󰂑"
	;;
esac
exit 0
