#include <stdio.h>
#include <stdlib.h>
#include <time.h> 

void seq(float a[], float b[], float c[]) {
    for (int i = 0; i < 4; i++) {
        c[i] = a[i] * b[i];
    }
    // Раскомментируйте для проверки результата:
    // for (int i = 0; i < 4; i++) {
    //     printf("%f ", c[i]);
    // }
    // printf("\n");
}

int main(int argc, char** argv) {
    if (argc != 2) {
        printf("Неправильное количество аргументов :(\n"); 
        return 1;
    }
    
    int iterations_num = atoi(argv[1]); // Исправлено на atoi
    if (iterations_num <= 0) {
        printf("Введите положительное целое число аргументов.\n");
        return 1;
    }
    
    // Исправлено: размер массивов явно указан как 4
    float a[4] = {1.0, 2.0, 3.0, 4.0};
    float b[4] = {5.0, 6.0, 7.0, 8.0};
    float c[4];
    
    clock_t start = clock();

    for (int i = 0; i < iterations_num; i++) {
        seq(a, b, c);
    }
    
    clock_t end = clock();  // Конец замера времени
    double time_spent = (double)(end - start) / CLOCKS_PER_SEC;

    printf("\n--------------------------------\n");
    printf("Общее время: %.6f секунд\n", time_spent);
    printf("Среднее время итерации: %.6f мс\n", (time_spent * 1000) / iterations_num);

    return 0;
}