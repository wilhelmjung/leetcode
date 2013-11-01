#include <iostream>

using namespace std;

//XXX negative int?
int inline my_div_ceil(int d, int q)
{
	int s = d/q, r = d%q;
	return (r ? s+1 : s);
}

int inline min(int a, int b)
{
	return (a < b ? a : b);
}

int inline max(int a, int b)
{
	return (a > b ? a : b);
}

// Invariant: B[j] <= A[i] <= B[j+1]
double median_search(int a[], int l, int b[], int m, int left, int right)
{
	int n = l + m;

	cout << "left=" << left << ", right=" << right << endl;

	if (left > right) // median is not found in A[1..l], swap A with B.
		return median_search(b, m, a, l,
				max(1, my_div_ceil(n, 2) - l),
				min(m, my_div_ceil(n, 2)));
	int i = (left + right)/2; // floor
	int j = my_div_ceil(n, 2) - i;

	cout << "i=" << i << ", j=" << j << endl;

	if ((j == 0 || a[i] >= b[j]) && (j == m || a[i] <= b[j+1])) {
		if (n%2 == 1) { // only one median
			cout << "m:" << a[i] << endl;
			return (double)a[i];
		} else { // two medians, get successor of the smaller median a[i]
			int m2;
			if (i+1 <= l && j+1 <= m)
				m2 = min(a[i+1], b[j+1]);
			else {
				if (i+1 > l) // not in A
					m2 = b[j+1];
				if (j+1 > m) // not in B
					m2 = a[i+1];
			}
			cout << "m1:" << a[i] << ", m2:" << m2 << endl;
			return ((double)a[i]+(double)m2)/2.0;
		}
	} else if ((j == 0 || a[i] >= b[j]) && j != m && a[i] > b[j+1])
		return median_search(a, l, b, m, left, i-1);
	else
		return median_search(a, l, b, m, i+1, right);
}

double median_two_array(int a[], int l, int b[], int m)
{
	//debug
	cout << "a: ";
	for (int i = 0; i < l; i++)
		cout << a[i] << ", ";
	cout << "\b\b " << endl;
	cout << "b: ";
	for (int i = 0; i < m; i++)
		cout << b[i] << ", ";
	cout << "\b\b " << endl;

	int left, right, n = l + m;
	if (n == 0)
		return 0; //XXX ERROR

	// assume median is in A[left..right].
	// h = CEIL(n/2)
	// if l >  m left=h-m, right=h
	// if l <= m left=1,   right=l
	left = max(1, my_div_ceil(n, 2) - m);
	right = min(l,my_div_ceil(n, 2));
	// index shift(fix)
	return median_search(a-1, l, b-1, m, left, right);
}

int main()
{
	// 0,1,2,3,4,5,6,7,8,9
	//int a[] = {1,3,5,7}, b[] = {2,4,9,11};
	int a[] = {1,3}, b[] = {2,4};
	double res = median_two_array(a,2,b,2);

	cout << "median: " << res << endl;

	return 0;
}
