package seminar12_pixels;

import processing.core.PApplet;
import processing.core.PImage;

public class MonroePixels extends PApplet {

    private static final long serialVersionUID = -56589606646834162L;

    private PImage            img;

    int                       index;

    @Override
    public void setup() {
        img = loadImage("pixels/monroe.jpg");
        size(img.width, img.height);

        img.loadPixels();
        for (int x = 0; x < img.width; x++) {
            for (int y = 0; y < img.height; y++) {
                int newColor;
                if (brightness(img.get(x, y)) > 180) {
                    newColor = color(255);
                } else {
                    newColor = color(0);
                }
                img.set(x, y, newColor);
            }
        }
        img.updatePixels();
    }

    @Override
    public void draw() {
        image(img, 0, 0);

    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", MonroePixels.class.getName() });
    }
}