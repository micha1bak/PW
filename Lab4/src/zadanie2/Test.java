package zadanie2;

public class Test {

    public static void main(String[] args) {
        int m = 4;
        int n = 2;
        int a = 5, b = 15;
        int c = 1, d = 5;
        int powtorzenia = 100;

        Thread[] czytelnicy = new Thread[m];
        Thread[] pisarze = new Thread[n];
        Czytelnia czytelnia = new Czytelnia();

        for (int i = 0; i < m; i++) {
            czytelnicy[i] = new Thread(new Czytelnik("C-" + i, powtorzenia, a, b, c, d, czytelnia));
            czytelnicy[i].start();
        }

        for (int i = 0; i < n; i++) {
            pisarze[i] = new Thread(new Pisarz("P-" + i, powtorzenia, a, b, c, d, czytelnia));
            pisarze[i].start();
        }

        try {
            for (int i = 0; i < m; i++){
                czytelnicy[i].join();
            }
            for (int i = 0; i < n; i++){
                pisarze[i].join();
            }
        } catch (InterruptedException e) {}
    }
}

