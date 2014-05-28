package util;

public class Numbers {

    public static final float SQRT_2 = (float) Math.sqrt(2);

    public static double random(double low, double high) {
        return Math.random() * (high - low) + low;
    }

    public static int random(int low, int high) {
        return (int) (Math.random() * (high - low) + low);
    }

}
