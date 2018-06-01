n=0
cd $6

# 1 File nam  without extention 
# 2 Compile name without extention
# 3 in name 
# 4 out name 
# 5 output
# 6 filefolder of code and output 
# 7 timelimit
if ! g++ $1.cpp -o $2 -O2 -Wall -lm --static -std=c++11 -DONLINE_JUDGE; then 
echo "Compilation Error";
exit 1;
fi  

(timeout $7s ./$2 < $3 > $4.out   || ( [ $? -eq 124 ] && echo timeout && exit 1) ) || (echo runtime && exit 1)



if ! diff -bwB $5 $4.out &>/dev/null; then
  echo "Wrong answer"
else 
  echo "Accepted"
fi
