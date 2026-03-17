package zad6;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Licznik wspolnyLicznik = new Licznik();
        Thread[] watki = new Thread[10];

        for (int i = 0; i < 10; i++) {
            watki[i] = new MojWatek("W-" + i, wspolnyLicznik);
            watki[i].start();
        }

        for (Thread w : watki) {
            w.join();
        }

        System.out.println("Stan licznika = " + wspolnyLicznik.get());
    }
}
