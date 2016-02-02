package seminar03;

import processing.core.PApplet;

public class MalevichSuzan extends PApplet {

    @Override
    public void setup() {
        size(400, 500);
        colorMode(RGB);
        background(232, 226, 201);

    }

    @Override
    public void draw() {

        fill(5, 5, 5);
        noStroke();
        rect(110, 20, 180, 180);

        fill(234, 163, 28);
        noStroke();
        rect(20, 220, 80, 130);

        fill(170, 87, 9);
        noStroke();
        rect(70, 350, 40, 35);

        strokeWeight(9);
        stroke(170, 87, 9);
        noFill();
        ellipse(140, 330, 40, 40);

        stroke(5, 5, 5);
        strokeWeight(20);
        strokeCap(SQUARE);
        line(270, 445, 340, 405);

        fill(90, 99, 144);
        noStroke();
        quad(180, 380, 280, 320, 310, 410, 240, 450);

    }

    public static void main(String[] args) {
        PApplet.main(new String[] { "--present", MalevichSuzan.class.getName() });
    }
}