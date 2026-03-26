package zadanie4;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class SemThread extends Thread {

    private int nr;
    private char metoda;
    private int liczbaPowtorzen;
    static Semaphore semafor = new Semaphore(1);
    static ReentrantLock lock = new ReentrantLock();
    static char[] znaki = {'+', '-', '*', '/', '#'};

    public SemThread(int nr, char metoda, int liczbaPowtorzen) {
        this.nr = nr;
        this.metoda = metoda;
        this.liczbaPowtorzen = liczbaPowtorzen;
    }

    public void sprawyWlasne() {
        try {
            long czas = (long) (Math.random() * 10) + 1;
            Thread.sleep(czas);
        } catch (InterruptedException e) {}
    }

    public void sekcjaKrytyczna(long i) {
        System.out.println("\nSekcja krytyczna wątku: SemThread-" + nr + ", nr powt.= " + i);
        for (int j = 0; j < 100; j++) {
            System.out.print(znaki[nr]);
        }
        System.out.println();
    }

    private void dzialanieSem() {
        for (int i = 1; i <= liczbaPowtorzen; i++) {
            sprawyWlasne();
            try {
                semafor.acquire();
                sekcjaKrytyczna(i);
            } catch (InterruptedException e) {}
            finally {
                semafor.release();
            }
        }
    }

    private void dzialanieMetSynchr() {
        for (int i = 1; i <= liczbaPowtorzen; i++) {
            sprawyWlasne();
            wykonajSekcjeMetodaSynchr(this, i);
        }
    }

    private static synchronized void wykonajSekcjeMetodaSynchr(SemThread watek, int nrPowt) {
        watek.sekcjaKrytyczna(nrPowt);
    }

    private void dzialanieLock() {
        for (int i = 1; i <= liczbaPowtorzen; i++) {
            sprawyWlasne();
            lock.lock();
            sekcjaKrytyczna(i);
            lock.unlock();
        }
    }

    public void run() {
        if (metoda == 'a') {
            dzialanieSem();
        }
        else if (metoda == 'b') {
            dzialanieMetSynchr();
        }
        else {
            dzialanieLock();
        }
    }

}
