package creativecode.city;

import static creativecode.city.GenerativeCity.*;
import static processing.core.PConstants.*;
import punktiert.math.Vec;
import punktiert.physics.BCollision;
import punktiert.physics.BWander;
import punktiert.physics.VParticle;

public class Car {

    static final int   COLOR    = 0xffFFDB5B;

    static final float DIAMETER = 5;

    VParticle          particle;

    Car(float x, float y) {
        particle = new VParticle(new Vec(x - DIAMETER, y - DIAMETER), 1, DIAMETER / 2);
//        particle.add(new Vec(-particle.radius * 8, -particle.radius * 8));
        particle.addBehavior(new BCollision(-0.5f));
        BWander wander = new BWander(1, 1, 1);
        wander.setChange(TWO_PI);
        particle.addBehavior(wander);
//        particle.addVelocity(new Vec($.random(-5, 5), $.random(-5, 5)));
//        particle.setVelocity(new Vec($.random(-5, 5), $.random(-5, 5)));
//        particle.addBehavior(new BSeparate(particle.radius));

        $.physics.addParticle(particle);
    }

    public void draw() {
        $.ellipseMode(RADIUS);
        $.noStroke();
        $.fill(COLOR);
        $.ellipse(particle.x, particle.y, particle.radius, particle.radius);
    }
}
