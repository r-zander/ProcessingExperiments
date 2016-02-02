package generativeLogo;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

public class ChaosStar extends PApplet {

    private static final int   MAX_RANGE          = 300;

    private static final int   MIN_RANGE          = 100;

    private static final float MAX_ANGLE          = radians(135);

    private static final float MIN_ANGLE          = radians(45);

    int                        bgColor            = 0xFF000000;

    int                        hue                = (int) random(255);

    int                        color;

    List<Point>                points             = new ArrayList<ChaosStar.Point>();

    Point                      lastPoint;

    float                      lastAngle;

    boolean                    foundMatchingPoint = false;

    class Point {

        float x;

        float y;

        public Point(float x, float y) {
            if (x > width || x < 0) {
                x = lastPoint.x - (x - lastPoint.x);
            }
            if (y > height || y < 0) {
                y = lastPoint.y - (y - lastPoint.y);
            }

            this.x = x;
            this.y = y;

            ellipse(x, y, 10, 10);
        }

    }

    @Override
    public void setup() {
        size(displayWidth, displayHeight);
        background(bgColor);
        frameRate(5);
        colorMode(HSB);
        color = color(hue, 255, 255);
        fill(color);
        stroke(color);
        strokeWeight(4);

        float radius = 200;
        float angle = random(TWO_PI);
        angle = PI;
        Point startPoint = new Point(width / 2 + cos(angle) * radius, height / 2 + sin(angle) * radius);
        line(width / 2, height / 2, startPoint.x, startPoint.y);
        points.add(startPoint);
        lastPoint = startPoint;
        lastAngle = angle + PI * 1.5f;
    }

    @Override
    public void draw() {
//        fill(bgColor, 5);
//        noStroke();
//        rect(0, 0, width, height);
//        strokeWeight(4);

//        if (foundMatchingPoint) {
//            frameRate(1);
//        }

        hue += random(-5, 5);
        if (hue < 0) {
            hue = 255 + hue;
        } else if (hue > 255) {
            hue -= 255;
        }
        color = color(hue, 255, 255);
        fill(color);
        stroke(color);

        /*
         * Find matching point.
         */
        float range = 0;
        float angle = 0;
//        for (Point point : points) {
//            range = dist(point.x, point.y, lastPoint.x, lastPoint.y);
//            if (range >= MIN_RANGE && range <= MAX_RANGE) {
//                angle = atan2(point.y - lastPoint.y, point.x - lastPoint.y) + PI;
//                if (angle >= MIN_ANGLE && angle <= MAX_ANGLE) {
//                    foundMatchingPoint = true;
//                    break;
//                }
//            }
//        }
//
//        if (!foundMatchingPoint) {
        angle = lastAngle + random(MIN_ANGLE, MAX_ANGLE);
        angle %= TWO_PI;
        range = random(MIN_RANGE, MAX_RANGE);
//        }

        Point point = new Point(lastPoint.x + cos(angle) * range, lastPoint.y + sin(angle) * range);

        line(lastPoint.x, lastPoint.y, point.x, point.y);

        points.add(point);
        lastPoint = point;
        lastAngle = angle;

        if (keyPressed && key == ' ') {
            save(getClass().getSimpleName() + ".png");
        }
    }

    float angle(Point v1, Point v2) {
        float a = atan2(v2.y, v2.x) - atan2(v1.y, v1.x);
        if (a < 0)
            a += TWO_PI;
        return a;
    }

    public static void main(String[] args) {
        PApplet.main(new String[] { "--present", ChaosStar.class.getName() });
    }
}