package zadanie4;

import java.util.Random;

class Fryzjer implements Runnable {
    private final String id;
    private final int powt;
    private final int c, d;
    private final ZakladFryzjerski zaklad;
    private final Random random = new Random();

    public Fryzjer(String id, int powt, int c, int d, ZakladFryzjerski zaklad) {
        this.id = id;
        this.powt = powt;
        this.c = c;
        this.d = d;
        this.zaklad = zaklad;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= powt; i++) {
                if (Thread.currentThread().isInterrupted()) break;
                zaklad.ROZPOCZECIE_USLUGI(id, i);
                Thread.sleep(random.nextInt(d - c + 1) + c);
                zaklad.ZAKONCZENIE_USLUGI(id, i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
