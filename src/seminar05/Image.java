package seminar05;

import processing.core.PApplet;
import processing.core.PImage;

public class Image extends PApplet {

    private static final long serialVersionUID = -56589606646834162L;

    PImage                    lol;

    @Override
    public void setup() {
        size(400, 400);
        background(255);
        lol = loadImage("rock-n-roll.jpg");
    }

    @Override
    public void draw() {
        noFill();
        stroke(200);
        strokeWeight(10);
        rectMode(CENTER);
        imageMode(CENTER);
        tint(0, 255, 0);
        image(lol, width / 2, height / 2);
        rect(width / 2, height / 2, lol.width, lol.height);
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", Image.class.getName() });
    }
}