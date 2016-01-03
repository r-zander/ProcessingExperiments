package animations;

import processing.core.PApplet;

public class WhitneyChromatic extends PApplet {

    private static final long serialVersionUID = -56589606646834162L;

    private static class Ball {

        private float hue;

        private float centerDistance;

        private float size;

        private int   speed;

    }

    private static enum Mode {
        BALLS,
        LINES,
        BALLS_AND_LINES;
    }

    private static final Mode    MODE                = Mode.BALLS;

    private static final int     NUMBER_OF_ELEMENTS  = 36;

    private static final int     MIN_HUE             = 0;

    private static final int     MAX_HUE             = 240;

    private static final float   MIN_SIZE            = 7;

    private static final float   MAX_SIZE            = 30;

    private static final int     BACKGROUND_COLOR    = 0;

    private static final boolean RERENDER_BACKGROUND = true;

    /**
     * In seconds.
     */
    private static final int     FULL_DURATION       = 120;

    private float                centerX;

    private float                centerY;

    private Ball[]               balls               = new Ball[NUMBER_OF_ELEMENTS];

    private int                  counter             = 0;

    @Override
    public boolean sketchFullScreen() {
        return true;
    }

    @Override
    public void setup() {
        size(displayWidth, displayHeight);

        colorMode(HSB);
        background(BACKGROUND_COLOR);

        centerX = width / 2;
        centerY = height / 2;

        float radius = min(height, width) / 2 * .95f;
        for (int ballIndex = 0; ballIndex < NUMBER_OF_ELEMENTS; ballIndex++) {
            Ball ball = new Ball();
            balls[ballIndex] = ball;

            ball.hue = getBallValue(MIN_HUE, MAX_HUE, ballIndex);
            ball.centerDistance = getBallValue(0, radius, ballIndex);
            ball.size = getBallValue(MIN_SIZE, MAX_SIZE, ballIndex);
            ball.speed = NUMBER_OF_ELEMENTS - ballIndex;
        }
    }

    @Override
    public void draw() {
        if (keyPressed) {
            save("export.jpg");
        }

        if (mousePressed) {
            counter--;
        } else {
            counter++;
        }

        if (RERENDER_BACKGROUND) {
            background(BACKGROUND_COLOR);
            noStroke();
            fill(BACKGROUND_COLOR, 20);
            rect(0, 0, width, height);
        }

        for (int ballIndex = 0; ballIndex < NUMBER_OF_ELEMENTS; ballIndex++) {

            float angle = 2 * PI / FULL_DURATION * (counter / 60f) * balls[ballIndex].speed;

            switch (MODE) {
                case BALLS:
                    drawBall(balls[ballIndex], angle);
                    break;
                case LINES:
                    drawLine(balls[ballIndex], angle);
                    break;
                case BALLS_AND_LINES:
                    drawBall(balls[ballIndex], angle);
                    drawLine(balls[ballIndex], angle);
                    break;

            }

        }
    }

    private void drawBall(Ball ball, float angle) {
        noStroke();
        fill(ball.hue, 255, 255);
        ellipse(
                centerX + cos(angle) * ball.centerDistance,
                centerY + sin(angle) * ball.centerDistance,
                ball.size,
                ball.size);
    }

    private void drawLine(Ball ball, float angle) {
        stroke(ball.hue, 255, 255);
        strokeWeight(10);
        line(centerX, centerY, centerX + cos(angle) * ball.centerDistance, centerY + sin(angle) * ball.centerDistance);
    }

    private float getBallValue(float minValue, float maxValue, int ballIndex) {
        return minValue + (maxValue - minValue) / NUMBER_OF_ELEMENTS * ballIndex;
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", WhitneyChromatic.class.getName() });
    }
}