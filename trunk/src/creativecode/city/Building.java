package creativecode.city;

import static creativecode.city.GenerativeCity.*;
import static processing.core.PApplet.*;
import static processing.core.PConstants.*;
import processing.core.PShape;
import punktiert.math.Vec;
import punktiert.physics.BSeparate;
import punktiert.physics.VParticle;
import punktiert.physics.VSpring;

public class Building {

    VParticle        particle;

    PShape           shape;

    final static int BACKGROUND = 0x5E232BFF;

    final static int STROKE     = 0xFF78CCFF;

    int              steps;

    public Building(float x, float y, float width, float height) {
        $.fill(BACKGROUND);
        $.stroke(STROKE);
        $.strokeWeight(1);
        shape = BuildingShapeFactory.newShape(width, height);

        particle = new VParticle(new Vec(x + width / 2, y + height / 2), 0, max(width, height) / 2);
//        particle.addBehavior(new BCollision());
        particle.addBehavior(new BSeparate(particle.radius));
        $.physics.addParticle(particle);
        spawnCar();
    }

    void draw() {
//        $.shape(shape, particle.x, particle.y);
        $.ellipseMode(RADIUS);
        $.fill(BACKGROUND);
        $.stroke(STROKE);
        $.strokeWeight(1);
        $.ellipse(particle.x, particle.y, particle.radius, particle.radius);
    }

    public void step() {
//        steps++;
//        if (steps % 60 == 0) {
//            spawnCar();
//        }
    }

    private void spawnCar() {
        Car car = new Car(particle.x, particle.y);
        $.cars.add(car);

        $.physics.addSpring(new VSpring(car.particle, particle, 100, 0.0005f));
    }
}
