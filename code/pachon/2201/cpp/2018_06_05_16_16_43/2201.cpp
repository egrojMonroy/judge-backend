#include <bits/stdc++.h>

using namespace std;

typedef long long ll;

int main() {
    ll n;
    cin >> n;
    ll ans = 0;
    ll pow10 = 1;
    ll e = 1;
    while(pow10 * 10 <= n) {
        ans += e * pow10 * 9;
        pow10 *= 10;
        e++;
    }
    if(n < 10) {
        ans = n;
    } else {
        ll q = n / pow10;
        ll r = n - q * pow10 + 1;
        ans += r * e;
    }
    cout << ans + n - 1 << endl;
}