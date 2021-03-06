#!/bin/bash
#
# PostGIS Import - run all import steps.
#

set -e

#1Dir code 
#2Dir input
#3File Name of code 
#4compilationName
#5Directory

# Steps live in the same directory as this script.
STEPS=$(dirname $(realpath $0))

# Limit task memory usage to 4GB (better to fail than be slowed by heavy swapping).
ulimit   -v80000000

    #echo "running steps from $STEPS"
    #echo

n=0

cd $5

if ! javac -J-Xms32m -J-Xmx256m $1; then 
echo "Compilation Error";
exit 1;
fi  

timeout 10s time -p java $4 < $2 > testTime.txt || ( [ $? -eq 124 ] && echo TLE && exit 1)
 