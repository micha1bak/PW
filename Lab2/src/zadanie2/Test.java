package zadanie2;

public class Test {
    public static void main(String[] args) {
        boolean czySynchr = true;
        int powtorzenia = 100;

        PetersonThread t1 = new PetersonThread(0, czySynchr, powtorzenia);
        PetersonThread t2 = new PetersonThread(1, czySynchr, powtorzenia);

        t1.start();
        t2.start();
    }
}
