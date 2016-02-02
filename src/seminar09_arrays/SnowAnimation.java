package seminar09_arrays;

import processing.core.PApplet;
import processing.core.PImage;

public class SnowAnimation extends PApplet {

    private class Flocke {

        private final PImage snowImage;

        private float        xPos, yPos;

        private float        xSpeed, ySpeed;

        private float        xSpeedMod;

        private float        xSpeedMultiplier;

//        private float        rotationMod;

        private boolean      stopped = false;

        public Flocke(PImage snowImage) {
            this.snowImage = snowImage;

            xPos = random(width);
            yPos = random(-height);
            xSpeedMod = random(TWO_PI);
            xSpeedMultiplier = random(0.2f, 1f);
//            rotationMod = random(1, 4);
            xSpeed = sin(xSpeedMod) * X_SPEED_BASE * xSpeedMultiplier;
            ySpeed = Y_SPEED_BASE + abs(randomGaussian()) * Y_SPEED_BASE;
        }

        public void moveAndDraw() {
//            pushMatrix();
//            translate(xPos, yPos);
//            rotate(xSpeedMod * rotationMod);
//            translate(-snowImage.width / 2, -snowImage.height / 2);
//          image(snowImage, 0, 0);
            image(snowImage, xPos, yPos);
//            popMatrix();

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

            xSpeedMod += .03f;
            xSpeed = sin(xSpeedMod) * X_SPEED_BASE * xSpeedMultiplier;
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
            flocke.moveAndDraw();
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[] { "--present", SnowAnimation.class.getName() });
    }
}