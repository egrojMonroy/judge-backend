#include <bits/stdc++.h>
using namespace std;
long long limits[] = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};
long long v[] = {9, 90, 900, 9000,90000, 900000,9000000,90000000,900000000, 9000000000};
long long b[] = {1, 2,  3,   4,   5,     6,     7,      8,       9,         10};
int main() {
  long long n;
  cin >> n;
  int digits = log10(n);
  string s = "";
  /*
  for (long long i = 1; i <= n; i++) {
    stringstream ss;
    ss << i;
    string k;
    ss >> k;
    s += k;
    s += ",";
  }*/
  long long ans = 0LL;
  for (long i = 0; i < digits; i++) {
    ans += (v[i] * b[i]);
  }
  // cout << s.size() - 1 << endl;
  const long long cosa = (n - limits[digits] + 1) * b[digits];
  cout << ans + cosa + n - 1 << endl;
}