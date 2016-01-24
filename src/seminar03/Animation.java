package seminar03;

import processing.core.PApplet;

public class Animation extends PApplet {

    @Override
    public void setup() {
        size(1920, 1080);
        background(255);
        colorMode(HSB);
    }

    int yPos                = height / 2;

    int ellipseSize         = 60;

    int ellipseSizeModifier = 10;

    int ellipseHue          = 0xFF000000;

    @Override
    public void draw() {
        background(255);
        fill(ellipseHue, 255, 255);
        ellipse(width / 2, yPos, ellipseSize, ellipseSize);
        yPos = (yPos + 4) % height;
        ellipseSize += ellipseSizeModifier;
        if (ellipseSize >= 300 || ellipseSize <= 60) {
            ellipseSizeModifier = -ellipseSizeModifier;
        }
        ellipseHue += 1;
        ellipseHue = ellipseHue % 255;
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", Animation.class.getName() });
    }
}