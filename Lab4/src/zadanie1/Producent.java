package zadanie1;

import java.util.Random;

class Producent implements Runnable {
    private final String id;
    private final int iteracje;
    private final int a, b;
    private final Bufor bufor;
    private final Random random = new Random();

    public Producent(String id, int iteracje, int a, int b, Bufor bufor) {
        this.id = id;
        this.iteracje = iteracje;
        this.a = a;
        this.b = b;
        this.bufor = bufor;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= iteracje; i++) {
                int czasCzekania = random.nextInt((b - a) + 1) + a;
                Thread.sleep(czasCzekania);
                int wartosc = random.nextInt(100);
                int pozycja = bufor.wstaw(wartosc);
                System.out.println("[" + id + ", " + i + "] => (" + wartosc + ", " + pozycja + ")");
            }
        } catch (InterruptedException e) {
        }
    }
}