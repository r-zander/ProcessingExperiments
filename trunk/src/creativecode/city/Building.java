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

    PShape           shape;

    int              sizeX;

    int              sizeY;

    public Building(float x, float y, float width, float height, float padding) {
        $.fill(BACKGROUND);
        $.stroke(STROKE);
        $.strokeWeight(1);
        shape = BuildingShapeFactory.newShape(width, height);

        particle = new VParticle(new Vec(x + width / 2, y + height / 2), 0, max(width, height) / 2);
        particle.addBehavior(new BCollision());
        $.physics.addParticle(particle);
    }

    void draw() {
        $.shape(shape, particle.x - particle.radius, particle.y - particle.radius);
    }

    public void step() {}

}
