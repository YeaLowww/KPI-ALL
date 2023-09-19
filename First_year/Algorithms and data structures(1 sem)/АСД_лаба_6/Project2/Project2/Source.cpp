#include<iostream>
#include<cmath>
using namespace std;
int A(int, int);
int main() {
    int number;
    int n=0;
    cin >> number;
    cout << A(number, n);

}
int A(int num, int k = 0) {
    if (num > 0)
        k += num % 8 + 10 * A(num / 8, k);
    return k;
}