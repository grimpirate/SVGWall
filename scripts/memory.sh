#!/bin/sh

DIR="$(dirname "$(readlink -f "${0}")")"

MEM=$(free | grep Mem | awk '{print int($3/$2 * 100.0)}')
echo "  ${MEM}%"
exit 0
