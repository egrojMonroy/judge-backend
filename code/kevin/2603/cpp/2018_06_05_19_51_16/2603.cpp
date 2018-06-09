#include <iostream>
#include <iomanip>
using namespace std;
int main ()
{
    int a,b,x;
    float t,n,m,z;
    cin>>n;
    while (n<1||n>1000){
        cin>>n;}
    cin>>m;
    while (m<1||m>1000){
        cin>>m;}
    cin>>a;
    while (a<1||a>1000){
        cin>>a;}
    cin>>b;
    while (b<1||b>1000){
        cin>>b;}
    t=n/m;
    x=n*a;
    z=t*b;
    if (z<x){
        cout<<setprecision(1)<<z;}
    else{
        cout<<x;}
    }