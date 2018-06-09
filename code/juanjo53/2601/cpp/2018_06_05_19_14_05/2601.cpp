#include <iostream>
#include <cstdlib>
using namespace std;
int main (){
    int w;
    cin>>w;
    while (w<=100){
        cin>>w;
        if (w%2==0){
            cout<<"YES"<<endl;
            break;
        }else{
            cout<<"NO"<<endl;
            break;
        }
    }
    return 0;
}
