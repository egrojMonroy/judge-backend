# include <iostream>
using namespace std;
int main(){
    int w;
    cin>>w;
    while (0>=w || w>=100){
        cin>>w;
    }
    if (w%2==0 && w!=2)
        cout<<"YES"<<endl;
    else
        cout<<"NO"<<endl;
}