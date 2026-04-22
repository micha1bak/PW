package zadanie4;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {

        int m = 10;
        int n = 5;
        int k = 4;
        int l = 6;
        int a = 8, b = 12;
        int c = 2, d = 6;
        int powtorzenia = 1000;

        ZakladFryzjerski zaklad = new ZakladFryzjerski(l, k);

        List<Thread> watki = new ArrayList<>();

        for (int i = 0; i < m; i++) {
            Thread t = new Thread(new Klient("K-" + i, powtorzenia, a, b, zaklad));
            watki.add(t);
            t.start();
        }

        for (int i = 0; i < n; i++) {
            Thread t = new Thread(new Fryzjer("F-" + i, powtorzenia, c, d, zaklad));
            watki.add(t);
            t.start();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {}

        for (Thread t : watki) {
            t.interrupt();
        }

        for (Thread t : watki) {
            try {
                t.join();
            } catch (InterruptedException e) {}
        }
    }
}
