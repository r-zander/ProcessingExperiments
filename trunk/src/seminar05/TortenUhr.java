package seminar05;

import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PImage;

public class TortenUhr extends PApplet {

    private static final long    serialVersionUID = -56589606646834162L;

    private Map<Integer, PImage> images           = new HashMap<Integer, PImage>();

    @Override
    public void setup() {
        size(1400, 300);
        background(255);
        frameRate(2);

        for (int i = 0; i < 10; i++) {
            images.put(i, loadImage("torte/torte" + i + ".jpg"));
        }

        /*
         * Draw colons.
         */
        fill(0);
        ellipse(450, 100, 30, 30);
        ellipse(450, 200, 30, 30);
        ellipse(950, 100, 30, 30);
        ellipse(950, 200, 30, 30);
    }

    @Override
    public void draw() {
        renderDigits(hour(), 0);
        renderDigits(minute(), 500);
        renderDigits(second(), 1000);
    }

    private void renderDigits(int number, int offset) {
        int ten = number / 10;
        int one = number % 10;
        image(images.get(ten), offset, 0);
        image(images.get(one), offset + 200, 0);
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", TortenUhr.class.getName() });
    }
}