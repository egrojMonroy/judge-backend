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
# fifth name of solution
# 6th place of everything
# 7th time limit 
cd $6
if ! javac -J-Xms32m -J-Xmx256m $1.java; then 
echo "Compilation Error";
exit 1;
fi  

timeout $7s java $2 < $3 > $4.out  || ( [ $? -eq 124 ] && echo timeout && exit 1)

if ! diff -bwB $5 $4.out &>/dev/null; then
  echo "Wrong answer"
else 
  echo "Accepted"
fi
