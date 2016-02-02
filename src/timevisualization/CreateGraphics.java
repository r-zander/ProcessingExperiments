package timevisualization;

import processing.core.PApplet;
import processing.core.PGraphics;

public class CreateGraphics extends PApplet {

    PGraphics pg;

    @Override
    public void setup() {
        size(200, 200);
        pg = createGraphics(100, 100);
    }

    @Override
    public void draw() {
        pg.beginDraw();
        pg.background(102);
        pg.stroke(255);
        pg.line(pg.width * 0.5f, pg.height * 0.5f, mouseX, mouseY);
        pg.endDraw();
        image(pg, 50, 50);
    }

    public static void main(String[] args) {
        PApplet.main(new String[] { "--present", CreateGraphics.class.getName() });
    }
}