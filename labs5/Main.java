import java.util.concurrent.Semaphore;

public class Main {
    private static final int PERMITS = 3; // Максимум 3 потока одновременно
    private static final Semaphore semaphore = new Semaphore(PERMITS);
    private static final int THREADS = 5; // Всего потоков

    public static void main(String[] args) {
        for (int i = 1; i <= THREADS; i++) {
            new Thread(new Worker(i)).start();
        }
    }

    static class Worker implements Runnable {
        private final int id;

        Worker(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                System.out.println("Поток " + id + " ожидает разрешения...");
                semaphore.acquire(); // Запрос разрешения
                System.out.println("Поток " + id + " получил разрешение. Доступно: " 
                                 + semaphore.availablePermits());

                // Имитация работы с ресурсом (4 секунды)
                Thread.sleep(4000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release(); // Освобождение разрешения
                System.out.println("Поток " + id + " освободил разрешение. Доступно: " 
                                 + semaphore.availablePermits());
            }
        }
    }
}