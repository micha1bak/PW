package zadanie2;

public class Watek implements Runnable {

    private String _name;

    public Watek(String name) {
        _name = name;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Pozdrowienia z wątku " + _name + " " + i);
            long sleepTime = (long) (Math.random() * 100 + 100);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

