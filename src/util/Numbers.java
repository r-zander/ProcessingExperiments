package util;

import java.util.Random;

import processing.core.PApplet;

public class Numbers {

    public static final Random RANDOM_NUMBER_GENERATOR = new Random();

    public static final float  SQRT_2                  = (float) PApplet.sqrt(2);

    public static final float  ONE_AND_A_HALF_PI       = PApplet.HALF_PI * 3;

    public static double random(double low, double high) {
        return Math.random() * (high - low) + low;
    }

    public static int random(int low, int high) {
        return RANDOM_NUMBER_GENERATOR.nextInt(high - low + 1) + low;
    }

    public static int random(int high) {
        return RANDOM_NUMBER_GENERATOR.nextInt(high);
    }

}
