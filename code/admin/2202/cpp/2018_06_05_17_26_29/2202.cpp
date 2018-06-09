#include <bits/stdc++.h>

using namespace std;

typedef long long ll;


int main() {
    long a, b;
    cin>>a>>b; 
	if(b%2!=0) {cout<<"-1"<<endl; return 0;}
	int vacas = b/2 - a;
	int gallinas = a - vacas;
	if(vacas<0 || gallinas<0) {
		cout<<"-1">>endl;
		return 0;
	}
	cout<<vacas<<" "<<gallinas<<endl;
}