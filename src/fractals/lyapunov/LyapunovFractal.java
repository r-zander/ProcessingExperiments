package fractals.lyapunov;

import processing.core.PApplet;

public class LyapunovFractal extends PApplet {

    private static final long   serialVersionUID = -987763235416578283L;

    private static final String SEQUENCE         = "BBBBBBAAAAAA";

    private static final char[] SEQUENCE_ARRAY   = SEQUENCE.toUpperCase().toCharArray();

    private static final float  A_MIN            = 3.4f;

    private static final float  A_MAX            = 4f;

    private static final float  B_MIN            = 2.5f;

    private static final float  B_MAX            = 3.4f;

    private static final int    ITERATIONS       = 80;

    int                         bgColor          = 0xFF000000;

    @Override
    public void setup() {
        size(800, 800);
        background(bgColor);
        frameRate(5);

        stroke(255);
        fill(255);
    }

    private void drawFractal() {
        for (int x = 1; x < width; x++) {
            for (int y = 1; y < height; y++) {
//                float a = map(x, 0, width, A_MIN, A_MAX);
//                float b = map(y, 0, height, B_MIN, B_MAX);

                float a = 2;
                float b = 3;

                double xn = 0.5;
                double sum = 0;
                for (int i = 1; i < ITERATIONS; i++) {
                    float rn;
                    if (SEQUENCE_ARRAY[i % SEQUENCE_ARRAY.length] == 'A') {
                        rn = a;
                    } else {
                        rn = b;
                    }
                    xn = rn * xn * (1 - xn);
//                    sum += fastLog(Math.abs(rn * (1 - 2 * xn)));
                }
                double lamda = (1.0 / ITERATIONS) * sum;
//                int color;
//                color = lerpColor(0xffffff00, 0xff0000ff, (float) map(lamda, -1.5, 0.6, 0, 1));
//                stroke(color);
//                point(x, y);
            }
        }
    }

    @Override
    public void draw() {
        drawFractal();
    }

    static public double map(double value, double start1, double stop1, double start2, double stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }

    public static double fastLog(double x) {
//        return 6 * (x - 1) / (x + 1 + 4 * (Math.sqrt(x)));
        return Math.log(x);
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", LyapunovFractal.class.getName() });
    }
}