package util;

import processing.core.PVector;

public class TwoDimensional {

    private TwoDimensional() {}

    public static float angleBetween(float x1, float y1, float x2, float y2) {
        double atan2 = Math.atan2(y1 - y2, x1 - x2);
        return (float) (atan2 < 0 ? Math.PI * 2 + atan2 : atan2);
    }

    public static void main(String[] args) {
        float x1, x2, y1, y2;
        x1 = 100;
        y1 = 300;
        x2 = 100;
        y2 = 200;
        System.out.println((angleBetween(x1, y1, x2, y2)));
        System.out.println((PVector.angleBetween(new PVector(x1, y1), new PVector(x2, y2))));

        x1 = 100;
        y1 = 200;
        x2 = 100;
        y2 = 300;
        System.out.println((angleBetween(x1, y1, x2, y2)));
        System.out.println((PVector.angleBetween(new PVector(x1, y1), new PVector(x2, y2))));
    }
}
