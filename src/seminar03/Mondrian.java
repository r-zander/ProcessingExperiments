package seminar03;

import processing.core.PApplet;

public class Mondrian extends PApplet {

    @Override
    public void setup() {

        final int h1 = 130;
        final int h2 = 150;
        final int h3 = 120;

        final int b1 = 120;
        final int b2 = 270;
        final int b3 = 30;

        final int weiss = 0xFFFFFFFF;
        final int rot = 0xFFEA0212;
        final int blau = 0xFF2C85FF;
        final int gelb = 0xFFFFF300;

        size(b1 + b2 + b3, h1 + h2 + h3);
        strokeWeight(8);

        fill(weiss);
        rect(0, 0, b1, h1);
        rect(0, h1, b1, h2);

        fill(blau);
        rect(0, h1 + h2, b1, h3);

        fill(rot);
        rect(b1, 0, b2 + b3, h1 + h2);

        fill(weiss);
        rect(b1, h1 + h2, b2, h3);
        rect(b1 + b2, h1 + h2, b3, h3 / 2);

        fill(gelb);
        rect(b1 + b2, h1 + h2 + h3 / 2, b3, h3 / 2);
    }

    @Override
    public void draw() {
        // NOP
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", Mondrian.class.getName() });
    }
}