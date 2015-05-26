package creativecode.city;

import static creativecode.city.GenerativeCity.*;
import static processing.core.PConstants.*;
import processing.core.PShape;

public class Building {

    PShape           shape;

    float            x, y;

    final static int BACKGROUND = 0x5E232BFF;

    final static int STROKE     = 0x0078CCFF;

    public Building(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;

        shape = $.createShape(RECT, 0, 0, width, height);
    }

    void draw() {
        $.fill(BACKGROUND);
        $.stroke(STROKE);
        $.shape(shape, x, y);
    }
}
