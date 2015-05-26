package creativecode.city;

import static creativecode.city.GenerativeCity.*;
import static processing.core.PConstants.*;
import processing.core.PShape;
import util.Numbers;

public class Building {

    PShape           shape;

    float            x, y;

    final static int BACKGROUND = 0x5E232BFF;

    final static int STROKE     = 0xFF78CCFF;

    public Building(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;

        $.fill(BACKGROUND);
        $.stroke(STROKE);
        $.strokeWeight(1);
        shape = createShape(width, height);
    }

    void draw() {
        $.shape(shape, x, y);
    }

    PShape createShape(float width, float height) {
        switch (Numbers.random(1, 2)) {
            case 1:
                PShape shape = $.createShape();
                shape.beginShape();
                shape.vertex(0, 0);
                shape.vertex(width, 0);
                shape.vertex(width, height);
                float notchWidth = width * $.random(0.1f, 0.7f);
                shape.vertex(notchWidth, height);
                float notchHeight = height * $.random(0.3f, 0.9f);
                shape.vertex(notchWidth, notchHeight);
                shape.vertex(0, notchHeight);
                shape.endShape(CLOSE);
                return shape;
            default:
                return $.createShape(RECT, 0, 0, width, height);
        }
    }
}
