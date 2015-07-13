package creativecode.noisePainter;

import processing.core.PApplet;
import util.Numbers;

public class NoisePainter extends PApplet {

    int color = Numbers.random(0, 255);

    @Override
    public void setup() {
        size(displayWidth, displayHeight);
        background(255);

        colorMode(HSB);
    }

    @Override
    public void draw() {
        if (mousePressed) {
            stroke(color, 255, 255);
//        System.out.println(sqrt(sq(pmouseX - mouseX) + sq(pmouseY - mouseY)));

            float mouseDist = dist(pmouseX, mouseY, pmouseX, pmouseY);

//        strokeWeight(max(map(mouseDist, 0, 150, 40, 3), 0));
            strokeWeight(noise(frameCount * 0.1f) * 40);
            line(pmouseX, pmouseY, mouseX, mouseY);
            color += noise(frameCount * 0.1f) * 10 - 4;

            color %= 256;
        }
    }

    @Override
    public void keyPressed() {
        background(255);
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", NoisePainter.class.getName() });
    }
}
