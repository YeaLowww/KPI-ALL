#include<iostream>
using namespace std;

int main() {
	const int a = 10;
	double average = 0;
	char array1[a], array2[a], array3[a];
	int n = 0;
	double s = 0;
	cout << "first array: " << endl;
	for (int i = 0; i <a; i++)                   //1 array
	{
		array1[i] = (95 - 3*i);
		cout << (int)array1[i] <<"\t"<<array1[i] << endl;
	}
	cout<<"second array: "<<endl;
	for (int i = 0; i < a; i++)                       //2 array
	{
		array2[i] = (74 + 3*i);
		cout << (int)array2[i] << "\t" << array2[i] << endl;
	}
	cout << "third array: " << endl;
	for (int i = 0; i < a; i++)                   //3 array
	{
		for (int j = 0; j < a; j++)
		{
			if (array1[i] == array2[j])
				array3[i] = array1[i];
		}
		if (array3[i] != array1[i])
			array3[i] =  0;
		cout << (int)array3[i] << "\t" << array3[i] << endl;
	}
	for (int i = 0; i < a; i++)
	{
		if (((int)array3[i] < 82) && ((int)array3[i]>0))
		{
			s = s + (int)array3[i];
			n = n+1;
		}
	}
	average = s / n;

	cout <<"suma: " << s << endl;
	cout <<"amount: " << n << endl;
	cout <<"average: "<< average << endl;
}