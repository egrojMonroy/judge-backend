#include <bits/stdc++.h>

using namespace std;

int main() {
#ifdef _DEBUG
	freopen("input.txt", "r", stdin);
//	freopen("output.txt", "w", stdout);
#endif

	int n, k;
	scanf("%d %d", &n, &k);
	
	vector<int> a(n);
	vector<int> t(n);
	
	int overall = 0;
	
	for (int i = 0; i < n; ++i)
		scanf("%d", &a[i]);
	for (int i = 0; i < n; ++i)
		scanf("%d", &t[i]);
		
	vector<int> pr(n);
	for (int i = 0; i < n; ++i) {
		if (i) pr[i] += pr[i - 1];
		if (t[i] == 0) pr[i] += a[i];
		else overall += a[i];
	}
	
	int add = 0;
	for (int i = k - 1; i < n; ++i)
		add = max(add, pr[i] - (i >= k ? pr[i - k] : 0));
	
	printf("%d\n", overall + add);
	
	return 0;
}