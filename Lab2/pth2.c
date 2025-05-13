#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <math.h>
#include <time.h>

#define ITERATIONS 1e8

int counter = 0;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

void *heavy_task(void *arg) {
    int thread_num = *((int*)arg);
    double dummy = 0.0;

    // Синхронизированный вывод
    pthread_mutex_lock(&mutex);
    printf("\tThread #%d started\n", thread_num);
    pthread_mutex_unlock(&mutex);

    // Критическая секция
    pthread_mutex_lock(&mutex);
    counter++;
    pthread_mutex_unlock(&mutex);

    // Длительная операция
    for (int i = 0; i < ITERATIONS; i++) {
        dummy += sqrt(i);
    }

    // Синхронизированный вывод
    pthread_mutex_lock(&mutex);
    printf("\tThread #%d finished (dummy=%.2f)\n", thread_num, dummy);
    pthread_mutex_unlock(&mutex);

    free(arg);
    return NULL;
}

void pthreads(int threads_num) {
    pthread_t threads[threads_num];
    struct timespec start, end;

    clock_gettime(CLOCK_MONOTONIC, &start);

    for (int i = 0; i < threads_num; i++) {
        int *thread_num = malloc(sizeof(int));
        *thread_num = i;

        if (pthread_create(&threads[i], NULL, heavy_task, thread_num)) {
            perror("pthread_create failed");
            exit(EXIT_FAILURE);
        }
    }

    for (int i = 0; i < threads_num; i++) {
        pthread_join(threads[i], NULL);
    }

    clock_gettime(CLOCK_MONOTONIC, &end);
    double time_spent = (end.tv_sec - start.tv_sec) +
                       (end.tv_nsec - start.tv_nsec) / 1e9;

    printf("\nTotal counter: %d\n", counter);
    printf("Execution time: %.2f seconds\n", time_spent);
}

int main(int argc, char** argv) {
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <number_of_threads>\n", argv[0]);
        return EXIT_FAILURE;
    }

    int threads_num = atoi(argv[1]);
    if (threads_num <= 0) {
        fprintf(stderr, "Invalid number of threads\n");
        return EXIT_FAILURE;
    }

    pthreads(threads_num);
    pthread_mutex_destroy(&mutex);
    return EXIT_SUCCESS;
}