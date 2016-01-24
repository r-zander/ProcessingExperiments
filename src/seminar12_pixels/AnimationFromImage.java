package seminar12_pixels;

import processing.core.PApplet;
import processing.core.PImage;

public class AnimationFromImage extends PApplet {

    private PImage img;

    private int    resizeFactor = 6;

    private class Vector {

        float winkel = 0;

        float speed  = random(.1f, .25f);

        float length = random(1, 6);
    }

    private Vector[][] vectors;

    @Override
    public void setup() {
        img = loadImage("pixels/candy_s.jpg");
        resizeFactor = min(displayHeight / img.height, displayWidth / img.width);
        size(img.width * resizeFactor, img.height * resizeFactor);
        vectors = new Vector[img.width][img.height];

        for (int x = 0; x < img.width; x++) {
            for (int y = 0; y < img.height; y++) {
                vectors[x][y] = new Vector();
            }
        }
    }

    @Override
    public void draw() {
        background(255);
        for (int x = 0; x < img.width; x++) {
            for (int y = 0; y < img.height; y++) {
                pushMatrix();
                translate(x * resizeFactor, y * resizeFactor);
                int color = img.get(x, y);
                rotate(vectors[x][y].winkel);
                vectors[x][y].winkel += vectors[x][y].speed;
                stroke(color);

                strokeWeight(map(mouseX, 0, width, 0.1f, resizeFactor - 1));
                line(0, 0, vectors[x][y].length, vectors[x][y].length);
                line(vectors[x][y].length, 0, 0, vectors[x][y].length);
                popMatrix();
            }
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", AnimationFromImage.class.getName() });
    }
}