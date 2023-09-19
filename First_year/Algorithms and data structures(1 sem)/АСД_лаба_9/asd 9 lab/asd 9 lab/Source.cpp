#include<iostream>
#include<time.h>
using namespace std;

int main() {
	const int n = 7;
	double Dvomass[n][n];  
	int x=0;
	int jf;
	srand(time(NULL));
	for (int i = 0; i < n; i++)                  
		for (int j = 0; j < n; j++)
			Dvomass[i][j] = rand() % 11 -5;

	for (int i = 0; i < n; i++) {              
		for (int j = 0; j < n; j++)
			cout << Dvomass[i][j] << "\t";
		cout << endl;}
	cout << "----------------------------------------------------------" << endl;
	for (int i = 0; i < n; i++){
		for (int j = 0; j < n; j++){
			if (Dvomass[i][j] < 0) {
				jf = j;
				x = Dvomass[i][j];}
		}
		int temp = Dvomass[i][i];
		Dvomass[i][i] = x;
		Dvomass[i][jf] = temp;
		cout << "row: " << i+1 << "  Column: " << jf+1 << "  Element_x: "<<x<<endl;}
	cout << "----------------------------------------------------------" << endl;
	for (int i = 0; i < n; i++) {
		for (int j = 0; j < n; j++)
			cout << Dvomass[i][j] << "\t";
		cout << endl;
	}
}