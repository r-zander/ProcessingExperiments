package seminar06;

import processing.core.PApplet;

public class MultiplicationTable extends PApplet {

    @Override
    public void setup() {
        size(displayWidth, displayHeight);
        background(255);

        fill(0);
        for (int row = 1; row <= 10; row++) {
            for (int col = 1; col <= 10; col++) {
                float x = width / 10 * (col - .5f);
                float y = height / 10 * (row - .5f);
                text(row + " x " + col + " = " + row * col, x, y);
            }
        }
    }

    @Override
    public void draw() {}

    public static void main(String[] args) {
        PApplet.main(new String[] { "--present", MultiplicationTable.class.getName() });
    }
}