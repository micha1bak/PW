package zadanie3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Widelce {
    private final boolean[] zajety = new boolean[5];
    private int jest = 0;
    private final Lock zamek = new ReentrantLock();
    private final Condition[] widelec = new Condition[5];
    private final Condition lokaj = zamek.newCondition();

    public Widelce() {
        for (int i = 0; i < 5; i++) {
            zajety[i] = false;
            widelec[i] = zamek.newCondition();
        }
    }

    private void drukuj(String znak, String etap, String id, int powt) {
        System.out.printf("%s(%s) [%s, %d] :: [%d, %d, %d, %d, %d] - %d\n",
                znak, etap, id, powt,
                zajety[0] ? 1 : 0, zajety[1] ? 1 : 0, zajety[2] ? 1 : 0,
                zajety[3] ? 1 : 0, zajety[4] ? 1 : 0,
                jest);
    }

    public void WEZ(int i, String id, int powt) throws InterruptedException {
        zamek.lock();
        try {
            drukuj(">>>", "*", id, powt);
            if (jest == 4) {
                lokaj.await();
            }
            jest = jest + 1;
            if (zajety[i]) {
                widelec[i].await();
            }
            zajety[i] = true;
            if (zajety[(i + 1) % 5]) {
                widelec[(i + 1) % 5].await();
            }
            zajety[(i + 1) % 5] = true;
            drukuj(">>>", "**", id, powt);
        } finally {
            zamek.unlock();
        }
    }

    public void ODLOZ(int i, String id, int powt) {
        zamek.lock();
        try {
            drukuj("<<<", "*", id, powt);
            zajety[i] = false;
            widelec[i].signal();
            zajety[(i + 1) % 5] = false;
            widelec[(i + 1) % 5].signal();
            jest = jest - 1;
            lokaj.signal();
            drukuj("<<<", "**", id, powt);
        } finally {
            zamek.unlock();
        }
    }
}