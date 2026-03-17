package zad3;

import java.util.Random;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Thread[] w = new Thread[10];
        Random r = new Random();

        for (int i = 0; i < 10; i++) {
            String name = "" + i;

            w[i] = new Thread(new Runnable() {
                public void run() {
                    for (int j = 1; j <= 10; j++) {
                        System.out.println("Pozdrowienia z wątku " + name + " nr " + j);
                        try {
                            Thread.sleep(r.nextInt(100, 201));
                        } catch (InterruptedException e) {}
                    }
                }
            });
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