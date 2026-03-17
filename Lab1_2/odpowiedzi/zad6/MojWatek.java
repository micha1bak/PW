package zad6;

class MojWatek extends Thread {
    private Licznik licznik;

    public MojWatek(String nazwa, Licznik licznik) {
        super(nazwa);
        this.licznik = licznik;
    }

    public void run() {
        for (int i = 0; i < 5000000; i++) {
            licznik.incNiesynch();
        }
    }
}