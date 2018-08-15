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


# 1 File nam  without extention 
# 2 ' 4 '
# 3 { 2 }  
# 5 output
# 6 { 5 } 

# Steps live in the same directory as this script.
STEPS=$(dirname $(realpath $0))

# Limit task memory usage to 4GB (better to fail than be slowed by heavy swapping).
ulimit   -v80000000

    #echo "running steps from $STEPS"
    #echo

n=0

cd $5


if ! g++ $1 -o $4 -fno-asm -O2 -Wall -lm --static -std=c++11 -DONLINE_JUDGE; then 
echo "Compilation Error";
exit 1;
fi  

timeout 10s time -p ./$4 < $2 > testTime.txt