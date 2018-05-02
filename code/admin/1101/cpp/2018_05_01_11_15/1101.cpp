#include<iostream>
#include<cmath>
using namespace std;
main ()
{
    int x,i,j;
    int a,b;
    int m[5][5];
    for(i=0;i<5;i++)
    {
        for(j=0;j<5;j++)
        {
            cin>>x;
            m[i][j]=x;
        }
    }
     for(i=0;i<5;i++)
    {
        for(j=0;j<5;j++)
        {
         if(m[i][j]==1)
         {
             a=i;b=j;
             break;
         }
        }
    }
    x=abs(2)+abs(2-b);
    cout<<x<<endl;


}