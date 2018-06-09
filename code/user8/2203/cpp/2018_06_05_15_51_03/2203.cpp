#include<bits/stdc++.h>
using namespace std;
int main() {
int x; 
cin>>x; 
string s;
for(int i = 0 ; i < x ; i++) {
	cin>>s;
	int left = 0 ,right =0, inc = 0 ;
	for(int j = 0 ; j < s.length() ;++j){
		if( s[j] == 'R') {
		   right++;
		} else if (s[j] == 'L'){
		   left++;		
		} else {
			inc++;		
		}
	}
	int sol = 0; 
	sol = max(right,left) - min(right,left) + inc;
	cout<<sol<<endl;
}


return 0;

}