package zad1;
import java.util.Random;

public class mojWatek extends Thread {
    public String name;
    public Random randomowy = new Random();
    public mojWatek(String name) {
        this.name = name;
    }
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("Pozdrowienia z wątku " + name + " nr " + i);
            try {
                Thread.sleep(randomowy.nextInt(100, 201));
            } catch (InterruptedException e) {}
        }
    }
}