import java.lang.Math;
import java.util.function.DoubleToLongFunction;

public class Test {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            long num = (long) Math.random();
            System.out.println(num + " " + Math.random());
        }
    }
}
