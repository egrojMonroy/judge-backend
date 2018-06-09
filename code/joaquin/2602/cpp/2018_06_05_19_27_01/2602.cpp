#include<iostream>
using namespace std;
int main ()
{
    int n,m,a,ca=0,ar,cl,arl;
    cin>>n;
    cin>>m;
    cin>>a;
    ar=m*n;
    arl=a*a;
    while (ar>0)
    {
        ar=ar-arl;
        ca=ca+1;
    }
    cout<<ca+1;
    return 0;
}