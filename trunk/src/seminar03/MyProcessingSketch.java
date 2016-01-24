package seminar03;

import processing.core.PApplet;

public class MyProcessingSketch extends PApplet {

    @Override
    public void setup() {
        size(1920, 1080);
        background(0);
    }

    @Override
    public void draw() {
        stroke(255);
        if (mousePressed) {
//            line(mouseX, mouseY, pmouseX, pmouseY);
            ellipse(mouseX, mouseY, 50, 50);
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", MyProcessingSketch.class.getName() });
    }
}