#include <iostream>
using namespace std;
int main()
{
    int w, c;
    cin>>w;
    for (int i=1;i<=w;i++)
    {
        if (w%i==0)
            c++;
    }
    if(c==2)
        cout<<"NO";
    else
        cout<<"YES";
}