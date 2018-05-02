#include<bits/stdc++.h>
using namespace std; 
int main() {
    long long n,m;
    cin>>n>>m;
    int v[n+1];
    for(int i = 0;i<n;++i){
        v[i]=0;
    }
    for(int i=0;i<m;++i) {
        int x; 
        cin>>x;
        x--; 
        v[x]++;
    }
    int mini = 999999;
    for(int i =0;i<n;++i) {
        mini = min(mini,v[i]);
    }
    cout<<mini<<endl;
    while(true) {
	mini++;	
	}
    
}