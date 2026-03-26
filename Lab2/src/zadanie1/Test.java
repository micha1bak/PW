package zadanie1;

public class Test {
    public static void main(String[] args) {
        boolean czySynchr = true;
        int powtorzenia = 100;

        DekkerThread t1 = new DekkerThread(0, czySynchr, powtorzenia);
        DekkerThread t2 = new DekkerThread(1, czySynchr, powtorzenia);

        t1.start();
        t2.start();
    }
}
