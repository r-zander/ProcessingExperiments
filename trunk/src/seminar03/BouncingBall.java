package seminar03;

import processing.core.PApplet;

public class BouncingBall extends PApplet {

    @Override
    public void setup() {
        size(400, 400);
        stroke(0);
        strokeWeight(2);
        line(100, 200, 300, 200);
    }

    float x   = 0;

    float sin = 0;

    @Override
    public void draw() {
        background(255);
        x += .01;
        sin = sin(x);

        ellipse(200, 200 + abs(sin) * 300, 50, 50);
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", BouncingBall.class.getName() });
    }
}