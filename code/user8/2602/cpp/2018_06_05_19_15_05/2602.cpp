#include <iostream>
using namespace std;
int main ()
{
    int n,m,a,l,ta,losas;
    cin>>n;
    cin>>m;
    cin>>a;
    l=n*m;
    ta=a*a;
    losas=l%ta;
    cout<<losas<<endl;
return 0;
}
