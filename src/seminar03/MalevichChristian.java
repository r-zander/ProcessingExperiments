package seminar03;

import processing.core.PApplet;

public class MalevichChristian extends PApplet {

    @Override
    public void setup() {
        size(520, 700);
        stroke(0);
        colorMode(RGB);
        background(242, 241, 237);
        frameRate(30);

    }

    @Override
    public void draw() {

        // fill(0,0,255);
        // rectMode(RADIUS);
        // rect(200, 200, 30, 30);
        //
        // noFill();
        // ellipseMode(RADIUS);
        // stroke(0,255,0);
        // ellipse(200, 200, 22, 30);
        //
        // noFill();
        // rectMode(CORNER);
        // stroke(0);
        // strokeWeight(5);
        // rect(200, 200, 30, 30);
        //
        // stroke(255,0,0);
        // strokeWeight(1);
        // line(0,200,200,200);

        if (mousePressed == true) {
            fill(0);
            stroke(6);
            strokeWeight(3);
            line(250, 200, mouseX, mouseY);

        }
        fill(0);
        noStroke();
        rect(152, 60, 200, 200);

        colorMode(RGB);
        fill(245, 188, 0);
        noStroke();
        rect(50, 300, 80, 130);

        colorMode(RGB);
        fill(137, 77, 4);
        noStroke();
        rect(100, 430, 50, 50);

        colorMode(RGB);
        noFill();
        strokeWeight(9);
        stroke(137, 77, 4);
        ellipse(180, 405, 50, 50);

        colorMode(RGB);
        fill(5, 8, 149);
        noStroke();
        strokeWeight(0);
        quad(200, 510, 370, 400, 450, 560, 335, 640);

        fill(0);
        noStroke();
        strokeWeight(0);
        quad(371, 615, 488, 525, 506, 549, 388, 640);

    }

    public static void main(String[] args) {
        PApplet.main(new String[] { "--present", MalevichChristian.class.getName() });
    }
}