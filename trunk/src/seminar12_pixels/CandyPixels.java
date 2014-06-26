package seminar12_pixels;

import processing.core.PApplet;
import processing.core.PImage;

public class CandyPixels extends PApplet {

    private static final long serialVersionUID = -56589606646834162L;

    private PImage            img;

    int                       index;

    @Override
    public void setup() {
        img = loadImage("pixels/candy.jpg");
        size(img.width, img.height);
    }

    @Override
    public void draw() {
        image(img, 0, 0);

        int color = img.get(mouseX, mouseY);
        fill(color);
        ellipse(width / 2, height / 3, 150, 150);
        fill(red(color), 0, 0);
        ellipse(width / 4, height / 3 * 2, 150, 150);
        fill(0, green(color), 0);
        ellipse(width / 4 * 2, height / 3 * 2, 150, 150);
        fill(0, 0, blue(color));
        ellipse(width / 4 * 3, height / 3 * 2, 150, 150);

    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", CandyPixels.class.getName() });
    }
}