#include <iostream>
using namespace std;
int main(){
    int n,m,a,sol=0;
    cin>>n>>m>>a;
    while(m>0){
        m-=a;
        sol+=n/a;
        if(n%a){
            sol++;
        }
    }
    cout<<sol<<endl;
    return 0;
}
