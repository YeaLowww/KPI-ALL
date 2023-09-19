#include<iostream>                                 
#include<cmath>

using namespace std;

int main() {
	setlocale(LC_ALL, "ukr");
	float x;
	float y;
	int r = 1;

	cout << "Введiть координату Х: ";
	cin >> x;
	cout << "Введiть координату Y: ";
	cin >> y;

	double h1 = (pow(x, 2) + pow(y, 2));
	double hypotenuse = pow(h1, 0.5);

	if (x >= 0 && y >= 0 && y >= x && hypotenuse <= r)
		cout << "Точка належить площинi ";
	else if (x >= 0 && y <= 0 && y <= x && hypotenuse <= r)
		cout << "Точка належить площинi ";
	else if (x <= 0 && y >= 0 && y <= -x && hypotenuse <= r)
		cout << "Точка належить площинi ";
	else if (x <= 0 && y <= 0 && y <= x && hypotenuse <= r)
		cout << "Точка належить площинi ";
	else
		cout << "Точка НЕ належить площинi ";







}