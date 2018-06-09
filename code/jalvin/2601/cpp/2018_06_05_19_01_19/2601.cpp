#include<iostream>
using namespace std;
int main()
{
    int w;
    cin>>w;
    while(w<1||w>100)
        cin>>w;
    if(w%2==0&&w!=2)
        cout<<"YES";
    else
        cout<<"NO";
return 0;
}
