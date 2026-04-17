package zadanie1;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Konsument extends Thread {
    private final String[] buf;
    private final int bufSize;
    private final int c = 2;
    private final int d = 12;
    private final int repetitions;
    private final Semaphore full;
    private final Semaphore empty;
    private final Semaphore read;
    private final AtomicInteger bufReadIndex;

    public Konsument(String name, String[] buf, Semaphore full, Semaphore empty, Semaphore read, int repetitions, AtomicInteger bufReadIndex, int bufSize) {
        this.buf = buf;
        super(name);
        this.full = full;
        this.empty = empty;
        this.read = read;
        this.repetitions = repetitions;
        this.bufReadIndex = bufReadIndex;
        this.bufSize = bufSize;
    }

    private void consume(int repNumber, int bufPosition, String data) {
        sleepFromAtoB(c, d);
        String message = "[ ";
        message += Thread.currentThread().getName() + ", ";
        message += repNumber + ", ";
        message += bufPosition + " ] :: ";
        message += data;
        System.out.println(message);
    }

    private void sleepFromAtoB(int a, int b) {
        long sleepTime = (long) (Math.random() * (b - a) + a);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    private int incrementBufIndex(int bufIndex) {
        return (bufIndex + 1) % bufSize;
    }
    @Override
    public void run() {
        for (int i = 1; i <= repetitions; i++) {
            String data = "";
            int currentIndex;
            try {
                full.acquire();
                read.acquire();
                currentIndex = bufReadIndex.get();
                data = buf[currentIndex];
                empty.release();
                bufReadIndex.set(incrementBufIndex(currentIndex));
                read.release();
                consume(i, currentIndex, data);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}