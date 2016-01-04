package animations.animatedAbstractPepe;

import processing.core.PApplet;
import processing.core.PImage;

public class AnimatedAbstractPepe extends PApplet {

    private static final long serialVersionUID = -56589606646834162L;

    private PImage            img;

    private int               column;

    private boolean           forward          = true;

    private boolean           manualMode       = false;

    @Override
    public void setup() {
//        img = loadImage("pepes/sad.png");
        img = loadImage("pepes/hello-kitty.jpg");
//        img = loadImage("pepes/cocktail.jpg");

//        int size = min(displayHeight, displayWidth);
        size(img.height, img.height * 2);
        // scale to screen
        strokeWeight(1);
    }

    @Override
    public void draw() {
        if (manualMode) {
            column = (int) ((float) mouseX / width * img.width);
        }

        /*
         * Draw Debug image
         */
        image(img, 0, 0);
        stroke(255, 0, 0);
        line(column, 0, column, img.height);

        for (int y = 0; y < img.height; y++) {
            stroke(img.get(column, y));
            line(0, img.height + y, width, img.height + y);
        }

        if (!manualMode) {
            if (forward) {
                if (column >= img.width - 1) {
                    forward = false;
                } else {
                    column++;
                }
            } else {
                if (column < 1) {
                    forward = true;
                } else {
                    column--;
                }
            }
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", AnimatedAbstractPepe.class.getName() });
    }
}