#include <iostream>
using namespace std;
int main()
{
    int w,k;
    cin>>w;
    if (w>=1 && w<=100)
    {
        k=w%2;
        if (k==0)
        {
            cout<<"YES"<<endl;
        }
        else
        {
            cout<<"NO"<<endl;
        }
    }
return 0;
}
