package timevisualization.orbitclock;

import processing.core.PApplet;
import util.Axis;
import util.Colors;
import util.TwoDimensional;
import util.structures.Point;

public class OrbitClock extends PApplet {

    private static final long serialVersionUID = -56589606646834162L;

    private static enum ColorMode {
        RANDOM,
        BLACK_WHITE,
        BLACK_ALPHA,
        YELLOW_BLUE;
    }

    private static final ColorMode COLOR_MODE = ColorMode.YELLOW_BLUE;

    private CurveFunction          function;

    private static final int       STEPS      = 240;

    private static int             YELLOW;

    private static int             BLUE;

    private float                  centerX;

    private float                  centerY;

    private static enum Mode {
        ORB,
        ORBIT;
    }

    private abstract class TimeShape {

        float radius;

        int   steps;

        float width;

        Mode  mode;

        public void draw(float x, float y) {
            float startAngle = getStartAngle();
            float angleSteps = TWO_PI / steps;
            switch (mode) {
                case ORB:
                    setFill(steps, steps);
                    noStroke();
                    ellipse(x + cos(startAngle) * radius, y + sin(startAngle) * radius, 10 * width, 10 * width);
                    break;
                case ORBIT:
                    noFill();
                    strokeWeight(width);
                    for (int i = 0; i < steps; i++) {
                        float angle = startAngle - angleSteps * i;
                        setStroke(i, steps);
                        arc(x, y, radius * 2, radius * 2, angle, angle + angleSteps);
                    }
            }
        }

        protected abstract float getStartAngle();
    }

    private TimeShape minutesShape;

    private TimeShape secondsShape;

    private TimeShape millisShape;

    @Override
    public void setup() {
        size(displayWidth, displayHeight);

        centerX = width / 2;
        centerY = height / 2;

        function = new LemniscateOfBernoulli(width * .30f);
//        function = new Hippopede(width * .2f, 1f, 1f);

        YELLOW = color(255, 192, 0);
        BLUE = color(0, 0, 192);

        strokeCap(SQUARE);

        minutesShape = new TimeShape() {

            @Override
            protected float getStartAngle() {
                return map(minute() + second() / 60f, 0, 60, PI * -.5f, PI * 1.5f);
            }
        };
        minutesShape.radius = width * .05f;
        minutesShape.steps = 60;
        minutesShape.width = 10;
        minutesShape.mode = Mode.ORBIT;

        secondsShape = new TimeShape() {

            @Override
            protected float getStartAngle() {
                return map(second(), 0, 60, PI * -.5f, PI * 1.5f);
            }
        };
        secondsShape.radius = minutesShape.radius * .5f;
        secondsShape.steps = 60;
        secondsShape.width = minutesShape.width * .2f;
        secondsShape.mode = Mode.ORBIT;

        millisShape = new TimeShape() {

            @Override
            protected float getStartAngle() {
                return map(System.currentTimeMillis() % 1000, 0, 1000, PI * -.5f, PI * 1.5f);
            }
        };
        millisShape.radius = secondsShape.radius * .4f;
        millisShape.steps = 100;
        millisShape.width = secondsShape.width * .5f;
        millisShape.mode = Mode.ORBIT;
    }

    @Override
    public void draw() {
        drawBackground();
        drawLemniscate();
        drawFPS();
    }

    private void drawBackground() {
        switch (COLOR_MODE) {
            case RANDOM:
                for (int i = 0; i <= 0 + width; i++) {
                    stroke(color(random(255), random(255), random(255)));
                    line(i, 0, i, height);
                }
                stroke(color(random(255), random(255), random(255)));
                fill(color(random(255), random(255), random(255)));
                break;
            case BLACK_ALPHA:
            case BLACK_WHITE:
                Colors.drawGradient(this, 0, 0, width, height, YELLOW, BLUE, Axis.X_AXIS);
                noFill();
                stroke(0);
                break;
            case YELLOW_BLUE:
                background(255);
                noFill();
                stroke(0);
                break;

        }
        strokeWeight(3);
//        Shapes.sun(this, centerX - a, centerY, width * .1f);
//        Shapes.moon(this, centerX + a, centerY, width * .05f);
    }

