package zadanie2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Czytelnia {
    private int licz_czyt = 0;
    private int licz_pisz = 0;
    private int czyt_pocz = 0;
    private int pis_pocz = 0;

    private final Lock zamek = new ReentrantLock();
    private final Condition czytelnicy = zamek.newCondition();
    private final Condition pisarze = zamek.newCondition();

    private void drukuj(String znak, String etap, String id, int powt) {
        System.out.printf("%s(%s) [%s, %d] :: [licz_czyt=%d, licz_czyt_pocz=%d, licz_pis=%d, licz_pis_pocz=%d]\n",
                znak, etap, id, powt, licz_czyt, czyt_pocz, licz_pisz, pis_pocz);
    }

    public void POCZ_CZYTANIA(String id, int powt) throws InterruptedException {
        zamek.lock();
        try {
            drukuj(">>>", "*", id, powt);
            if (licz_pisz + pis_pocz > 0) {
                czyt_pocz = czyt_pocz + 1;
                czytelnicy.await();
                czyt_pocz = czyt_pocz - 1;
            }
            licz_czyt = licz_czyt + 1;
            drukuj(">>>", "**", id, powt);
        } finally {
            zamek.unlock();
        }
    }

    public void KON_CZYTANIA(String id, int powt) {
        zamek.lock();
        try {
            drukuj("<<<", "*", id, powt);
            licz_czyt = licz_czyt - 1;
            if (licz_czyt == 0) {
                pisarze.signal();
            }
            drukuj("<<<", "**", id, powt);
        } finally {
            zamek.unlock();
        }
    }

    public void POCZ_PISANIA(String id, int powt) throws InterruptedException {
        zamek.lock();
        try {
            drukuj("==>", "*", id, powt);
            if (licz_czyt + licz_pisz > 0) {
                pis_pocz = pis_pocz + 1;
                pisarze.await();
                pis_pocz = pis_pocz - 1;
            }
            licz_pisz = 1;
            drukuj("==>", "**", id, powt);
        } finally {
            zamek.unlock();
        }
    }

    public void KON_PISANIA(String id, int powt) {
        zamek.lock();
        try {
            drukuj("<==", "*", id, powt);
            licz_pisz = 0;
            if (czyt_pocz > 0) {
                czytelnicy.signalAll();
            } else {
                pisarze.signal();
            }
            drukuj("<==", "**", id, powt);
        } finally {
            zamek.unlock();
        }
    }
}