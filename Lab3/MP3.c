#include <stdlib.h>
#include <omp.h>
#include <math.h>
#include <stdio.h>

void heavy_task() {
    volatile double dummy = 0.0; // volatile предотвращает оптимизацию
    int limit = 1e8;
    printf("thread_num %d \n", omp_get_thread_num());
    for (int i = 0; i < limit; i++) {
        dummy += sqrt(i);
    }
}

void openmp(int thread_num) {
    // Настройка количества потоков
    omp_set_dynamic(0);
    omp_set_num_threads(thread_num);
    
    printf("Using %d OpenMP threads\n", omp_get_max_threads());

    // Параллельный запуск задач
    #pragma omp parallel for
    for (int i = 0; i < thread_num; i++) {
        heavy_task();
    }
}

int main(int argc, char** argv) {
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <threads>\n", argv[0]);
        return 1;
    }
    int threads = atoi(argv[1]);
    
    double start = omp_get_wtime();
    openmp(threads);
    double end = omp_get_wtime();
    
    printf("OpenMP time: %.2f sec\n", end - start);
    return 0;
}