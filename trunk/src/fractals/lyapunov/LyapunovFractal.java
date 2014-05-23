package fractals.lyapunov;

import processing.core.PApplet;

public class LyapunovFractal extends PApplet {

    private static final long   serialVersionUID = -987763235416578283L;

    private static final String SEQUENCE         = "ba";

    private static final char[] SEQUENCE_ARRAY   = SEQUENCE.toUpperCase().toCharArray();

    private static final float  A_MIN            = 0;

    private static final float  A_MAX            = 4;

    private static final float  B_MIN            = 0;

    private static final float  B_MAX            = 4;

    private static final int    ITERATIONS       = 80;

    int                         bgColor          = 0xFF000000;

    @Override
    public void setup() {
        size(800, 800);
        background(bgColor);
        frameRate(5);

        stroke(255);
        fill(255);
        drawFractal();
    }

    private void drawFractal() {
        for (int x = 1; x < width; x++) {
            for (int y = 1; y < height; y++) {
                float a = map(x, 0, width, A_MIN, A_MAX);
                float b = map(y, 0, height, A_MIN, A_MAX);
                for (int i = 0; i < ITERATIONS; i++) {
                    float rn;
                    if (SEQUENCE_ARRAY[i % SEQUENCE_ARRAY.length] == 'A') {
                        rn = a;
                    } else {
                        rn = b;
                    }
//                    x_{n+1} = r_n x_n (1 - x_n)
                }
            }
        }
    }

    @Override
    public void draw() {}

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", LyapunovFractal.class.getName() });
    }
}