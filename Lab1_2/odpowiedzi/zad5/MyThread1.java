package zad5;

import java.util.Random;

class MyThread1 extends Thread {
    private Thread[] dzieci = new Thread[3];
    private Random r = new Random();

    public MyThread1(String nazwa) {
        super(nazwa);
    }

    public void run() {
        for (int i = 0; i < 3; i++) {
            int nr = i + 1;
            dzieci[i] = new Thread(() -> {
                long a = 0, b = 1, iteracja = 0;
                while (!Thread.currentThread().isInterrupted()) {
                    long suma = a + b; a = b; b = suma;
                    iteracja++;
                }
                System.out.println("Dziecko " + nr + " wątku " + getName() + " : " + iteracja + ", Fib: " + a);
            });
            dzieci[i].start();
        }

        try {
            for (int i = 1; i <= 5; i++) {
                int czas = r.nextInt(2500, 3001);
                Thread.sleep(czas);
                System.out.println(getName() + " " + i + " :: spałem przez: " + czas);
            }
        } catch (InterruptedException e) {
            System.out.println(getName() + " - zostałem obudzony");
            for (Thread d : dzieci) {
                d.interrupt();
                try { d.join(); } catch (InterruptedException ex) {}
            }
            return;
        }
        System.out.println(getName() + " - KONIEC");
    }
}