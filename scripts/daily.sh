#!/bin/sh

# Daily wallpaper
DIR="$(dirname "$(readlink -f "${0}")")"
curl -sLo  "$DIR/daily.jpg" $(curl -sL "https://bingwallpaper.anerg.com/$(curl -sL "https://bingwallpaper.anerg.com" | grep -Eo "/detail/us[^\"]+" | head -n 1)" | grep -Eo "https://imgproxy.*w:1920[^\"]+" | tail -n 1)
