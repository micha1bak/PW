package zad5;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        MyThread1 t1 = new MyThread1("Watek-A");
        MyThread1 t2 = new MyThread1("Watek-B");
        t1.start();
        t2.start();
        try {
            Thread.sleep(4500);
        } catch (InterruptedException e) {}
        t1.interrupt();
        t1.join();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
        t2.interrupt();
        t2.join();
        System.out.println("Koniec");
    }
}
