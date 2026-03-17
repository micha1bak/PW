package zad1;
public class Test {
    public static void main(String[] args) throws InterruptedException {
        Thread[] w = new Thread[10];

        for (int i = 0; i < 10; i++) {
            w[i] = new mojWatek("" + i);
        }
        for (int i = 0; i < 10; i++) {
            w[i].start();
        }
        for (int i = 0; i < 10; i++) {
            w[i].join();
        }
        System.out.println("Koniec");
    }
}