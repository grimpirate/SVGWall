#!/bin/sh

# Daily wallpaper
DIR="$(dirname "$(readlink -f "${0}")")"
curl -sLo  "$DIR/daily.jpg" $(curl -sL "https://bingwallpaper.anerg.com/$(curl -sL "https://bingwallpaper.anerg.com" | grep -Eo "/detail[^\"]+" | head -n 1)" | grep -Eo "https://imgproxy[^\"]+" | tail -n 1)
