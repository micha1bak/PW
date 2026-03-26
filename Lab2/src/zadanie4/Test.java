package zadanie4;

public class Test {
    public static void main(String[] args) {
        char metoda = 'c';
        int powtorzenia = 100;
        SemThread[] tab = new SemThread[5];
        for (int i = 0; i < tab.length; i++) {
            tab[i] = new SemThread(i, metoda, powtorzenia);
            tab[i].start();
        }

        for (SemThread t: tab) {
            try {
                t.join();
            } catch (InterruptedException e) {}
        }
    }
}
