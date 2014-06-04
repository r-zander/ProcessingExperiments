package timevisualization;

import processing.core.PApplet;
import util.Shapes;

import com.koniaris.astronomy.Moon;

public class ArcTest extends PApplet {

    private static final long serialVersionUID = -56589606646834162L;

    private float             centerX;

    private float             centerY;

    @Override
    public void setup() {
        size(displayWidth, displayHeight);
        background(255);
        centerX = width / 2;
        centerY = height / 2;

//        stroke(0);
//        strokeWeight(20);
//
//        strokeCap(SQUARE);
//        arc(centerX, 100, 200, 200, 0, PI * .75f);
//
//        strokeCap(ROUND);
//        arc(centerX, centerY, 200, 200, 0, PI * .75f);
//
//        strokeCap(PROJECT);
//        arc(centerX, height - 300, 200, 200, 0, PI * .75f);

        stroke(0);
        strokeWeight(3);
        fill(color(255, 192, 0));

        try {
            Moon moon = new Moon();
            Shapes.moon(this, centerX, centerY, 300, moon.illuminatedFraction(), moon.isWaning());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void draw() {}

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", ArcTest.class.getName() });
    }
}