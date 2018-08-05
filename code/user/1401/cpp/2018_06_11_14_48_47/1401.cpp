#include<cstdio>
#include<iostream>
#include<cmath>
#include<cstring>
using namespace std;
#define sf scanf
#define pf printf

int min(int a, int b)
{
    if(a<=b )
        return a;
    else
        return b;
}

int main()
{
    int n, m, a, b;
    int ans, ans1, ans2;
    while(cin >> n >> m >> a >> b)
    {
        if((m*a)<=b)
        {
            ans  = a*n;
            cout << ans << endl;
        }
        else
        {
            ans1 = ((n/m)*b) + b;
            ans2 = ((n/m)*b) + (n%m)*a;
            ans = min(ans1, ans2);
            cout << ans << endl;
        }
    }
    return 0;
}