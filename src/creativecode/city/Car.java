package creativecode.city;

import static creativecode.city.GenerativeCity.*;
import fisica.FCircle;

public class Car {

    static final int   COLOR    = 0xffFFDB5B;

    static final float DIAMETER = 5;

    FCircle            circle;

    Car(float x, float y) {
        circle = new FCircle(DIAMETER);
        circle.setPosition(x, y);
        circle.setRestitution(0);
        circle.setFriction(0);
        circle.setDamping(0);
        circle.setAngularDamping(0);

        circle.setFillColor(COLOR);
        circle.setNoStroke();

//        circle.addForce($.random(200), $.random(200));
//        circle.addImpulse($.random(100), $.random(100));
//        circle.addTorque($.random(-20000, 20000));
        circle.setVelocity($.random(100), $.random(100));

        $.world.add(circle);

    }
}
