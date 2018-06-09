#include <iostream>
#include <cstdio>
#include <string>
#include <cmath>

using namespace std;
 
int main()
{	
    int cases;
	cin>>cases;
    while(cases--){
        string s;
        cin>>s;
        int max1=0,max2=0,di=0;
        for(int i=0;i<s.size();i++){
            if(s[i]=='L'){max1--;max2--;}
            else if(s[i]=='R'){max1++;max2++;}
            else if(s[i]=='?'){max1++;max2--;}
            di=max(di,abs(max1));
            di=max(di,abs(max2));
            //cout<<di<<endl;
        }
        printf("%d\n",di);
    }
    return 0;
}