#include <iostream>
using namespace std;
int main(){
    long long n,m,a,sol=0,aux;
    cin>>n>>m>>a;
    aux=m/a;
    if(m%a){
        aux++;
    }
    sol+=n/a;
    if(n%a){
        sol++;
    }
    cout<<sol*aux<<endl;
    return 0;
}