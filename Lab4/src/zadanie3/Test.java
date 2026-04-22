package zadanie3;

public class Test {
    public static void main(String[] args) {
        int a = 8, b = 12;
        int c = 2, d = 6;
        int powtorzenia = 100;

        Widelce widelce = new Widelce();
        Thread[] filozofowie = new Thread[5];

        for (int i = 0; i < 5; i++) {
            filozofowie[i] = new Thread(new Filozof(i, "F-" + i, powtorzenia, a, b, c, d, widelce));
            filozofowie[i].start();
        }

        for (int i = 0; i < 5; i++) {
            try {
                filozofowie[i].join();
            } catch (InterruptedException e) {}
        }
    }
}

