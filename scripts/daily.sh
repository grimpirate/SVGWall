#!/bin/sh

# Daily wallpaper
DIR="$(dirname "$(readlink -f "${0}")")"
curl -sLo  "$DIR/daily.jpg" "https://www.bing.com$(sed 's/"//g' <<< "$(curl -s "https://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=en-US" | jq .images[0].url)")"
#curl -sLo  "$DIR/daily.jpg" $(curl -sL "https://bingwallpaper.anerg.com/$(curl -sL "https://bingwallpaper.anerg.com" | grep -Eo "/detail/us[^\"]+" | head -n 1)" | grep -Eo "https://imgproxy.*w:1920[^\"]+" | tail -n 1)
