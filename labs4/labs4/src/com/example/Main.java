package com.example;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static final int THREADS = 3;
    public static final int ITERATIONS = 1000000;
    public static final double NSEC = 1_000_000_000.0;
    public static final int MAP_SIZE = 100;
    public static final int SAMPLES = 1;

    private static class Result {
        long time;
        int errors;

        Result(long time, int errors) {
            this.time = time;
            this.errors = errors;
        }
    }

    public static void main(String[] args) {
        System.out.println("Testing collections performance:");

        Result hashMapResult = testCollection(new HashMap<>());
        Result hashTableResult = testCollection(new Hashtable<>());
        Result syncMapResult = testCollection(Collections.synchronizedMap(new HashMap<>()));
        Result cHashMapResult = testCollection(new ConcurrentHashMap<>());

        System.out.println("\nResults (lower is better):");
        printResult("HashMap", hashMapResult);
        printResult("Hashtable", hashTableResult);
        printResult("SynchronizedMap", syncMapResult);
        printResult("ConcurrentHashMap", cHashMapResult);
    }

    private static void printResult(String name, Result result) {
        System.out.printf("%-18s Time: %.3f s | Errors: %d\n",
                name + ":", result.time / NSEC, result.errors);
    }

    private static Result testCollection(Map<String, Integer> map) {
        System.out.print("\nTesting " + map.getClass().getSimpleName() + "... ");

        long totalTime = 0;
        int totalErrors = 0;

        for (int sample = 0; sample < SAMPLES; sample++) {
            map.clear();
            ExecutorService executor = Executors.newFixedThreadPool(THREADS);
            List<Callable<Void>> tasks = new ArrayList<>();
            AtomicInteger errorCount = new AtomicInteger(0);

            for (int i = 0; i < THREADS; i++) {
                tasks.add(() -> {
                    try {
                        for (int j = 0; j < ITERATIONS; j++) {
                            String key = "key" + ThreadLocalRandom.current().nextInt(MAP_SIZE); //sss
                            map.merge(key, 1, Integer::sum);
                        }
                    } catch (Exception e) {
                        errorCount.incrementAndGet();
                    }
                    return null;
                });
            }

            long start = System.nanoTime();
            try {
                List<Future<Void>> futures = executor.invokeAll(tasks);
                for (Future<Void> future : futures) {
                    try {
                        future.get();
                    } catch (ExecutionException e) {
                        errorCount.incrementAndGet();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                executor.shutdown();
            }
            
            totalTime += System.nanoTime() - start;
            totalErrors += errorCount.get();
            Integer counter = 0;
            for (Integer val: map.values()){
                counter = counter + val;
            }
            System.out.print("\nTotal: " + counter);
        }
        return new Result(totalTime / SAMPLES, totalErrors / SAMPLES);
    }
}