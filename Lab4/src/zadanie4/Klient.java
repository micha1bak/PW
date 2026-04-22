package zadanie4;

import java.util.Random;

class Klient implements Runnable {
    private final String id;
    private final int powt;
    private final int a, b;
    private final ZakladFryzjerski zaklad;
    private final Random random = new Random();

    public Klient(String id, int powt, int a, int b, ZakladFryzjerski zaklad) {
        this.id = id;
        this.powt = powt;
        this.a = a;
        this.b = b;
        this.zaklad = zaklad;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= powt; i++) {
                if (Thread.currentThread().isInterrupted()) break;
                Thread.sleep(random.nextInt(b - a + 1) + a);
                boolean wynik = false;
                while (!wynik) {
                    if (Thread.currentThread().isInterrupted()) break;
                    wynik = zaklad.ZADANIE_USLUGI(id, i);
                    if (!wynik) {
                        Thread.sleep(10);
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}