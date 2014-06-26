package seminar12_pixels;

import processing.core.PApplet;
import processing.core.PImage;

public class FilterResizeImage extends PApplet {

    private int               resizeFactor     = 6;

    private static final long serialVersionUID = -56589606646834162L;

    private PImage            img;

    int                       index;

    @Override
    public void setup() {
        img = loadImage("pixels/candy_s.jpg");
        resizeFactor = min(displayHeight / img.height, displayWidth / img.width);
        size(img.width * resizeFactor, img.height * resizeFactor);
    }

    @Override
    public void draw() {
        background(0);
        strokeWeight(1);
        stroke(0);
        for (int x = 0; x < img.width; x++) {
            for (int y = 0; y < img.height; y++) {
                fill(img.get(x, y));
                float size = resizeFactor / 2 + random(resizeFactor);
                ellipse((x + .5f) * resizeFactor, (y + .5f) * resizeFactor, size, size);
            }
        }
        image(img, 0, 0);
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", FilterResizeImage.class.getName() });
    }
}