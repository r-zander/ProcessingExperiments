package creativecode.city;

import static creativecode.city.GenerativeCity.*;
import processing.core.PShape;
import fisica.FBox;
import fisica.FRevoluteJoint;

public class Building {

    FBox             box;

    PShape           shape;

    float            x, y;

    final static int BACKGROUND = 0x5E232BFF;

    final static int STROKE     = 0xFF78CCFF;

    int              steps;

//    FConstantVolumeJoint volumeJoint;

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

        spawnCar();
    }

    void draw() {
        $.shape(shape, x, y);
    }

//    public void step() {
//        steps++;
//        if (steps % 60 == 0) {
//            spawnCar();
//        }
//    }

    private void spawnCar() {
        Car car = new Car(x, y);

        FRevoluteJoint j = new FRevoluteJoint(box, car.circle);
        j.setEnableMotor(true);
        j.setMotorSpeed(100);
        j.setMaxMotorTorque(0);
        j.setAnchor(x, y);
        $.world.add(j);
    }
}
