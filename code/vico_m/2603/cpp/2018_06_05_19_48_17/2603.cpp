#include<iostream>
#include<string.h>
#include<stdio.h>
#include<stdlib.h>
using namespace std;
int main()
{
    char g[200],*z,*c;
    int d,e,f,m,a,b,n,c1,c2,vnc,y=0;
    cin.getline(g,200);
    z=strtok(g," ");
    n=atoi(z);
    for(e=1;e<=3;e++){
        c=strtok(NULL," ");
        f=atoi(c);
        switch(e){
        case 1:
            m=f;
            while(n>m){
            n=n-m;
            y++;
            }
            break;
        case 2:
            a=f;
            break;
        case 3:
            b=f;
            break;
        }
    }
    c1=y*b;
    c2=n*a;
    cout<<c1+c2<<endl;
    return 0;
}
