# include <iostream>
using namespace std;
int main(){
    int w;
    cin>>w;
    while (1>=w || w>=100){
        cin>>w;
    }
    if (w%2==0)
        cout<<"YES"<<endl;
    else
        cout<<"NO"<<endl;
}