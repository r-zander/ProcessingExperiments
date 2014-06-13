package seminar09_arrays;

import processing.core.PApplet;
import processing.core.PImage;

public class SchneeArray extends PApplet {

    private static final long serialVersionUID = -56589606646834162L;

    private class Flocke {

        private final PImage snowImage;

        private float        xPos, yPos;

        private float        xSpeed, ySpeed;

        private float        speedMod;

        private boolean      stopped = false;

        public Flocke(PImage snowImage) {
            this.snowImage = snowImage;

            xPos = random(width);
            yPos = random(-height);
            speedMod = random(TWO_PI);
            xSpeed = sin(speedMod) * X_SPEED_BASE;
            ySpeed = Y_SPEED_BASE + abs(randomGaussian()) * Y_SPEED_BASE;
        }

        public void moveAnddraw() {
            image(snowImage, xPos, yPos);

            if (stopped) {
                return;
            }

            yPos += ySpeed;
            if (yPos >= height - 30) {
                if (yPos >= height || random(30) < 1) {
                    stopped = true;
                    return;
                }
            }

            speedMod += .03f;
            xSpeed = sin(speedMod) * X_SPEED_BASE;
            xPos += xSpeed;
        }
    }

    PImage[]           snowImages;

    Flocke[]           flocken;

    static final float X_SPEED_BASE = 5;

    static final float Y_SPEED_BASE = .5f;

    @Override
    public void setup() {
        size(displayWidth, displayHeight);

        snowImages = new PImage[3];
        for (int i = 0; i < snowImages.length; i++) {
            snowImages[i] = loadImage("schnee/snow" + i + ".png");
        }

        flocken = new Flocke[500];
        for (int i = 0; i < flocken.length; i++) {
            flocken[i] = new Flocke(snowImages[round(random(0, 2))]);
        }

        imageMode(CENTER);
    }

    @Override
    public void draw() {
        background(0);

        for (Flocke flocke : flocken) {
            flocke.moveAnddraw();
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", SchneeArray.class.getName() });
    }
}