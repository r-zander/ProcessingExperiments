package timevisualization;

import static util.Numbers.SQRT_2;
import processing.core.PApplet;
import util.Axis;
import util.Colors;

public class OrbitClock extends PApplet {

    private static final long serialVersionUID = -56589606646834162L;

    private static final int  STEPS            = 120;

    private float             a;

    private float             centerX;

    private float             centerY;

    @Override
    public void setup() {
        size(displayWidth, displayHeight);
        background(192);
//        Colors.drawGradient(this, 0, 0, width, height, color(255, 192, 0), color(0, 0, 192), Axis.X_AXIS);

        a = width * .30f;
        centerX = width / 2;
        centerY = height / 2;

    }

    @Override
    public void draw() {
        float angle = hour2angle(hour(), minute());
        background(192);
        Colors.drawGradient(this, 0, 0, width, height, color(255, 192, 0), color(0, 0, 192), Axis.X_AXIS);
        float lastX = -1;
        float lastY = -1;
        for (int currentStep = 0; currentStep < STEPS; currentStep++) {
            float sin = sin(angle);
            float divisor = sq(sin) + 1;
            float dividend = a * SQRT_2 * cos(angle);
            float x = centerX + dividend / divisor;
            float y = centerY + (dividend * sin) / divisor;

            if (lastX >= 0) {
                setStroke(currentStep, STEPS);
                strokeWeight(10);
                line(lastX, lastY, x, y);
            } else {
                drawMinutes(x, y);
            }

            lastX = x;
            lastY = y;
            angle += TWO_PI / STEPS;
        }
        fill(0);
        text(String.format("%.1f / %.1f", frameRate, frameRateTarget), width - 100, height - 100);
    }

    private void setStroke(float currentStep, float steps) {
//        stroke(0, 255 * (1 - currentStep / steps));
        stroke(255 * (1 - currentStep / steps), 255);
    }

    private static final float MINUTES_RADIUS     = 200;

    private static final float MINUTES_STEPS      = 60;

    private static final float MINUTE_ANGLE_STEPS = TWO_PI / MINUTES_STEPS;

    private void drawMinutes(float x, float y) {
        float startAngle = minute2angle(minute(), second());

        noFill();
        strokeWeight(10);
        for (int i = 0; i < MINUTES_STEPS; i++) {
            float angle = startAngle + MINUTE_ANGLE_STEPS * i;
            setStroke(i, MINUTES_STEPS);
            arc(x, y, MINUTES_RADIUS, MINUTES_RADIUS, angle, angle + MINUTE_ANGLE_STEPS);
        }
        drawSeconds(x + cos(startAngle) * (MINUTES_RADIUS / 2), y + sin(startAngle) * (MINUTES_RADIUS / 2));
    }

    private static final float SECONDS_RADIUS      = 60;

    private static final float SECONDS_STEPS       = 60;

    private static final float SECONDS_ANGLE_STEPS = TWO_PI / SECONDS_STEPS;

    private void drawSeconds(float x, float y) {
        float startAngle = seconds2angle(second());

        noFill();
        strokeWeight(2);
        for (int i = 0; i < SECONDS_STEPS; i++) {
            float angle = startAngle + SECONDS_ANGLE_STEPS * i;
            setStroke(i, SECONDS_STEPS);
            arc(x, y, SECONDS_RADIUS, SECONDS_RADIUS, angle, angle + MINUTE_ANGLE_STEPS);
        }
        drawMilliseconds(x + cos(startAngle) * (SECONDS_RADIUS / 2), y + sin(startAngle) * (SECONDS_RADIUS / 2));
    }

    private static final float MILLIS_RADIUS      = 30;

    private static final float MILLIS_STEPS       = 100;

    private static final float MILLIS_ANGLE_STEPS = TWO_PI / MILLIS_STEPS;

    private void drawMilliseconds(float x, float y) {
        float startAngle = millis2angle(millis() % 1000);

        noFill();
        strokeWeight(1);
        for (int i = 0; i < MILLIS_STEPS; i++) {
            float angle = startAngle + MILLIS_ANGLE_STEPS * i;
            setStroke(i, MILLIS_STEPS);
            arc(x, y, MILLIS_RADIUS, MILLIS_RADIUS, angle, angle + MILLIS_ANGLE_STEPS);
        }
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
        return -TWO_PI * (hours / 24f + minutes / (24f * 60));
    }

    private static float minute2angle(int minutes, int seconds) {
        return map(minutes + seconds / 60f, 0, 60, PI * -.5f, PI * 1.5f);
    }

    private static float seconds2angle(int seconds) {
        return map(seconds, 0, 60, PI * -.5f, PI * 1.5f);
    }

    private static float millis2angle(int millis) {
        return map(millis, 0, 1000, PI * -.5f, PI * 1.5f);
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", OrbitClock.class.getName() });
    }
}