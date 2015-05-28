package creativecode.city;

import static creativecode.city.GenerativeCity.*;
import fisica.FCircle;

public class Car {

    private static final int   COLOR    = 0xffFFDB5B;

    private static final float DIAMETER = 5;

    FCircle                    circle;

    Car(float x, float y) {
        circle = new FCircle(DIAMETER);
        circle.setPosition(x, y);
        circle.setRestitution(0);

        circle.setFillColor(COLOR);
        circle.setNoStroke();

        $.world.add(circle);
    }
}
