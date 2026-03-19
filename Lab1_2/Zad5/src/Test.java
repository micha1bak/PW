public class Test {
    public static void main(String[] args) {
        Thread[] tab = new Thread[2];

        for (int i = 0; i < 2; i++) {
            tab[i] = new MyThread1("Glowny " + i);
            tab[i].start();
        }

        try {
            Thread.sleep(4500);
        } catch (InterruptedException e) {}
        tab[0].interrupt();
        try {
            tab[0].join();
        } catch (InterruptedException e) {}

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
        tab[1].interrupt();
        try {
            tab[1].join();
        } catch (InterruptedException e) {}

        System.out.println("KONIEC");

    }
}
