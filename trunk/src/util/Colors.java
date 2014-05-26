package util;

import processing.core.PApplet;

public class Colors {

    public static final int RED = 0xFFFF0000;

    public static int randomColor() {
        return Numbers.random(0xFF000000, 0xFFFFFFFF);
    }

    public static void drawGradient(PApplet parent, int x, int y, float width, float height, int colorFrom,
            int colorTo, Axis axis) {

        parent.noFill();

        switch (axis) {
            case Y_AXIS: // Top to bottom gradient
                for (int i = y; i <= y + height; i++) {
                    float inter = PApplet.map(i, y, y + height, 0, 1);
                    int c = parent.lerpColor(colorFrom, colorTo, inter);
                    parent.stroke(c);
                    parent.line(x, i, x + width, i);
                }
                break;
            case X_AXIS: // Left to right gradient
                for (int i = x; i <= x + width; i++) {
                    float inter = PApplet.map(i, x, x + width, 0, 1);
                    int c = parent.lerpColor(colorFrom, colorTo, inter);
                    parent.stroke(c);
                    parent.line(i, y, i, y + height);
                }
                break;
        }
    }

}
