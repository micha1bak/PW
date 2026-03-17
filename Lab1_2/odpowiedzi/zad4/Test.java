package zad4;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Powitania p = new Powitania();

        Thread t1 = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                System.out.println("Pozdrowienia z wątku:: lambda:: " + i);
            }
        });

        Thread t2 = new Thread(p::pozdrowienia1);

        Thread t3 = new Thread(Powitania::pozdrowienia2);

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("Koniec");
    }
}
