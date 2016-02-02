package generativeLogo.zr_logo;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

public class ZR_Logo extends PApplet {

    static final float STROKE_WEIGHT             = 10;

    static final float STROKE_WEIGHT_CONSTRUCTED = 2;

    private int        frameCounter              = 0;

    private int        finishFrame;

    static final int   DEFAULT_FRAME_COUNT       = 120;

    static final float DRAW_SPEED                = 8;  // Pixel per frame;

    private float      centerX;

    private float      centerY;

    /**
     * Eigenschaften des Logo/Icons - gekapselt zur besseren Übersicht.
     */
    private static class Icon {

        public static float height;

        public static float width;

        public static float minX;

        public static float minY;

        public static float maxX;

        public static float maxY;
    }

    private List<Element> elements = new ArrayList<Element>();

    @Override
    public void setup() {
        size(600, 600);
        background(255);

        centerX = width / 2f;
        centerY = height / 2f;
        Icon.width = width * .4f;
        Icon.height = Icon.width * .8f;
        Icon.minX = centerX - Icon.width / 2;
        Icon.maxX = centerX + Icon.width / 2;
        Icon.minY = centerY - Icon.height / 2;
        Icon.maxY = centerY + Icon.height / 2;

        elements.add(new CompoundElement(Line.ofCoordinates(this, 0, 0, centerX, centerY), Line.ofCoordinates(
                this,
                width,
                0,
                centerX,
                centerY), Line.ofCoordinates(this, width, height, centerX, centerY), Line.ofCoordinates(
                this,
                0,
                height,
                centerX,
                centerY)).persistent(false));

        elements.add(Line.ofCoordinates(this, centerX, centerY, centerX, Icon.minY).persistent(false));

        final Line line1 = Line.ofCoordinates(this, Icon.minX, Icon.maxY, centerX, Icon.minY);

        elements.add(Line.ofVector(this, line1.length, 0, line1.endX, line1.endY).persistent(false));

        elements.add(new ConstructionArc(
                Line.ofCoordinates(this, line1.endX, line1.endY, line1.startX, line1.startY),
                0));
        elements.add(line1);

        Line line3 = Line.ofCoordinates(this, line1.endX, line1.endY, line1.endX, line1.startY);
        elements.add(new ConstructionArc(line3, line1.angle));
        elements.add(line3);

        Line line2 = Line.ofCoordinates(this, line1.startX, line1.startY, line1.endX, line1.startY);
        elements.add(new ConstructionArc(line2, line1.angle - PI));
        elements.add(line2);

        elements.add(new ConstructionArc(Line.ofVector(this, line1.length / 2, PI, line3.endX, line3.endY), line3.angle
                + PI));
        float length = Icon.width * .2f;
        Line line4 = Line.ofVector(this, length, 0, Icon.minX + Icon.width * .25f - length / 2, centerY);
        elements.add(line4);
        Line constructionLine1 = Line.ofCoordinates(this, line4.endX, line4.endY, line1.endX, line4.endY);
        constructionLine1.persistent(false);
        elements.add(constructionLine1);
        Bezier bezier =
                new Bezier(this, constructionLine1.endX, constructionLine1.endY, Icon.maxX, line4.endY, line1.endX
                        + Icon.width * .3f, line1.endY, line1.endX, line1.endY);
        bezier.frameCount = 100;
        elements.add(bezier);

        elements.add(new CompoundElement(Line.ofCoordinates(this, bezier.x4, bezier.y4, Icon.minX, bezier.y4), Line
                .ofCoordinates(this, bezier.getX(this, .2f), bezier.getY(this, .2f), Icon.maxX, Icon.maxY)));

        for (Element element : elements) {
            finishFrame += element.frameCount;
        }
        finishFrame += 300;
    }

    @Override
    public void draw() {
//        fill(255, 15);
//        noStroke();
//        rect(0, 0, width, height);
        background(255);

        strokeWeight(STROKE_WEIGHT);
        noFill();
        int relativeFrameCount = frameCounter;
        for (Element element : elements) {
            if (relativeFrameCount <= element.frameCount) {
                element.draw(relativeFrameCount / (float) element.frameCount);
                /*
                 * Aktuelles Element wurde gefunden - Schleife abbrechen.
                 */
                break;
            } else {
//                if (element.persistent) {
                element.draw(1);
//                }
                relativeFrameCount -= element.frameCount;
            }
        }
        if (frameCounter >= finishFrame) {
            frameCounter = 0;
            background(255);
        }
        if (!mousePressed) {
            frameCounter++;
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[] { "--present", ZR_Logo.class.getName() });
    }
}