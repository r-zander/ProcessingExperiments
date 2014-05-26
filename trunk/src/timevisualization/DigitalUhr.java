package timevisualization;

import processing.core.PApplet;
import processing.core.PFont;

public class DigitalUhr extends PApplet {

    private static final long serialVersionUID = -56589606646834162L;

    PFont                     uhrFont;

    @Override
    public void setup() {
        size(400, 200);
        background(255);
        uhrFont = createFont("ACaslonPro-Regular", 40);
    }

    @Override
    public void draw() {
        int h = hour();
        int m = minute();
        int s = second();

        String aktuelleZeit = h + " : " + m + " : " + s;
        background(0);
        textAlign(CENTER, CENTER);
        fill(0, 255, 0);
        text(aktuelleZeit, width / 2, height / 2);
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", DigitalUhr.class.getName() });
    }
}