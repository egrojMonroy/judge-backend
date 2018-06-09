#include <iostream>
#include <stdlib.h>
using namespace std;
int main(){
    string xemo;
    int x,a,b,maxa,maxb;
    cin>>x;
    while(x--){
        a=0;b=0;maxa=0;maxb=0;
        cin>>xemo;
        for(int i=0;i<xemo.size();++i){
            if(xemo[i]=='L' || xemo[i]=='?'){a++;}
            if(xemo[i]=='R'){a--;}
            maxa=max(maxa,abs(a));
        }
        for(int i=0;i<xemo.size();++i){
            if(xemo[i]=='R' || xemo[i]=='?'){b++;}
            if(xemo[i]=='L'){b--;}
            maxb=max(maxb,abs(b));
        }
        cout<<max(maxa,maxb)<<endl;
    }
    return 0;
}