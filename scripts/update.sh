#!/bin/sh

DIR="$(dirname "$(readlink -f "${0}")")"
"$DIR/disk.sh" / "$DIR/svg.js" BAT0
"$DIR/../SVGWall-"*".AppImage" -j="$DIR/svg.js"
BRIGHT=$("$DIR/brightness.sh")
MEM=$("$DIR/memory.sh")
BATT=$("$DIR/battery.sh" BAT0)

TRIM="$BRIGHT $MEM $BATT"
TRIM="${TRIM#"${TRIM%%[![:space:]]*}"}"
TRIM="${TRIM%"${TRIM##*[![:space:]]}"}"

echo " $TRIM "

exit 0
