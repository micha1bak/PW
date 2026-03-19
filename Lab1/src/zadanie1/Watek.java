package zadanie1;

public class Watek extends Thread {

    public Watek(String name) {
        super(name);
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Pozdrowienia z wątku " + getName() + " " + i);
            long sleepTime = (long) (Math.random() * 100 + 100);
            try {
                sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
