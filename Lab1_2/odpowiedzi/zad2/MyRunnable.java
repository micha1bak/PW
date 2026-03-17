package zad2;

import java.util.Random;

public class MyRunnable implements Runnable {
    private String name;
    private Random randomowe = new Random();

    public MyRunnable(String name) {
        this.name = name;
    }

    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("Pozdrowienia z wątku " + name + " nr " + i);
            try {
                Thread.sleep(randomowe.nextInt(100, 201));
            } catch (InterruptedException e) {}
        }
    }
}