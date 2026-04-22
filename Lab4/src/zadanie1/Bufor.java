package zadanie1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Bufor {
    private final int N;
    private final int[] pula;
    private int wej = 0;
    private int wyj = 0;
    private int licz = 0;

    private final Lock zamek = new ReentrantLock();
    private final Condition pelny = zamek.newCondition();
    private final Condition pusty = zamek.newCondition();

    public Bufor(int rozmiar) {
        this.N = rozmiar;
        this.pula = new int[N];
    }

    public int wstaw(int elem) throws InterruptedException {
        zamek.lock();
        try {
            while (licz == N) {
                pelny.await();
            }
            int pozycja = wej;
            pula[wej] = elem;
            wej = (wej + 1) % N;
            licz++;
            pusty.signal();
            return pozycja;
        } finally {
            zamek.unlock();
        }
    }

    public int[] pobierz() throws InterruptedException {
        zamek.lock();
        try {
            while (licz == 0) {
                pusty.await();
            }
            int pozycja = wyj;
            int elem = pula[wyj];
            wyj = (wyj + 1) % N;
            licz--;
            pelny.signal();
            return new int[]{elem, pozycja};
        } finally {
            zamek.unlock();
        }
    }
}