package zadanie4;

public class Pozdrowienia {
    public void pozdrowienia1() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Pozdrowienia z wątku::pozdrowienia1::" + i);
        }
    }

    public static void pozdrowienia2() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Pozdrowienia z wątku::pozdrowienia2::" + i);
        }
    }
}
