public class ssseef {

}
import java.util.concurrent.Semaphore;

public class CountingSemaphoreDemo {
    private static final int PERMITS = 3; // Максимум 3 потока одновременно
    private static final Semaphore semaphore = new Semaphore(PERMITS);
    private static final int THREADS = 10; // Всего потоков

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
                // Запрашиваем разрешение у семафора
                System.out.println("Поток " + id + " ожидает разрешения...");
                semaphore.acquire();
                
                System.out.println("Поток " + id + " получил разрешение. Доступно: " 
                                 + semaphore.availablePermits());
                
                // Имитация работы с ресурсом (2 секунды)
                Thread.sleep(2000);
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // Освобождаем разрешение
                semaphore.release();
                System.out.println("Поток " + id + " освободил разрешение. Доступно: " 
                                 + semaphore.availablePermits());
            }
        }
    }
}