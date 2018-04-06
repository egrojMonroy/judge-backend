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
# firt argument name of file , second name of class 

javac -J-Xms32m -J-Xmx256m ~/Desktop/$1  && time java $2 < in1.in > out1.out

if ! diff -bwB output.out out1.out &>/dev/null; then
  >&2 echo "different"
fi
