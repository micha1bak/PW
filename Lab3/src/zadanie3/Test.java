package zadanie3;

import java.util.concurrent.Semaphore;

public class Test {
    private static int cp = 0;
    private static int cc = 0;
    private static int pp = 0;
    private static int pc = 0;

    public static void main(String[] args) {

        Semaphore read = new Semaphore(0);
        Semaphore write = new Semaphore(0);
        Semaphore protect = new Semaphore(1);

        int a = 5, b = 15, c = 1, d = 5;
        int K = 3;

        int readerCount = 5;
        int writerCount = 5;

        Thread[] readers = new Thread[readerCount];
        Thread[] writers = new Thread[writerCount];

        Runnable reader = () -> {
            int repetitionCounter = 1;
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    sleepRandom(a, b);
                    protect.acquire();
                    if ((pp + pc) == 0 && cc < K) {
                        cc++;
                        read.release();
                    } else {
                        cp++;
                    }
                    protect.release();
                    read.acquire();
                    protect.acquire();
                    System.out.println(">>> [" + Thread.currentThread().getName() + ", " + repetitionCounter + "]");
                    protect.release();
                    sleepRandom(c, d);
                    protect.acquire();
                    System.out.println("<<< [" + Thread.currentThread().getName() + ", " + repetitionCounter + "]");
                    cc--;
                    if (cc == 0 && pp > 0) {
                        pc = 1;
                        pp--;
                        write.release();
                    } else if (cp > 0 && pp == 0) {
                        cc++;
                        cp--;
                        read.release();
                    }
                    protect.release();
                } catch (InterruptedException e) {
                    break;
                }
                repetitionCounter++;
            }
        };

        Runnable writer = () -> {
            int repetitionCounter = 1;
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    sleepRandom(a, b);
                    protect.acquire();
                    if ((cc + pc) == 0) {
                        pc++;
                        write.release();
                    } else {
                        pp++;
                    }
                    protect.release();
                    write.acquire();
                    protect.acquire();
                    System.out.println("==> [" + Thread.currentThread().getName() + ", " + repetitionCounter + "]");
                    protect.release();
                    sleepRandom(c, d);
                    protect.acquire();
                    System.out.println("<== [" + Thread.currentThread().getName() + ", " + repetitionCounter + "]");
                    pc = 0;
                    if (cp > 0) {
                        int czytDoObudz = Math.min(cp, K);
                        for (int i = 0; i < czytDoObudz; i++) {
                            cc++;
                            cp--;
                            read.release();
                        }
                    } else if (pp > 0) {
                        pc = 1;
                        pp--;
                        write.release();
                    }
                    protect.release();
                } catch (InterruptedException e) {
                    break;
                }
                repetitionCounter++;
            }
        };

        for (int i = 0; i < readerCount; i++) {
            readers[i] = new Thread(reader, "C-" + i);
            readers[i].start();
        }

        for (int i = 0; i < writerCount; i++) {
            writers[i] = new Thread(writer, "P-" + i);
            writers[i].start();
        }

        try {
            Thread.sleep(5000);
            for (Thread t : readers) {
                t.interrupt();
            }
            for (Thread t : writers) {
                t.interrupt();
            }
            for (Thread t : readers) {
                t.join();
            }
            for (Thread t : writers) {
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
