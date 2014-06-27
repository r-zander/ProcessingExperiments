package fractals.lyapunov;

import processing.core.PApplet;
import processing.core.PImage;
import processing.event.MouseEvent;
import util.ApproximationMath;
import util.structures.FloatRange;

public class LyapunovFractal extends PApplet {

    private static final long   serialVersionUID = -987763235416578283L;

    private static final String SEQUENCE         = "BBBBBBAAAAAA";                      // = "BBBBBBAAAAAA";

    private static final char[] SEQUENCE_ARRAY   = SEQUENCE.toUpperCase().toCharArray();

    private static class Section {

        public static FloatRange A;

        public static FloatRange B;

        static {
            reset();
        }

        public static void reset() {
//            A = new FloatRange(3, 4);
//            B = new FloatRange(3, 4);
            A = new FloatRange(3.4f, 4);
            B = new FloatRange(2.5f, 3.4f);
        }
    }

//    private static final float MIN_MULTIPLIER = 1;
//
//    private static final float MAX_MULTIPLIER = 3;

//    private static final float  MULTIPLIER_STEPS = .1f;

    private static final int ITERATIONS     = 60;

    int                      bgColor        = 0xFF000000;

    int                      animationFrame = 0;

    @Override
    public void setup() {
        size(400, 400);
//        size(displayWidth, displayHeight);
//        size(100, 100);
        background(bgColor);
//        frameRate(20);

        stroke(255);
        fill(255);
        colorMode(HSB);
//        noLoop();
    }

    @Override
    public void draw() {
        drawFractal();
        writeFrames();
        animationFrame++;

        if (keyPressed && key == ' ') {
            save(getClass().getSimpleName() + ".png");
        }
    }

    private void drawFractal() {
//        pushMatrix();
//        rotate(-HALF_PI);
//        translate(-width, 0);

        int iterations = ITERATIONS;
//        int iterations = round(loopMap(animationFrame, 0, 60, 5, 55));
//        float multiplier = loopMap(animationFrame, 0, 200, 0.5f, 1);
        int skippedIterations = 20;
//        double x0 = 0.5;
        double x0 = loopMap(animationFrame, 0, 3000, 0.1, 0.9);

        PImage fractal = new PImage(width, height);
        fractal.loadPixels();

        double minLambda = Double.POSITIVE_INFINITY;
        double maxLambda = Double.NEGATIVE_INFINITY;
        double[][] lambdas = new double[width][height];

        for (int x = 1; x < width; x++) {
            for (int y = 1; y < height; y++) {
                float a = map(x, 0, width, Section.A.min, Section.A.max);
                float b = map(y, 0, height, Section.B.min, Section.B.max);

                double xn = x0;
                double sum = 0;
                for (int i = 1; i < iterations; i++) {
                    float rn;
                    if (SEQUENCE_ARRAY[i % SEQUENCE_ARRAY.length] == 'A') {
                        rn = a;
                    } else {
                        rn = b;
                    }
                    xn = rn * xn * (1 - xn);
                    if (i > skippedIterations) {
                        sum += ApproximationMath.fasterlog((float) Math.abs(rn * (1 - 2 * xn)));
                    }
                }
                double lambda = (1.0 / ITERATIONS) * sum;
                if (lambda > maxLambda) {
                    maxLambda = lambda;
                }
                if (lambda < minLambda) {
                    minLambda = lambda;
                }
                lambdas[x][y] = lambda;
            }
        }
        for (int x = 1; x < width; x++) {
            for (int y = 1; y < height; y++) {
                int color;
                if (lambdas[x][y] <= 0) {
                    /*
                     * Order coloring
                     */
//                    color = lerpColor(0xffffff00, 0xff000000, (float) (lambdas[x][y] / minLambda));
                    color = color(255 * (float) (lambdas[x][y] / minLambda), 255, 255);
                } else {
                    /*
                     * Chaos coloring
                     */
//                    color = lerpColor(0xff000000, 0xffffffff, (float) (lambdas[x][y] / maxLambda));
//                    color = color(255 * (float) (lambdas[x][y] / maxLambda));
                    color = 0xff000000;
                }
                fractal.set(x, y, color);
            }
        }
        fractal.updatePixels();
        image(fractal, 0, 0);
//        popMatrix();
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        super.mouseClicked(event);

        if (event.getButton() == RIGHT) {
            Section.reset();
            return;
        }

        float aDistance = Section.A.distance() / 4;
        float bDistance = Section.B.distance() / 4;

        float ax = map(event.getX(), 0, width, Section.A.min, Section.A.max);
        Section.A.min = ax - aDistance;
        Section.A.max = ax + aDistance;

        float bx = map(event.getY(), 0, height, Section.B.min, Section.B.max);
        Section.B.min = bx - bDistance;
        Section.B.max = bx + bDistance;

    }

    private static float[] loopMap(int value, int maxValue, float[] minReturn, float[] maxReturn) {
        float[] returnValues = new float[minReturn.length];
        for (int i = 0; i < minReturn.length; i++) {
            returnValues[i] = loopMap(value, 0, maxValue, minReturn[i], maxReturn[i]);
        }
        return returnValues;
    }

    private static double[] loopMap(int value, int maxValue, double[] minReturn, double[] maxReturn) {
        double[] returnValues = new double[minReturn.length];
        for (int i = 0; i < minReturn.length; i++) {
            returnValues[i] = loopMap(value, 0, maxValue, minReturn[i], maxReturn[i]);
        }
        return returnValues;
    }

    private static float loopMap(float value, float minValue, float maxValue, float minReturn, float maxReturn) {
        float returnValue = map(value % maxValue, minValue, maxValue, minReturn, maxReturn);
        if (value / maxValue % 2 >= 1) {
            /*
             * Backward loop
             */
            return maxReturn - returnValue + minReturn;
        }

        return returnValue;
    }

    private static double loopMap(double value, double minValue, double maxValue, double minReturn, double maxReturn) {
        double returnValue = map(value % maxValue, minValue, maxValue, minReturn, maxReturn);
        if (value / maxValue % 2 >= 1) {
            /*
             * Backward loop
             */
            return maxReturn - returnValue + minReturn;
        }

        return returnValue;
    }

    private void writeFrames() {
        int x2 = width;
        int x1 = x2 - 120;
        int y2 = height;
        int y1 = y2 - 20;
        fill(0, 0, 0);
        noStroke();
        rect(x1, y1, x2, y2);
        fill(192, 192, 192);
        textSize(15);
//      textAlign(CENTER, CENTER);
        text(String.format("%d @ %.1f fps", animationFrame, frameRate), x1, y1, x2, y2);
    }

    static public double map(double value, double start1, double stop1, double start2, double stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", LyapunovFractal.class.getName() });
    }
}