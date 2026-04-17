package zadanie1;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
    public static void main(String[] args) {
        int m = 4;
        int n = 5;
        int producerRepetitions = 50;
        int consumerRepetitions = producerRepetitions * m / n;
        int bufSize = 5;
        AtomicInteger bufWriteIndex = new AtomicInteger(0);
        AtomicInteger bufReadIndex = new AtomicInteger(0);
        String[] buf = new String[bufSize];
        Semaphore full = new Semaphore(0);
        Semaphore empty = new Semaphore(bufSize);
        Semaphore write = new Semaphore(1);
        Semaphore read = new Semaphore(1);
        Producent[] p = new Producent[m];
        Konsument[] k = new Konsument[n];
        for (int i = 0; i < m; i++){
            p[i] = new Producent("P-" + i, buf, full, empty, write, producerRepetitions, bufWriteIndex, bufSize);
        }
        for (int i = 0; i < n; i++){
            k[i] = new Konsument("K-" + i, buf, full, empty, read, consumerRepetitions, bufReadIndex, bufSize);
        }
        for (int i = 0; i < m; i++){
            p[i].start();
        }
        for (int i = 0; i < n; i++){
            k[i].start();
        }
        try {
            for (int i = 0; i < m; i++){
                p[i].join();
            }
            for (int i = 0; i < n; i++){
                k[i].join();
            }
        } catch (InterruptedException e) {}
    }
}
