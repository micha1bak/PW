package zadanie6;

public class Watek extends Thread {
    private Licznik licznik;

    public Watek(String name, Licznik licznik) {
        super(name);
        this.licznik = licznik;
    }

    public void run() {
        for (int i = 0; i < 5000000; i++) {
            licznik.incNiesynch();
        }
    }
}
