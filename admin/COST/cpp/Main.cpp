#include<bits/stdc++.h>
using namespace std;
/*
    *
    * Prosen Ghosh
    * American International University - Bangladesh (AIUB)
    *
*/


    int T,ar[3];
    cin >> T;
    for(int t = 1; t <= T; t++){

        cin >> ar[0] >> ar[1] >> ar[2];
        sort(ar,ar+3);
        cout << "Case " << t << ": " << ar[1] << endl;
    }
    return 0;
}
