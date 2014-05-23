package seminar04;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

public class ReflectedBall extends PApplet {

    private static final long  serialVersionUID = -56589606646834162L;

    private static final int   BACKGROUND_COLOR = 0;

    private int                radius           = 100;

    private static final float SPEED_BASE       = 15;

    private class Ball {

        private float hue;

        private float xPos;

        private float yPos;

        private float xSpeed;

        private float ySpeed;

        public Ball() {
            hue = random(0, 255);
            xSpeed = random(-SPEED_BASE, SPEED_BASE);
            ySpeed = random(-SPEED_BASE, SPEED_BASE);
        }

        public void draw() {
            fill(hue, 255, 255);
            ellipse(xPos, yPos, radius, radius);
        }
    }

    private List<Ball> balls = new ArrayList<Ball>();

    private Ball       puck;

    private int        blockCounter;

    @Override
    public boolean sketchFullScreen() {
        return true;
    }

    @Override
    public void setup() {
        size(displayWidth, displayHeight);

        colorMode(HSB);
        background(BACKGROUND_COLOR);
        noStroke();

        Ball ball = new Ball();

        ball.xPos = width / 2;
        ball.yPos = height / 2;

        ball.draw();

        balls.add(ball);
    }

    @Override
    public void draw() {
//        background(BACKGROUND_COLOR);
        fill(BACKGROUND_COLOR, 20);
        rect(0, 0, width, height);

        if (mousePressed) {
            Ball ball = new Ball();
            ball.xPos = mouseX;
            ball.yPos = mouseY;

            balls.add(ball);
        }

        // Draw mouse puck
        fill(255 - BACKGROUND_COLOR);
        ellipse(mouseX, mouseY, radius, radius);

        for (Ball ball : balls) {
            ball.xPos += ball.xSpeed;
            ball.yPos += ball.ySpeed;

            if (ball.xPos + radius / 2 < 0 || ball.xPos + radius / 2 > width) {
                ball.xSpeed = -ball.xSpeed;
            } else if (ball.yPos + radius / 2 < 0 || ball.yPos + radius / 2 > height) {
                ball.ySpeed = -ball.ySpeed;
            } else if (sqrt(sq(ball.xPos - mouseX) + sq(ball.yPos - mouseY)) <= radius) {
                ball.xPos -= ball.xSpeed;
                ball.yPos -= ball.ySpeed;
                float random = random(3);
                if (random <= 1) {
                    ball.xSpeed = -ball.xSpeed;
                } else if (random <= 2) {
                    ball.ySpeed = -ball.ySpeed;
                } else {
                    ball.xSpeed = -ball.xSpeed;
                    ball.ySpeed = -ball.ySpeed;
                }
                fill(255 - BACKGROUND_COLOR);
                ellipse(mouseX, mouseY, radius * 3, radius * 3);
            }
            ball.draw();
        }

        if (keyPressed) {
            balls.clear();
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", ReflectedBall.class.getName() });
    }
}