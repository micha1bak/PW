package zadanie5;

public class MyThread1 extends Thread {
    public MyThread1(String name) {
        super(name);
    }

    public void run() {
        Thread[] tab = new Thread[3];
        for (int i = 0; i < 3; i++) {
            tab[i] = new Thread(() -> {
                int fib_prev = 0;
                int fib_curr = 1;
                int k = 1;
                while(true) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Przerwanie wątku " + Thread.currentThread().getName() + ", k=" + k + ", fib=" + fib_curr);
                        break;
                    }
                    else {
                        fib_curr = fib_prev + fib_curr;
                        fib_prev = fib_curr - fib_prev;
                        k++;
                    }
                }
            }, "Potomny" + getName() + i);
            tab[i].start();
        }
        for (int i = 0; i < 5; i++) {
            try {
                long sleepTime = (long) (Math.random() * 500 + 2500);
                Thread.sleep(sleepTime);
                System.out.println(getName() + " " + i + " ::spałem przez: " + sleepTime);
            } catch (InterruptedException e1) {
                System.out.println(getName() + " - zostałem obudzony");
                for (Thread t : tab) t.interrupt();
                for (Thread t : tab) {
                    try {
                        t.join();
                    } catch (InterruptedException e2) {}
                }
                return;
            }
        }

        System.out.println(getName() + " - KONIEC");
    }
}
