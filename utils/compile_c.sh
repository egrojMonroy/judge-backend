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
cd $6

# 1 File nam  without exception 
# 2 Compile name without exception
# 3 in name 
# 4 out name 
g++ $1.cpp -o $2 -fno-asm -O2 -Wall -lm --static -std=c++11 -DONLINE_JUDGE && time ./$2 < $3.in > $4.out 

if ! diff -bwB $5.out $4.out &>/dev/null; then
  echo "Wrong answer"
else 
  echo "Accepted"
fi
