package zadanie4;

import java.util.concurrent.Semaphore;

public class Test {

    private static int licz_fil_przy_stole = 0;

    public static void main(String[] args) {

        Semaphore allow = new Semaphore(4);
        Semaphore protect = new Semaphore(1);
        Semaphore[] fork =  new Semaphore[5];
        Thread[] f = new Thread[5];

        int a = 5, b = 15, c = 1, d = 5;

        for (int i = 0; i < 5; i++) {
            fork[i] = new Semaphore(1);
        }

        Runnable filozof = () -> {
            String name = Thread.currentThread().getName();
            int id = Integer.parseInt(name.split("-")[1]);
            int repetition = 1;
            while(!Thread.currentThread().isInterrupted()){
                try {
                    sleepRandom(a, b);
                    allow.acquire();
                    fork[id].acquire();
                    fork[(id + 1) % 5].acquire();
                    protect.acquire();
                    licz_fil_przy_stole++;
                    System.out.printf(">>>[%s, %d] :: licz_fil_przy_stole=%d , [w0=%d, w1=%d, w2=%d, w3=%d, w4=%d]\n",
                            name, repetition, licz_fil_przy_stole,
                            1 - fork[0].availablePermits(), 1 - fork[1].availablePermits(),
                            1 - fork[2].availablePermits(), 1 - fork[3].availablePermits(),
                            1 - fork[4].availablePermits());
                    protect.release();
                    sleepRandom(c, d);
                    protect.acquire();
                    System.out.printf("<<<[%s, %d] :: licz_fil_przy_stole=%d , [w0=%d, w1=%d, w2=%d, w3=%d, w4=%d]\n",
                            name, repetition, licz_fil_przy_stole,
                            1 - fork[0].availablePermits(), 1 - fork[1].availablePermits(),
                            1 - fork[2].availablePermits(), 1 - fork[3].availablePermits(),
                            1 - fork[4].availablePermits());
                    licz_fil_przy_stole--;
                    protect.release();
                    fork[id].release();
                    fork[(id + 1) % 5].release();
                    allow.release();
                    repetition++;
                } catch (InterruptedException e) {
                    break;
                }
            }
        };

        for (int i = 0; i < 5; i++) {
            f[i] = new Thread(filozof, "F-" + i);
            f[i].start();
        }

        try {
            Thread.sleep(5000);
            for (Thread t : f) {
                t.interrupt();
            }
            for (Thread t : f) {
                t.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sleepRandom(int min, int max) {
        long sleepTime = (long) (Math.random() * (max - min) + min);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
