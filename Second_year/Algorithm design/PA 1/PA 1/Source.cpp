#define _CRT_SECURE_NO_DEPRECATE
#include <stdio.h>
#include <time.h>
#include <windows.h>
#include <stdlib.h>
#include <stdbool.h>
int main(int argc, char* argv[])
{
    SetConsoleCP(1251);
    SetConsoleOutputCP(1251);
    srand(time(NULL));

    FILE* f_read = NULL;
    f_read = fopen("mass_1.txt", "rt+"); //Файл, який буде посортовано
    /*------------------------------------------------------початковий розподіл*/
    int count = 0;
    FILE* f_start[3] = { NULL };
    while (count < 3){
        char buff[6];
        sprintf(buff, "%d.txt", count + 1);//Створення файлів 123(форматує та записує ряд символів та значень у масиві виведення даних, на який посилається параметр 
        f_start[count] = fopen(buff, "at+");
        count++;
    }
    FILE* f_temp = NULL;//file-temp
    int temp;
    fscanf(f_read, "%d", &temp);
    fprintf(f_start[0], "%d", temp);
    /*--------------------------------------------початковий розподіл за файлами*/
    int flag = 1;
    while (!feof(f_read)){
        int tmp;
        if (fscanf(f_read, "%d", &tmp) == EOF) break;
        if (tmp >= temp){
            if (flag == 1)
                f_temp = f_start[0];
            else{
                if (flag == 2)
                    f_temp = f_start[1];
                else
                    if (flag == 3)
                        f_temp = f_start[2];
            }
        }
        else{
            if (flag == 1){
                flag = 2;
                f_temp = f_start[1];
            }
            else if (flag == 2){
                flag = 3;
                f_temp = f_start[2];
            }
            else if (flag == 3){
                flag = 1;
                f_temp = f_start[0];
            }
        }
        temp = tmp;
        fprintf(f_temp, " %d", temp);
    }
    count = 0;
    /*--------------основная часть алгоритма--------------------*/
    while (1){
        count = 0;
        while (count < 3)
            rewind(f_start[count++]);
        FILE* f_temp_[3] = { NULL };
        for (int i = 0; i < 3; i++){
            char buff[15];
            sprintf(buff, "temp_%d.txt", i + 1);
            f_temp_[i] = fopen(buff, "at+");
        }
        fpos_t pos[3];
        for (int i = 0; i < sizeof(pos) / sizeof(fpos_t); i++)
            fgetpos(f_start[i], &pos[i]);
        int in_file = 0;// указывает в какой файл осуществляется запись
        count = 0; // счётчик
        int end_of_file[3] = { 0 };
        while ((end_of_file[0] != 1 && end_of_file[1] != 1 && end_of_file[2] != 1)){
            if (in_file == 3)
                in_file = 0;
            int flag_abc[3] = { -1, -1, -1 };
            int temp_abc[3];
            while (1){
                bool exit = false;
                for (int i = 0; i < sizeof(flag_abc) / sizeof(int); i++)
                {
                    if (flag_abc[i])
                        break;
                    if (i == 2)
                        exit = true;
                }
                if (exit) break;
                //////////////////////////////////////////////
                for (int i = 0; i < 3; i++)
                    if (end_of_file[i] == 1) flag_abc[i] = 0;
                //////////////////////////////////////////////
                int a[3];
                for (int i = 0; i < sizeof(a) / sizeof(int); i++)
                    if (!end_of_file[i]) if (fscanf(f_start[i], "%d", &a[i]) == EOF) { flag_abc[i] = 0; end_of_file[i] = 1; }

                int first = 1, second = 2;
                for (int i = 0; i < 3; i++)
                {
                    if (!end_of_file[i]) 
                    {
                        if (i == 1) first = 0;
                        if (i == 2) second = 1;
                        if (flag_abc[i] == -1 || temp_abc[i] <= a[i])
                        {
                            if (((flag_abc[first] && flag_abc[second]) && (a[i] <= a[first] && a[i] <= a[second])) ||
                                ((!flag_abc[first] && flag_abc[second]) && a[i] <= a[second]) || ((flag_abc[first] && !flag_abc[second]) && a[i] <= a[first]))
                            {
                                if (flag_abc[first]) fsetpos(f_start[first], &pos[first]);
                                if (flag_abc[second]) fsetpos(f_start[second], &pos[second]);
                                fgetpos(f_start[i], &pos[i]);
                                temp_abc[i] = a[i];
                                flag_abc[i] = 1;
                                fprintf(f_temp_[in_file], " %d", a[i]);
                            }
                            else if (!flag_abc[first] && !flag_abc[second])
                            {
                                if (flag_abc[i])
                                {
                                    fprintf(f_temp_[in_file], " %d", a[i]);
                                    while (1)
                                    {
                                        temp_abc[i] = a[i];
                                        fgetpos(f_start[i], &pos[i]);
                                        if (fscanf(f_start[i], "%d", &a[i]) == EOF) { end_of_file[i] = 1; break; }
                                        if (a[i] >= temp_abc[i])
                                            fprintf(f_temp_[in_file], " %d", a[i]);
                                        else
                                        {
                                            fsetpos(f_start[i], &pos[i]);
                                            break;
                                        }
                                    }
                                    flag_abc[i] = 0;
                                }
                            }
                            else
                                fsetpos(f_start[i], &pos[i]);
                        }
                        else
                        {
                            flag_abc[i] = 0;
                            fsetpos(f_start[i], &pos[i]);
                        }
                    }
                }
            }
            in_file++;
        }
        count = 0;
        while (count < 3)
            fclose(f_start[count++]);
        remove("1.txt");
        remove("2.txt");
        remove("3.txt");
        count = 0;
        while (count < 3)
            fclose(f_temp_[count++]);
        rename("temp_1.txt", "1.txt");
        rename("temp_2.txt", "2.txt");
        rename("temp_3.txt", "3.txt");
        count = 0;
        while (count < 3)
        {
            char buff[6];
            sprintf(buff, "%d.txt", count + 1);
            f_start[count] = fopen(buff, "rt+");
            count++;
        }

        for (int i = 0; i < 3; i++)
            fseek(f_start[i], 0, SEEK_END);
        size_t size_1[3];
        for (int i = 0; i < 3; i++)
            size_1[i] = ftell(f_start[i]);
        if (size_1[1] == 0 && size_1[2] == 0) break;
    }
    rewind(f_read);
    rewind(f_start[0]);
    /*-----перезапись-----*/
    count = 0;
    while (!feof(f_start[0])){
        int temp;
        if (fscanf(f_start[0], "%d", &temp) == EOF) break;
        fprintf(f_read, " %d", temp);
        count++;
        if (count == 10)
        {
            fputc('\n', f_read);
            count = 0;
        }
    }
    for (int i = 0; i < 3; i++)
        fclose(f_start[i]);
    remove("1.txt");
    remove("2.txt");
    remove("3.txt");
    fclose(f_read);

    return 0;
}