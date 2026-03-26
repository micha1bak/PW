package zadanie2;

public class PetersonThread extends Thread {
    private int nr;
    private boolean czySynchr;
    static private volatile boolean[] chce = new boolean[2];
    static private volatile int czyjaKolej;
    static private long liczbaPowtorzen;
    private String[] znaki = {"+", "-"};

    public PetersonThread(int nr, boolean czySynchr, long liczbaPowtorzen) {
        this.nr = nr % 2;
        this.czySynchr = czySynchr;
        this.liczbaPowtorzen = liczbaPowtorzen;
    }

    public void sprawyWlasne() {
        try {
            long czas = (long) (Math.random() * 10) + 1;
            Thread.sleep(czas);
        } catch (InterruptedException e) {}
    }

    public void sekcjaKrytyczna(long i) {
        System.out.println("\nSekcja krytyczna wątku: Peterson-" + nr + ", nr powt.= " + i);
        for (int j = 0; j < 100; j++) {
            System.out.print(znaki[nr]);
        }
        System.out.println();
    }

    private void dzialanieNiesynchr() {
        for (int i = 1; i <= liczbaPowtorzen; i++) {
            sprawyWlasne();
            sekcjaKrytyczna(i);
        }
    }

    private void dzialanieSynchr() {
        int me = this.nr;
        int other = 1 - me;
        for (int i = 1; i <= liczbaPowtorzen; i++) {
            sprawyWlasne();
            chce[me] = true;
            czyjaKolej = other;
            while (chce[other] && czyjaKolej == other) {}
            sekcjaKrytyczna(i);
            chce[me] = false;
        }
    }

    public void run() {
        if (czySynchr) {
            dzialanieSynchr();
        }
        else {
            dzialanieNiesynchr();
        }
    }
}

