package zadanie1;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Producent extends Thread{
    private final String[] buf;
    private final int bufSize;
    private final int a = 1;
    private final int b = 10;
    private final int repetitions;
    private final Semaphore full;
    private final Semaphore empty;
    private final Semaphore write;
    private AtomicInteger bufWriteIndex;
    public Producent(String name, String[] buf, Semaphore full, Semaphore empty, Semaphore write, int repetitions, AtomicInteger bufWriteIndex, int bufSize) {
        this.buf = buf;
        super(name);
        this.full = full;
        this.empty = empty;
        this.write = write;
        this.repetitions = repetitions;
        this.bufWriteIndex = bufWriteIndex;
        this.bufSize = bufSize;
    }
    private int produce(){
         return (int)(Math.random() * 100);
    }
    private void sleepFromAtoB(int a, int b) {
        long sleepTime = (long) (Math.random() * (b - a) + a);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    private String formatedData(int repNumber, int bufPosition, int product) {
        String data = "Dana=[ ";
        data += Thread.currentThread().getName() + ", ";
        data += repNumber + ", ";
        data += bufPosition + ", ";
        data += product + " ]";
        return data;
    }
    private int incrementBufIndex(int bufIndex) {
        return (bufIndex + 1) % bufSize;
    }
    @Override
    public void run() {
        for (int i = 1; i <= repetitions; i++) {
            int currentIndex;
            try {
                sleepFromAtoB(a, b);
                int product = produce();
                empty.acquire();
                write.acquire();
                currentIndex = bufWriteIndex.get();
                buf[currentIndex] = formatedData(i, currentIndex, product);
                full.release();
                bufWriteIndex.set(incrementBufIndex(currentIndex));
                write.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
