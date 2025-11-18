#!/bin/sh

DIR="$(dirname "$(readlink -f "${0}")")"
"$DIR/disk.sh" / "$DIR/svg.js" sbs-6-000b
"$DIR/../SVGWall-aarch64.AppImage" -j="$DIR/svg.js"
