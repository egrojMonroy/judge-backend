#!/bin/bash
#
# PostGIS Import - run all import steps.
#

set -e

# Steps live in the same directory as this script.
STEPS=$(dirname $(realpath $0))

# Limit task memory usage to 4GB (better to fail than be slowed by heavy swapping).
ulimit -v8000000
n=0
#Arguments
# firt name of file
# second name of class
# third name of input
# fourth name of output 
cd $5
javac -J-Xms32m -J-Xmx256m $1.java  && time java $2 < $3.in > $4.out 

if ! diff -bwB /home/jorge/src/judge-backend/utils/solution$2.out $4.out &>/dev/null; then
  echo "Wrong answer"
else 
  echo "Accepted"
fi
