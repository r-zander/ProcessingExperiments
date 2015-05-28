package creativecode.city;

import static creativecode.city.GenerativeCity.*;
import processing.core.PShape;
import fisica.FBox;

public class Building {

    FBox             box;

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
        shape = BuildingShapeFactory.newShape(width, height);

        box = new FBox(width, height);
        box.setDrawable(false);

        $.world.add(box);
    }

    void draw() {
        $.shape(shape, x, y);
    }
}
