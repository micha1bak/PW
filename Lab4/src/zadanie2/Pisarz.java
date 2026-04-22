package zadanie2;

import java.util.Random;

class Pisarz implements Runnable {
    private final String id;
    private final int powt;
    private final int a, b, c, d;
    private final Czytelnia czytelnia;
    private final Random random = new Random();

    public Pisarz(String id, int powt, int a, int b, int c, int d, Czytelnia czytelnia) {
        this.id = id;
        this.powt = powt;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.czytelnia = czytelnia;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= powt; i++) {
                Thread.sleep(random.nextInt(b - a + 1) + a);
                czytelnia.POCZ_PISANIA(id, i);
                Thread.sleep(random.nextInt(d - c + 1) + c);
                czytelnia.KON_PISANIA(id, i);
            }
        } catch (InterruptedException e) {
        }
    }
}