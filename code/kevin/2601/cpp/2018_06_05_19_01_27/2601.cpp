#include <iostream>
using namespace std;
int main ()
{
    int w,x;
    cin>>w;
    if (w>=1||w<=100){
            x=w/2;
        if ((w%2==0)&&(x%2==0)){
            cout<<"YES";}
        else{
            cout<<"NO";}}
    return 0;
}
