// sequential.c
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>

#define ITERATIONS 1e8

void heavy_task() {
    double dummy = 0.0;
    for (int i = 0; i < ITERATIONS; i++) {
        dummy += sqrt(i);
    }                   
}

int main(int argc, char** argv) {
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <number_of_operations>\n", argv[0]);
        return EXIT_FAILURE;
    }

    int ops = atoi(argv[1]);
    struct timespec start, end;

    clock_gettime(CLOCK_MONOTONIC, &start);
    
    for (int i = 0; i < ops; i++) {
        heavy_task();
    }

    clock_gettime(CLOCK_MONOTONIC, &end);
    double time_spent = (end.tv_sec - start.tv_sec) +
                       (end.tv_nsec - start.tv_nsec) / 1e9;

    printf("Sequential execution time (%d ops): %.2f seconds\n", ops, time_spent);
    return EXIT_SUCCESS;
}