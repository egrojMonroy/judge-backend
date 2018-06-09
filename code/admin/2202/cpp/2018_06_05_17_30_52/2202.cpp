#include <bits/stdc++.h>

using namespace std;

typedef long long ll;


int main() {
    long a, b;
while(    cin>>a>>b ) {
	if(a == 0 && b == 0) return 0; 
	if(b%2!=0) {cout<<"-1"<<endl; continue;}
	int vacas = b/2 - a;
	int gallinas = a - vacas;
	if(vacas<0 || gallinas<0) {
		cout<<"-1"<<endl;
		continue;
	}
	cout<<gallinas<<" "<<vacas<<endl;
}
return 0;
}