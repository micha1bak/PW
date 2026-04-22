package zadanie4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ZakladFryzjerski {
    private final int poj_pocz;
    private int l_czek = 0;
    private int wolne_fot;

    private final Lock zamek = new ReentrantLock();
    private final Condition klient = zamek.newCondition();
    private final Condition fryzjer = zamek.newCondition();
    private final Condition fotel = zamek.newCondition();

    public ZakladFryzjerski(int poj_pocz, int liczba_foteli) {
        this.poj_pocz = poj_pocz;
        this.wolne_fot = liczba_foteli;
    }

    private void drukuj(String znak, String etap, String id, int powt) {
        System.out.printf("%s(%s) [%s, %d] :: %d, %d\n", znak, etap, id, powt, l_czek, wolne_fot);
    }

    public boolean ZADANIE_USLUGI(String id, int powt) throws InterruptedException {
        zamek.lock();
        try {
            drukuj(">>>", "*", id, powt);
            boolean wynik;
            if (l_czek < poj_pocz) {
                l_czek = l_czek + 1;
                klient.signal();
                fryzjer.await();
                wynik = true;
            } else {
                wynik = false;
            }
            drukuj(">>>", "**", id, powt);
            return wynik;
        } finally {
            zamek.unlock();
        }
    }

    public void ROZPOCZECIE_USLUGI(String id, int powt) throws InterruptedException {
        zamek.lock();
        try {
            drukuj(">>>", "*", id, powt);
            while (l_czek == 0) {
                klient.await();
            }
            l_czek = l_czek - 1;
            while (wolne_fot == 0) {
                fotel.await();
            }
            wolne_fot = wolne_fot - 1;
            fryzjer.signal();
            drukuj(">>>", "**", id, powt);
        } finally {
            zamek.unlock();
        }
    }

    public void ZAKONCZENIE_USLUGI(String id, int powt) {
        zamek.lock();
        try {
            drukuj("<<<", "*", id, powt);
            wolne_fot = wolne_fot + 1;
            fotel.signal();
            drukuj("<<<", "**", id, powt);
        } finally {
            zamek.unlock();
        }
    }
}