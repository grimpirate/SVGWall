#!/bin/sh

DIR="$(dirname "$(readlink -f "${0}")")"
"$DIR/disk.sh" / "$DIR/svg.js" BAT0
"$DIR/../SVGWall-x86_64.AppImage" -j="$DIR/svg.js"
