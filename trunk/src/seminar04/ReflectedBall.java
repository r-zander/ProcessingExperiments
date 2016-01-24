package seminar04;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import util.TwoDimensional;

public class ReflectedBall extends PApplet {

    private static final int   BACKGROUND_COLOR = 0;

    private int                ballWidth        = 100;

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
            ellipse(xPos, yPos, ballWidth, ballWidth);
        }
    }

    private List<Ball> balls = new ArrayList<Ball>();

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
        fill(BACKGROUND_COLOR, 20);
        rect(0, 0, width, height);

        // Draw mouse puck
        fill(255 - BACKGROUND_COLOR);
        ellipse(mouseX, mouseY, ballWidth, ballWidth);

        for (Ball ball : balls) {
            ball.xPos += ball.xSpeed;
            ball.yPos += ball.ySpeed;

            if (ball.xPos < ballWidth / 2 || ball.xPos > width - ballWidth / 2) {
                ball.xSpeed = -ball.xSpeed;
            } else if (ball.yPos < ballWidth / 2 || ball.yPos > height - ballWidth / 2) {
                ball.ySpeed = -ball.ySpeed;
            } else if (sqrt(sq(ball.xPos - mouseX) + sq(ball.yPos - mouseY)) <= ballWidth) {
                // Push ball out of puck
                float angle = TwoDimensional.angleBetween(ball.xPos, ball.yPos, mouseX, mouseY);
                ball.xPos = mouseX + cos(angle) * ballWidth;
                ball.yPos = mouseY + sin(angle) * ballWidth;

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
                ellipse(mouseX, mouseY, ballWidth * 3, ballWidth * 3);
            }
            ball.draw();
        }
    }

    @Override
    public void mousePressed() {
        Ball ball = new Ball();
        ball.xPos = mouseX;
        ball.yPos = mouseY;

        balls.add(ball);
    }

    @Override
    public void keyPressed() {
        balls.clear();
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", ReflectedBall.class.getName() });
    }
}