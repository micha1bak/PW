package zadanie3;

public class Test {
    public static void main(String[] args) {
        boolean czySynchr = true;
        int powtorzenia = 100;
        LamportThread[] tab = new LamportThread[5];
        for (int i = 0; i < tab.length; i++) {
            tab[i] = new LamportThread(i, czySynchr, powtorzenia);
            tab[i].start();
        }
    }
}
