#!/bin/bash
#
# PostGIS Import - run all import steps.
#

set -e

# Steps live in the same directory as this script.
STEPS=$(dirname $(realpath $0))

# Limit task memory usage to 4GB (better to fail than be slowed by heavy swapping).
ulimit -v800000

    echo "running steps from $STEPS"
    echo

n=0
# firt argument name of file , second name of user 

g++ ~/Desktop/$1 -o comp$2 -fno-asm -O2 -Wall -lm --static -std=c++11 -DONLINE_JUDGE && time ./comp$2 < ~/Desktop/in1.in > out1.out 

if ! diff -bwB output.out out1.out &>/dev/null; then
  >&2 echo "different"
fi
