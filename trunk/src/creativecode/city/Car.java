package creativecode.city;

import static creativecode.city.GenerativeCity.*;
import static processing.core.PConstants.*;
import punktiert.math.Vec;
import punktiert.physics.BCollision;
import punktiert.physics.BWander;
import punktiert.physics.VParticle;

public class Car {

    static final int   COLOR    = 0xffFFDB5B;

    static final float DIAMETER = 3;

    VParticle          particle;

    Car(float x, float y) {
        particle = new VParticle(new Vec(x - DIAMETER, y - DIAMETER), 5, DIAMETER / 2);
//        particle.add(new Vec(-particle.radius * 8, -particle.radius * 8));
        particle.addBehavior(new BCollision());
//        particle.addBehavior(new BCarSpeed());
        BWander wander = new BWander(1, 100, 1);
//        wander.setChange(TWO_PI);
        particle.addBehavior(wander);
//        particle.addVelocity(new Vec($.random(-5, 5), $.random(-5, 5)));
//        particle.setVelocity(new Vec($.random(-2, 2), $.random(-2, 2)));
//        particle.addBehavior(new BSeparate(particle.radius));

        $.physics.addParticle(particle);
    }

    public void draw() {
        $.ellipseMode(RADIUS);
        $.pushMatrix();

        Vec velocity = particle.getVelocity();

        $.translate(particle.x, particle.y);
        $.rotate(velocity.heading() + HALF_PI);

        $.noStroke();
        $.fill(COLOR);
        $.ellipse(0, 0, particle.radius, particle.radius * (1 + velocity.mag()));

        $.popMatrix();
    }
}
