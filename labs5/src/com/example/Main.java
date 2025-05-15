package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static final int THREADS = 4;
    public static final int COUNT = 2;
    public static MySemaphore mySemaphore = new MySemaphore(COUNT);
    public static Semaphore regularSemaphore = new Semaphore(COUNT);

    public static void main(String[] args) {
        System.out.println("-------------------\nRegular semaphore:\n-------------------");
        runTask(regularSemaphore);
        System.out.println("--------------\nMy semaphore:\n--------------");
        runTaskForMySemaphore(mySemaphore); // Используем отдельный метод для MySemaphore
    }

    // Для стандартного Semaphore
    private static void runTask(Semaphore semaphore) {
        ExecutorService es = Executors.newFixedThreadPool(THREADS);
        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < THREADS; i++) {
            tasks.add(() -> {
                String threadName = Thread.currentThread().getName();
                try {
                    semaphore.acquire();
                    System.out.println(threadName + " acquired. Available permits: " + semaphore.availablePermits());
                    Thread.sleep(2000);
                } finally {
                    semaphore.release();
                    System.out.println(threadName + " released. Available permits: " + semaphore.availablePermits());
                }
                return "Done";
            });
        }
        try {
            es.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        es.shutdown();
    }

    // Для MySemaphore
    private static void runTaskForMySemaphore(MySemaphore semaphore) {
        ExecutorService es = Executors.newFixedThreadPool(THREADS);
        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < THREADS; i++) {
            tasks.add(() -> {
                String threadName = Thread.currentThread().getName();
                try {
                    semaphore.acquire();
                    System.out.println(threadName + " acquired. Available permits: " + semaphore.availablePermits());
                    Thread.sleep(2000);
                } finally {
                    semaphore.release();
                    System.out.println(threadName + " released. Available permits: " + semaphore.availablePermits());
                }
                return "Done";
            });
        }
        try {
            es.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        es.shutdown();
    }
}

class MySemaphore {
    private int permits;

    public MySemaphore(int permits) {
        this.permits = permits;
    }

    public synchronized void acquire() throws InterruptedException {
        while (permits <= 0) wait();
        permits--;
    }

    public synchronized void release() {
        permits++;
        notifyAll();
    }

    public synchronized int availablePermits() {
        return permits;
    }
}