package timevisualization;

import processing.core.PApplet;

public class SpiralClock extends PApplet {

    private static final long  serialVersionUID = -56589606646834162L;

    private static final float BG_COLOR         = 128f;

    private float              centerX;

    private float              centerY;

    private class Spiral {

        /*
         * Config values
         */
        private float       maxRadius      = width * .4f;

        private float       minRadius      = maxRadius * .1f;

        private final float MAX_LINE_WIDTH = 10;

        private final float MIN_LINE_WIDTH = 10;

        private final int   LOOPS          = 3;

        /*
         * Calculated values
         */
        private final int   STEPS          = round(TWO_PI * maxRadius * LOOPS / MAX_LINE_WIDTH);

        private final float ANGLE_PER_STEP = TWO_PI / STEPS * LOOPS;

        public void draw() {
            fill(0);
            noStroke();
            for (int step = 0; step < STEPS; step++) {
                float angle = ANGLE_PER_STEP * step;
                float distance = map(step, 0, STEPS, maxRadius, minRadius);
                float x = centerX + cos(angle) * distance;
                float y = centerY + sin(angle) * distance;
                float lineWidth = map(step, 0, STEPS, MAX_LINE_WIDTH, MIN_LINE_WIDTH);

                ellipse(x, y, lineWidth, lineWidth);
            }

        }
    }

    private Spiral spiral;

    @Override
    public void setup() {
        size(800, 800);
        background(BG_COLOR);

        centerX = width / 2f;
        centerY = height / 2f;

        spiral = new Spiral();
        spiral.draw();
    }

    @Override
    public void draw() {}

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", SpiralClock.class.getName() });
    }
}