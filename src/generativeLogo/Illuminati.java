package generativeLogo;

import processing.core.PApplet;

public class Illuminati extends PApplet {

    private static final long serialVersionUID       = -56589606646834162L;

    private int               outerCircleColor       = color(0, 60, 191);

    private float             outerCircleStarteAngle = radians(150);

    private float             outerCircleRadius;

    private int               eyeColor               = color(210, 33, 40);

    private int               pyramideColor          = color(58, 191, 0);

    private float             pyramideStartX;

    private float             pyramideStartY;

    private float             pyramideBaseWidth;

    private int               frameCounter           = 0;

    private int               centerX;

    private int               centerY;

    private IlluminatiMode    mode                   = IlluminatiMode.NORMAL;

    @Override
    public void setup() {
        size(600, 600);
        background(0);
        frameRate(60);

        centerX = width / 2;
        centerY = height / 2;

        outerCircleRadius = centerX * .9f;

        float pyramideHalfHeight = outerCircleRadius * .9f;
        pyramideStartX = centerX + cos(outerCircleStarteAngle) * pyramideHalfHeight;
        pyramideStartY = centerX + sin(outerCircleStarteAngle) * pyramideHalfHeight;
        pyramideBaseWidth = pyramideHalfHeight * 1.5f / sin(radians(60));
        System.out.println(pyramideBaseWidth);

        strokeCap(SQUARE);
    }

    @Override
    public void draw() {

//        fill(0, 5);
//        noStroke();
//        rect(0, 0, width, height);

        if (frameCounter <= 60) {
            /*
             * Draw circle.
             */
            float angle = outerCircleStarteAngle + TWO_PI / 60 * frameCounter;
            stroke(outerCircleColor);
            if (mode == IlluminatiMode.HALF) {
                strokeWeight(15);
            } else {
                strokeWeight(30);
            }
            line(centerX, centerY, centerX + cos(angle) * outerCircleRadius, centerY + sin(angle) * outerCircleRadius);
        } else if (frameCounter <= 60 + 24) {
            stroke(pyramideColor);
            strokeWeight(5);
            float x = pyramideStartX + pyramideBaseWidth / 24 * (frameCounter - 60);
            line(pyramideStartX, pyramideStartY, x, pyramideStartY);
        } else if (frameCounter <= 60 + 24 + 60) {
            stroke(pyramideColor);
            float strokeWeight = 5;
            strokeWeight(mode == IlluminatiMode.HALF ? strokeWeight / 2 : strokeWeight);
            int counter = (frameCounter - 60 - 24);
            float halfWidth = pyramideBaseWidth * counter / 60 / 2;
            float y = pyramideStartY - counter * strokeWeight;
            line(pyramideStartX + halfWidth, y, pyramideStartX + pyramideBaseWidth - halfWidth, y);
        }

        if (mousePressed) {
            frameCounter++;
        }
        frameCounter++;

        if (keyPressed && key == ' ') {
            save(getClass().getSimpleName() + ".png");
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", Illuminati.class.getName() });
    }
}