package zadanie4;

public class Test {
    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("Pozdrowienia z wątku:: lambda" + i);
            }
        });
        t1.start();

        Thread t2 = new Thread(new Pozdrowienia()::pozdrowienia1);
        t2.start();

        Thread t3 = new Thread(Pozdrowienia::pozdrowienia2);
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {}

        System.out.println("Koniec");
    }
}
