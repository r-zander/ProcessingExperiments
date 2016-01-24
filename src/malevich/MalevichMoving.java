package malevich;

import processing.core.PApplet;
import util.Colors;

public class MalevichMoving extends PApplet {

    private static final float SPEED_BASE   = 15;

    private int                imageCounter = 1;

    private abstract class Element {

        public int   color;

        public float xPos;

        public float yPos;

        public float xSpeed;

        public float ySpeed;

        public float radius;

        public Element(float xPos, float yPos, float radius) {
            color = Colors.randomColor();
            this.xPos = xPos;
            this.yPos = yPos;
            xSpeed = random(-SPEED_BASE, SPEED_BASE);
            ySpeed = random(-SPEED_BASE, SPEED_BASE);
        }

        public void draw() {
            color = (int) map(noise(xPos / width / 70, yPos / height / 70), 0, 1, color(0, 0, 0), color(255, 255, 255));
            fill(color);
            noStroke();
            drawShape(xPos, yPos);
        }

        protected abstract void drawShape(float x, float y);
    }

    protected int     squareColor       = color(245, 188, 0);

    protected int     smallElementColor = color(137, 77, 4);

    protected int     quadColor         = color(5, 8, 149);

    protected int     bgColor           = color(242, 241, 237);

    protected int     rectColor         = color(5, 188, 0);

    private Element[] elements          = new Element[6];

    @Override
    public void setup() {
        size(520, 700);
        background(bgColor);
        frameRate(15);

        elements[0] = new Element(152, 60, 100) {

            @Override
            protected void drawShape(float x, float y) {
                rect(x, y, 200, 200);
            }
        };

        elements[1] = new Element(50, 300, 52.5f) {

            @Override
            protected void drawShape(float x, float y) {
                rect(x, y, 80, 130);
            }
        };

        elements[2] = new Element(100, 430, 25) {

            @Override
            protected void drawShape(float x, float y) {
                rect(x, y, 50, 50);
            }
        };

        elements[3] = new Element(180, 405, 25) {

            @Override
            protected void drawShape(float x, float y) {
                noFill();
                strokeWeight(9);
                stroke(color);
                ellipse(x, y, 50, 50);
            }
        };

        elements[4] = new Element(200, 510, 130) {

            @Override
            protected void drawShape(float x, float y) {
                quad(x, y, x + 170, y - 110, x + 250, y + 50, x + 135, y + 130);
            }
        };

        elements[5] = new Element(371, 615, 90) {

            @Override
            protected void drawShape(float x, float y) {
                quad(x, y, x + 117, y - 90, x + 135, y - 66, x + 17, y + 25);
            }
        };
        drawMalevich();
    }

    @Override
    public void draw() {
        background(bgColor);

        drawMalevich();

        if (keyPressed && key == ' ') {
            save(getClass().getSimpleName() + imageCounter + ".png");
        }
    }

    protected boolean isInteractive() {
        return true;
    }

    private void drawMalevich() {
        for (Element element : elements) {
            element.xPos += element.xSpeed;
            element.yPos += element.ySpeed;

            if (element.xPos + element.radius / 2 < 0 || element.xPos + element.radius / 2 > width) {
                element.xSpeed = -element.xSpeed;
            } else if (element.yPos + element.radius / 2 < 0 || element.yPos + element.radius / 2 > height) {
                element.ySpeed = -element.ySpeed;
            }
            element.draw();
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", MalevichMoving.class.getName() });
    }
}