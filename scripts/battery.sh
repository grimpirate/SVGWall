#!/bin/sh

DIR="$(dirname "$(readlink -f "${0}")")"

STATUS=$(cat "/sys/class/power_supply/$1/status")

case "$STATUS" in
	"Charging")
	echo " 󱟠"
	;;
	"Discharging")
	echo " 󱟞"
	;;
	"Full")
	echo " 󱟢"
	;;
	"Not charging")
	echo " 󱟨"
	;;
	*)
	echo " 󰂑"
	;;
esac
exit 0
