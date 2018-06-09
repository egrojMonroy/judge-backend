#include<iostream>
#include<stdlib.h>
#include<stdio.h>
#include<math.h>
using namespace std;
int main(){
int w,j;
cin>>w;
if ((w>=1)|(w<=100)){
        if (w%2!=0){
        cout<<"NO"<<endl;
        }else{
            j=w/2;
                if (j%2==0){
        cout<<"YES"<<endl;
      }else{
      cout<<"NO"<<endl;}
}}

return 0;}
