package zadanie3;

public class LamportThread extends Thread {
    private int nr;
    private boolean czySynchr;
    static private volatile boolean[] chce = new boolean[2];
    static private long liczbaPowtorzen;
    private String[] znaki = {"+", "-", "#", "@", "&"};
    static final int N = 5;
    static volatile boolean[] wybieranie = new boolean[N];
    static volatile int[] numerek = new int[N];

    public LamportThread(int nr, boolean czySynchr, long liczbaPowtorzen) {
        this.nr = nr;
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
        System.out.println("\nSekcja krytyczna wątku: Lamport-" + nr + ", nr powt.= " + i);
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

    private int znajdzMaxNumerek() {
        int max = 0;
        for (int i = 0; i < N; i++) {
            if (numerek[i] > max) {
                max = numerek[i];
            }
        }
        return max;
    }

    private void dzialanieSynchr() {
        for (int i = 1; i <= liczbaPowtorzen; i++) {
            sprawyWlasne();
            wybieranie[nr] = true;
            numerek[nr] = 1 + znajdzMaxNumerek();
            wybieranie[nr] = false;
            for (int j = 0; j < N; j++) {
                while (wybieranie[j]) {}
                while (numerek[j] != 0 && (numerek[j] < numerek[nr] || (numerek[j] == numerek[nr] && j < nr))) {}
            }
            sekcjaKrytyczna(i);
            numerek[nr] = 0;
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

