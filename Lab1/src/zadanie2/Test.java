package zadanie2;

public class Test {
    public static void main(String[] args) {
        Thread[] tab =  new Thread[10];

        for (int i = 0; i < tab.length; i++) {
            tab[i] = new Thread(new Watek("" + i));
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
