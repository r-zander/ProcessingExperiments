package creativecode.city;

import processing.core.PApplet;

public class GenerativeCity extends PApplet {

    /**
     * Currently running instance
     */
    public static GenerativeCity $;

    static class Colors {

        static final int BACKGROUND = 0x00000000;
    }

    @Override
    public void setup() {
        $ = this;
        size(displayWidth, displayHeight, P2D);
        background(0);

        frameRate(5);
    }

    @Override
    public void draw() {
        new Building(random(width), random(height), random(50, 200), random(50, 200)).draw();
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", GenerativeCity.class.getName() });
    }
}
