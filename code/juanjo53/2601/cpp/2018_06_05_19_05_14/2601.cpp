#include <iostream>
using namespace std;
int main (){
    int w,i;
    cin>>w;
    for (i=1;i<=w;i++){
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
