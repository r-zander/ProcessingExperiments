package util;

import processing.core.PApplet;

public class Numbers {

    public static final float SQRT_2            = (float) PApplet.sqrt(2);

    public static final float ONE_AND_A_HALF_PI = PApplet.HALF_PI * 3;

    public static double random(double low, double high) {
        return Math.random() * (high - low) + low;
    }

    public static int random(int low, int high) {
        return (int) (Math.random() * (high - low) + low + 0.5);
    }

}
