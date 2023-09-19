#include<iostream>
#include<time.h>
using namespace std;


int main() {
	double Dvomass[8][5];                       //1 масив
	srand(time(NULL));
	for (int i = 0; i < 8; i++)                  //Заповнення 1 масиву
		for (int j = 0; j < 5; j++)
			Dvomass[i][j] = rand() % 5 + 1;

	for (int i = 0; i < 8; i++) {               //Виведення 1 масиву
		for (int j = 0; j < 5; j++)
			cout << Dvomass[i][j] << "\t";
		cout << endl;
	}

	double mass[8];                             //Ініціалізація 2 масиву одиничками
	for (int i = 0; i < 8; i++)
		mass[i] = 1;

	for (int i = 0; i < 8; i++) {                //Ініціалізація добутком двомасиву
		for (int j = 0; j < 5; j++) {
			mass[i] = mass[i] * Dvomass[i][j];
		}
	}
	cout << "massiv :" << endl;
	for (int i = 0; i < 8; i++)                  //Виведення 2 масиву
		cout << mass[i] << "\t";
	cout << endl;

	cout << "sort massiv :" << endl ;

	//Сортировка методом бульбашки
		for (int i = 0; i < 8; i++)
		{
			for (int j = 8 - 1; j > i; j--)
				if (mass[j] < mass[j - 1])
				{
					int tmp = mass[j];             //tmp - доп змінна
					mass[j] = mass[j - 1];      //Заміна числа на попереднє
					mass[j - 1] = tmp;
				}
			cout << mass[i] << "\t";          //виведення
		}
}