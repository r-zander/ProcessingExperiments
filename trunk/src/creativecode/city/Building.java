package creativecode.city;

import static creativecode.city.GenerativeCity.*;
import static processing.core.PApplet.*;
import processing.core.PShape;
import punktiert.math.Vec;
import punktiert.physics.BCollision;
import punktiert.physics.VParticle;

public class Building {

    final static int BACKGROUND = 0x5E232BFF;

    final static int STROKE     = 0xFF78CCFF;

    VParticle        particle;

    float            x, y;

    BuildingShape    buildingShape;

    PShape           shape;

    int              sizeX;

    int              sizeY;

    float            padding;

    public Building(float x, float y, float width, float height, float padding) {
        this.buildingShape = BuildingShapeFactory.getRandomBuildingShape();
        this.padding = padding;

//        if (buildingShape.maxArea > 1) {
//            float sizeX = $.random(1) * $.random(1) * $.random(1) * buildingShape.maxArea;
//            this.sizeY = round($.random(1) * buildingShape.maxArea / sizeX);
//            this.sizeX = round(sizeX);
//        } else {
        this.sizeX = 1;
        this.sizeY = 1;
//        }

        $.fill(BACKGROUND);
        $.stroke(STROKE);
        $.strokeWeight(1);
        shape = buildingShape.newShape(width - (2 * padding / sizeX), height - (2 * padding / sizeY));
        shape.scale(sizeX, sizeY);
        width *= sizeX;
        height *= sizeY;

        this.x = x + padding;
        this.y = y + padding;

        particle = new VParticle(new Vec(this.x + width / 2, this.y + height / 2), 0, max(width, height) / 2);
        particle.addBehavior(new BCollision());
        $.physics.addParticle(particle);
    }

    void draw() {
        $.shape(shape, x, y);
    }

    public void step() {}

}
