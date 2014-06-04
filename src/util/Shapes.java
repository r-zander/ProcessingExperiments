package util;

import static processing.core.PApplet.cos;
import static processing.core.PApplet.map;
import static processing.core.PApplet.sin;
import static processing.core.PConstants.CLOSE;
import static processing.core.PConstants.TWO_PI;
import processing.core.PApplet;

import com.koniaris.astronomy.Moon;

public class Shapes {

    private Shapes() {}

    public static void star(PApplet parent, float x, float y, float innerRadius, float outerRadius, int points) {
        float angle = TWO_PI / points;
        float halfAngle = angle / 2.0f;
        parent.beginShape();
        for (float a = 0; a < TWO_PI; a += angle) {
            float sx = x + cos(a) * outerRadius;
            float sy = y + sin(a) * outerRadius;
            parent.vertex(sx, sy);
            sx = x + cos(a + halfAngle) * innerRadius;
            sy = y + sin(a + halfAngle) * innerRadius;
            parent.vertex(sx, sy);
        }
        parent.endShape(CLOSE);
    }

    public static void sun(PApplet parent, float x, float y, float radius) {
//      Shapes.star(this, x, centerY, sunRadius / 2f, sunRadius, 8);
        float height = radius / 2;
        float eigthPi = TWO_PI / 8;
        float twentyFourthPi = TWO_PI / 24;
        for (int i = 0; i < 8; i++) {
            int threeI = i * 3;
            parent.triangle(x + cos(eigthPi * i) * radius, y + sin(eigthPi * i) * radius, x
                    + cos(twentyFourthPi * (threeI + 1)) * height, y + sin(twentyFourthPi * (threeI + 1)) * height, x
                    + cos(twentyFourthPi * (threeI - 1)) * height, y + sin(twentyFourthPi * (threeI - 1)) * height);
        }
        float innerWidth = radius * .8f;
        parent.ellipse(x, y, innerWidth, innerWidth);
    }

    public static void moon(PApplet parent, float x, float y, float radius) {
//        parent.arc(x, y, radius * 2, radius * 2, -HALF_PI, HALF_PI, CHORD);
        try {
            Moon moon = new Moon();
            moon(parent, x, y, radius, moon.illuminatedFraction(), moon.isWaning());
        } catch (Exception e) {
            /*
             * render moon with default values.
             */
            moon(parent, x, y, radius, 0.75, false);
        }
    }

    public static void moon(PApplet parent, float x, float y, float radius, double illuminatedFraction, boolean waning) {
        parent.beginShape();
        float topY = y - radius;
        float bottomY = y + radius;
        parent.vertex(x, bottomY);
        float controlX = x + (waning ? -1.37f : 1.37f) * radius;
        parent.bezierVertex(controlX, bottomY, controlX, topY, x, topY);

        float controlFactor = map((float) illuminatedFraction, 0, 1, 1.37f, -1.37f);
        controlX = x + (waning ? -controlFactor : controlFactor) * radius;
        parent.bezierVertex(controlX, topY, controlX, bottomY, x, bottomY);
        parent.endShape();
    }

}
