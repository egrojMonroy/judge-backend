#!/bin/bash
#
# PostGIS Import - run all import steps.
#

set -e

# Steps live in the same directory as this script.
STEPS=$(dirname $(realpath $0))

# Limit task memory usage to 4GB (better to fail than be slowed by heavy swapping).
ulimit   -v800000

    #echo "running steps from $STEPS"
    #echo

n=0
cd $6

# 1 File nam  without extention 
# 2 Compile name without extention
# 3 in name 
# 4 out name 
# 5 output
# 6 filefolder of code and output 
g++ $1.cpp -o $2 -fno-asm -O2 -Wall -lm --static -std=c++11 -DONLINE_JUDGE && time ./$2 < $3 > $4.out 

if ! diff -bwB $5 $4.out &>/dev/null; then
  echo "Wrong answer"
else 
  echo "Accepted"
fi
