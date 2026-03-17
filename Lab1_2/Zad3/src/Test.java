public class Test {
    public static void main(String[] args) {
        Thread[] tab = new Thread[10];

        for (int i = 0; i < tab.length; i++) {

            tab[i] = new Thread(new Runnable() {
                public void run() {
                    for (int j = 0; j < 10; j++) {
                        System.out.println("Pozdrowienia z wątku " + Thread.currentThread().getName() + " " + j);
                        long sleepTime = (long) (Math.random() * 100 + 100);
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }, "" + i);

            tab[i].start();
        }

        for (Thread thread : tab) {
            try {
                thread.join();
            } catch (InterruptedException e) {}
        }

        System.out.println("Koniec");
    }
}
