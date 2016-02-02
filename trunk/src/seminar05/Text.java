package seminar05;

import processing.core.PApplet;
import processing.core.PFont;

public class Text extends PApplet {

    PFont font;

    @Override
    public void setup() {
        size(400, 400);
        background(0);
        /*
         * Load font funktioniert nur aus Processing direkt heraus.
         */
//        font = loadFont("ProggyCleanTTSZBP-16.vlw");

        /*
         * Create font geht nur mit TTF und OTF.
         */
        println(PFont.list());
        font = createFont("ProggyCleanTTSZBP", 16);
    }

    @Override
    public void draw() {
        textAlign(CENTER, CENTER);
        textFont(font, 16);
        textLeading(40);
        fill(color(0, 192, 0));
        text("Apfel\nBanane\nMelone", width / 2, height / 2);
    }

    public static void main(String[] args) {
        PApplet.main(new String[] { "--present", Text.class.getName() });
    }
}