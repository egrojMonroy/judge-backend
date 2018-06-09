#include<iostream>
using namespace std;
int main()
{
    int n,m,a,b,total;
    cin>>n;
    while(n<1||n>1000)
        cin>>n;
    cin>>m;
    while(m<1||m>1000)
        cin>>m;
    cin>>a;
    while(a<1||a>1000)
        cin>>a;
    cin>>b;
    while(b<1||b>1000)
        cin>>b;
    total=(n-m)*a+b;
    cout<<total;
return 0;
}