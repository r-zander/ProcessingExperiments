package util;

import processing.core.PApplet;
import processing.core.PGraphics;

public class Gradient {

    public class Axis {

        // @formatter:off
        public static final int
            X_AXIS = 0,
            Y_AXIS = 1;
        // @formatter:on
    }

    private PApplet parent;

    PGraphics       graphic;

    private int     x;

    private int     y;

    public Gradient(PApplet parent, int x, int y, int width, int height, int colorFrom, int colorTo, int axis) {

        this.parent = parent;
        this.x = x;
        this.y = y;

        graphic = parent.createGraphics(width, height);
        graphic.beginDraw();
        graphic.noFill();

        switch (axis) {
            case Axis.Y_AXIS: // Top to bottom gradient
                for (int i = 0; i <= 0 + height; i++) {
                    int c = graphic.lerpColor(colorFrom, colorTo, i / (float) height);
                    graphic.stroke(c);
                    graphic.line(0, i, width, i);
                }
                break;
            case Axis.X_AXIS: // Left to right gradient
                for (int i = 0; i <= 0 + width; i++) {
                    int c = graphic.lerpColor(colorFrom, colorTo, i / (float) width);
                    graphic.stroke(c);
                    graphic.line(i, 0, i, height);
                }
                break;
        }
        graphic.endDraw();
    }

    public void draw() {
        parent.image(graphic, x, y);
    }
}
