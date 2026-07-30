#!/bin/sh

DIR="$(dirname "$(readlink -f "${0}")")"
"$DIR/disk.sh" / "$DIR/svg.js" sbs-6-000b
"$DIR/../SVGWall-aarch64.AppImage" -j="$DIR/svg.js"
BRIGHT=$("$DIR/brightness.sh")
MEM=$("$DIR/memory.sh")
BATT=$("$DIR/battery.sh" "sbs-6-000b")

echo "$BRIGHT$MEM$BATT"
