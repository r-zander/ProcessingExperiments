package malevich;

import static util.Colors.*;
import processing.core.PApplet;

public class MalevichBasic extends PApplet {

    protected int squareColor       = color(245, 188, 0);

    protected int smallElementColor = color(137, 77, 4);

    protected int quadColor         = color(5, 8, 149);

    protected int bgColor           = color(242, 241, 237);

    protected int rectColor         = color(5, 188, 0);

    @Override
    public void setup() {
        size(520, 700);
        background(bgColor);
        drawMalevich();
        frameRate(10);
    }

    @Override
    public void draw() {
        if (isInteractive() && mousePressed == true) {
            squareColor = randomColor();
            smallElementColor = randomColor();
            quadColor = randomColor();
            bgColor = randomColor();
            rectColor = randomColor();
            background(bgColor);

            fill(squareColor);
            stroke(squareColor);
            strokeWeight(random(2, 30));
            line(250, 200, mouseX, mouseY);

            drawMalevich();
        }

        if (keyPressed && key == ' ') {
            save(getClass().getSimpleName() + ".png");
        }
    }

    protected boolean isInteractive() {
        return true;
    }

    private void drawMalevich() {
        fill(squareColor);
        noStroke();
        rect(152, 60, 200, 200);

        fill(rectColor);
        noStroke();
        rect(50, 300, 80, 130);

        fill(smallElementColor);
        noStroke();
        rect(100, 430, 50, 50);

        noFill();
        strokeWeight(9);
        stroke(smallElementColor);
        ellipse(180, 405, 50, 50);

        fill(quadColor);
        noStroke();
        strokeWeight(0);
        quad(200, 510, 370, 400, 450, 560, 335, 640);

        fill(squareColor);
        noStroke();
        strokeWeight(0);
        quad(371, 615, 488, 525, 506, 549, 388, 640);
    }

    public static void main(String[] args) {
        PApplet.main(new String[] { "--present", MalevichBasic.class.getName() });
    }
}