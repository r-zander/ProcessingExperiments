package timevisualization.orbitclock;

import static processing.core.PApplet.*;
import static processing.core.PConstants.*;
import static timevisualization.orbitclock.OrbitClock.*;

abstract class TimeShape {

    public static class MinutesShape extends TimeShape {

        @Override
        protected float getStartAngle() {
            return map(minute() + second() / 60f, 0, 60, PI * -.5f, PI * 1.5f);
        }
    }

    public static class SecondsShape extends TimeShape {

        @Override
        protected float getStartAngle() {
            return map(second(), 0, 60, PI * -.5f, PI * 1.5f);
        }
    }

    public static class MillisShape extends TimeShape {

        @Override
        protected float getStartAngle() {
            return map(System.currentTimeMillis() % 1000, 0, 1000, PI * -.5f, PI * 1.5f);
        }
    }

    float radius;

    int   steps;

    float width;

    int   mode;

    public void draw(float x, float y) {
        float startAngle = getStartAngle();
        float angleSteps = OrbitClock.TWO_PI / steps;
        switch (mode) {
            case OrbMode.ORB:
                $.setFill(steps, steps);
                $.noStroke();
                $.ellipse(
                        x + OrbitClock.cos(startAngle) * radius,
                        y + OrbitClock.sin(startAngle) * radius,
                        10 * width,
                        10 * width);
                break;
            case OrbMode.ORBIT:
                $.noFill();
                $.strokeWeight(width);
                for (int i = 0; i < steps; i++) {
                    float angle = startAngle - angleSteps * i;
                    $.setStroke(i, steps);
                    $.arc(x, y, radius * 2, radius * 2, angle, angle + angleSteps);
                }
        }
    }

    protected abstract float getStartAngle();
}