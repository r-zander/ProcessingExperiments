package generativeLogo.zr_logo;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

public class ZR_Logo extends PApplet {

    static final int   BG_COLOR                  = 0xFF1C1C1C;

    static final float STROKE_WEIGHT             = 20;

    static final float STROKE_WEIGHT_CONSTRUCTED = 1;

    private int        frameCounter              = 0;

    private int        finishFrame = 0;

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

    private final List<Element> elements = new ArrayList<>();

    @Override
    public void setup() {
        size(1000, 1000);
        background(BG_COLOR);

        centerX = width / 2f;
        centerY = height / 2f;
        Icon.width = width * .4f;
        Icon.height = Icon.width * .8f;
        Icon.minX = centerX - Icon.width / 2;
        Icon.maxX = centerX + Icon.width / 2;
        Icon.minY = centerY - Icon.height / 2;
        Icon.maxY = centerY + Icon.height / 2;

        elements.add(
                new CompoundElement(
                        Line.ofCoordinates(this, 0, 0, centerX, centerY),
                        Line.ofCoordinates(this, width, 0, centerX, centerY),
                        Line.ofCoordinates(this, width, height, centerX, centerY),
                        Line.ofCoordinates(this,0,height,centerX,centerY)
                ).persistent(false));

        elements.add(Line.ofCoordinates(this, centerX, centerY, centerX, Icon.minY).persistent(false));

        // Diagonal line of Z
        final Line line1 = Line.ofCoordinates(this, Icon.minX, Icon.maxY, centerX, Icon.minY);

        elements.add(Line.ofVector(this, line1.length, 0, line1.endX, line1.endY).persistent(false));

        elements.add(new ConstructionArc(
                Line.reverseOf(line1),
                0));
        elements.add(line1);


        // Vertical line of R
        Line line3 = Line.ofCoordinates(this, line1.endX, line1.startY, line1.endX, line1.endY );
        elements.add(Line.ofVector(this, line3.length, line1.angle, line1.endX, line1.endY).persistent(false));
        elements.add(new ConstructionArc(Line.reverseOf(line3), line1.angle));

        // Bottom line of Z
        Line line2 = Line.ofCoordinates(this, line1.endX, line1.startY, line1.startX, line1.startY);
        elements.add(
                new CompoundElement(
                        line3,
                        line2
                )
        );

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
        background(BG_COLOR);

        strokeWeight(STROKE_WEIGHT);
//        strokeCap(PROJECT);
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
            background(BG_COLOR);
        }

        if (mousePressed) {
            if (mouseButton == RIGHT) {
                frameCounter--;
            }
        } else {
            frameCounter++;
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[] {
//                "--present",
                ZR_Logo.class.getName() });
    }
}
