#include<iostream>
#include<time.h>
using namespace std;


int main() {
	double Dvomass[8][5];                       //1 �����
	srand(time(NULL));
	for (int i = 0; i < 8; i++)                  //���������� 1 ������
		for (int j = 0; j < 5; j++)
			Dvomass[i][j] = rand() % 5 + 1;

	for (int i = 0; i < 8; i++) {               //��������� 1 ������
		for (int j = 0; j < 5; j++)
			cout << Dvomass[i][j] << "\t";
		cout << endl;
	}

	double mass[8];                             //����������� 2 ������ ����������
	for (int i = 0; i < 8; i++)
		mass[i] = 1;

	for (int i = 0; i < 8; i++) {                //����������� �������� ���������
		for (int j = 0; j < 5; j++) {
			mass[i] = mass[i] * Dvomass[i][j];
		}
	}
	cout << "massiv :" << endl;
	for (int i = 0; i < 8; i++)                  //��������� 2 ������
		cout << mass[i] << "\t";
	cout << endl;

	cout << "sort massiv :" << endl ;

	//���������� ������� ���������
		for (int i = 0; i < 8; i++)
		{
			for (int j = 8 - 1; j > i; j--)
				if (mass[j] < mass[j - 1])
				{
					int tmp = mass[j];             //tmp - ��� �����
					mass[j] = mass[j - 1];      //����� ����� �� ��������
					mass[j - 1] = tmp;
				}
			cout << mass[i] << "\t";          //���������
		}
}