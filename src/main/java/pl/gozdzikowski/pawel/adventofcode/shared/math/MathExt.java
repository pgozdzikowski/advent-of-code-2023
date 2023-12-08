package pl.gozdzikowski.pawel.adventofcode.shared.math;

public class MathExt {

    public static long gcd(long a,long b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    public static long lcm(int[] arr) {
        long lcm = arr[0];
        for (int i = 1; i < arr.length; i++) {
            int currentNumber = arr[i];
            lcm = (lcm * currentNumber) / gcd(lcm, currentNumber);
        }
        return lcm;
    }
}
