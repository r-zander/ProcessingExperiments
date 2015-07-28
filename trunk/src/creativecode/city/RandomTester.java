package creativecode.city;

import java.util.ArrayList;
import java.util.Collections;

import processing.core.PApplet;

public class RandomTester extends PApplet {

    @Override
    public void setup() {
        size(displayWidth, displayHeight, P2D);

        int numberOfRolls = width / 2;

        @SuppressWarnings("unchecked")
        ArrayList<Float>[][] values = new ArrayList[2][2];

        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                values[i][j] = new ArrayList<Float>();
            }
        }

        for (int i = 0; i < numberOfRolls; i++) {
            values[0][0].add(random(1) * height / 2);
            values[1][0].add(random(1) * random(1) * height / 2);
            values[0][1].add(random(1) * random(1) * random(1) * height / 2);
            values[1][1].add(pow(random(1), 3) * height / 2);
        }

        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                Collections.sort(values[i][j]);
            }
        }

        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                int x = 0;
                for (Float value : values[i][j]) {
                    line(i * width / 2 + x, (j + 1) * height / 2, i * width / 2 + x, (j + 1) * height / 2 - value);
                    x++;
                }
            }
        }

    }

    @Override
    public void draw() {}

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", RandomTester.class.getName() });
    }
}
