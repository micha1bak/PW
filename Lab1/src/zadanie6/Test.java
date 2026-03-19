package zadanie6;

public class Test {
    public static void main(String[] args) {

        Thread[] tab = new Thread[10];
        Licznik licznik = new Licznik();

        for(int i = 0; i < tab.length; i++){
            tab[i] = new Watek("" + i, licznik);
            tab[i].start();
        }

        for (Thread t : tab) try {
            t.join();
        } catch (InterruptedException e) {}

        System.out.println("Stan licznika = " + licznik.get());
    }
}
