package zadanie1;

public class Test {

    public static void main(String[] args) {

        int m = 4;
        int n = 5;
        int a = 1, b = 10;
        int c = 2, d = 12;
        int powtorzeniaProducenta = 100;
        int powtorzeniaKonsumenta = 80;
        int rozmiarBufora = 10;

        Thread[] p = new Thread[m];
        Thread[] k = new Thread[n];
        Bufor bufor = new Bufor(rozmiarBufora);

        for (int i = 0; i < m; i++) {
            p[i] = new Thread(new Producent("P-" + i, powtorzeniaProducenta, a, b, bufor));
            p[i].start();
        }

        for (int i = 0; i < n; i++) {
            k[i] = new Thread(new Konsument("K-" + i, powtorzeniaKonsumenta, c, d, bufor));
            k[i].start();
        }

        try {
            for (int i = 0; i < m; i++){
                p[i].join();
            }
            for (int i = 0; i < n; i++){
                k[i].join();
            }
        } catch (InterruptedException e) {}
    }
}