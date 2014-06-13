package seminar10_methods;

import processing.core.PApplet;

public class TrianglePattern extends PApplet {

    private static final long serialVersionUID = -56589606646834162L;

    float                     breiteM, hoeheM;

    int                       xNum             = 3;

    int                       yNum             = 3;

    @Override
    public void setup() {
        size(displayWidth, displayHeight);

        breiteM = width / (float) xNum;
        hoeheM = height / (float) yNum;
    }

    @Override
    public void draw() {
        background(0);
        stroke(255);
        noFill();

        for (float mx = breiteM / 2; mx < width; mx += breiteM) {
            for (float my = hoeheM / 2; my < height; my += hoeheM) {
                float d = map(mouseX, 0, width, 0, breiteM);
                musterZeichnung(mx, my, d);
            }
        }
    }

    private void musterZeichnung(float mx, float my, float d) {
        triangle(mx - breiteM / 2, my - hoeheM / 2, mx - d, my, mx, my - d);
        triangle(mx + breiteM / 2, my - hoeheM / 2, mx + d, my, mx, my - d);
        triangle(mx - breiteM / 2, my + hoeheM / 2, mx, my + d, mx - d, my);
        triangle(mx + breiteM / 2, my + hoeheM / 2, mx, my + d, mx + d, my);
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", TrianglePattern.class.getName() });
    }
}