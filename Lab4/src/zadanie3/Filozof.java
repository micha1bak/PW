package zadanie3;

import java.util.Random;

class Filozof implements Runnable {
    private final int nr;
    private final String id;
    private final int powt;
    private final int a, b, c, d;
    private final Widelce widelce;
    private final Random random = new Random();

    public Filozof(int nr, String id, int powt, int a, int b, int c, int d, Widelce widelce) {
        this.nr = nr;
        this.id = id;
        this.powt = powt;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.widelce = widelce;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= powt; i++) {
                Thread.sleep(random.nextInt(b - a + 1) + a);
                widelce.WEZ(nr, id, i);
                Thread.sleep(random.nextInt(d - c + 1) + c);
                widelce.ODLOZ(nr, id, i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}