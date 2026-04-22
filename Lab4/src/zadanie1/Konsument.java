package zadanie1;

import java.util.Random;

class Konsument implements Runnable {
    private final String id;
    private final int iteracje;
    private final int c, d;
    private final Bufor bufor;
    private final Random random = new Random();

    public Konsument(String id, int iteracje, int c, int d, Bufor bufor) {
        this.id = id;
        this.iteracje = iteracje;
        this.c = c;
        this.d = d;
        this.bufor = bufor;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= iteracje; i++) {
                int[] dane = bufor.pobierz();
                int wartosc = dane[0];
                int pozycja = dane[1];
                int czasCzekania = random.nextInt((d - c) + 1) + c;
                Thread.sleep(czasCzekania);
                System.out.println("[" + id + ", " + i + "] <= (" + wartosc + ", " + pozycja + ")");
            }
        } catch (InterruptedException e) {
        }
    }
}