    private void drawLemniscate() {
        noSmooth();
        float angle;
        switch (COLOR_MODE) {
            case YELLOW_BLUE:
                angle = PI;
                break;
            default:
                angle = hour2angle(hour(), minute());

        }
        Point startPoint = function.calculate(angle);
        float startX = centerX + startPoint.x;
        float startY = centerY + startPoint.y;
        float lastX = startX;
        float lastY = startY;
        float lastX1 = -1;
        float lastY1 = -1;
        float lastX2 = -1;
        float lastY2 = -1;
        for (int currentStep = 0; currentStep <= STEPS; currentStep++) {
            angle += TWO_PI / STEPS;
            Point point = function.calculate(angle);
            float x = centerX + point.x;
            float y = centerY + point.y;

            float angleBetween = TwoDimensional.angleBetween(lastX, lastY, x, y) + HALF_PI;
            noStroke();

            switch (COLOR_MODE) {
//                case YELLOW_BLUE:
//                    float halfSteps = steps / 2f;
//                    if (currentStep > halfSteps) {
//                        currentStep = steps - currentStep;
//                    }
//                    fill(lerpColor(YELLOW, BLUE, currentStep / halfSteps));
                default:
                    setFill(currentStep, STEPS);
            }

            int lineWidth = 30;
            float distX = cos(angleBetween) * lineWidth;
            float distY = sin(angleBetween) * lineWidth;
            float x1 = x - distX;
            float y1 = y - distY;
            float x2 = x + distX;
            float y2 = y + distY;
            if (lastX1 > -1) {
                quad(lastX1, lastY1, x1, y1, x2, y2, lastX2, lastY2);
//                break;
            }
//            line(x1, y1,x2 , y2);
            lastX1 = x1;
            lastY1 = y1;
            lastX2 = x2;
            lastY2 = y2;

            lastX = x;
            lastY = y;
        }
        switch (COLOR_MODE) {
            case YELLOW_BLUE:
                angle = hour2angle(hour(), minute());
                Point point = function.calculate(angle);
                startX = centerX + point.x;
                startY = centerY + point.y;
                break;
            default:
                break;
        }
        smooth();
        drawMinutes(startX, startY);
    }

    private void drawFPS() {
        if (mousePressed) {
            fill(0);
            text(String.format("%.1f / %.1f", frameRate, frameRateTarget), width - 100, height - 100);
        }
    }

    private void setStroke(float currentStep, float steps) {
        switch (COLOR_MODE) {
            case RANDOM:
                stroke(color(random(255), random(255), random(255)));
                break;
            case BLACK_WHITE:
                stroke(255 * (currentStep / steps), 255);
                break;
            case YELLOW_BLUE:
            case BLACK_ALPHA:
                stroke(0, 255 * (1 - currentStep / steps));
                break;
        }
    }

    private void setFill(float currentStep, float steps) {
        switch (COLOR_MODE) {
            case RANDOM:
                fill(color(random(255), random(255), random(255)));
                break;
            case BLACK_WHITE:
                fill(255 * (currentStep / steps), 255);
                break;
            case BLACK_ALPHA:
            default:
                fill(0, 255 * (1 - currentStep / steps));
                break;
        }
    }

    private void drawMinutes(float x, float y) {
        minutesShape.draw(x, y);
        drawSeconds(x + cos(minutesShape.getStartAngle()) * minutesShape.radius, y + sin(minutesShape.getStartAngle())
                * minutesShape.radius);
    }

    private void drawSeconds(float x, float y) {
        secondsShape.draw(x, y);
        drawMillis(x + cos(secondsShape.getStartAngle()) * secondsShape.radius, y + sin(secondsShape.getStartAngle())
                * secondsShape.radius);
    }

    private void drawMillis(float x, float y) {
        millisShape.draw(x, y);
    }

    /**
     * 12:00 = PI
     * 24:00 = 0 = TWO_PI
     * 6:00 = -PI/2 = 1.5 * PI
     * 18:00 = PI/2
     * 
     * @param hours
     * @param minutes
     * @return
     */
    private static float hour2angle(int hours, int minutes) {
        return map(hours + minutes / 60f, 0, 24, TWO_PI, 0);
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", OrbitClock.class.getName() });
    }
}