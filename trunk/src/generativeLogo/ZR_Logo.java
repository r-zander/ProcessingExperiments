package generativeLogo;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import util.TwoDimensional;

public class ZR_Logo extends PApplet {

    private static final long serialVersionUID    = -56589606646834162L;

    private int               frameCounter        = 0;

    private int               finishFrame;

    private static final int  DEFAULT_FRAME_COUNT = 30;

    private abstract static class Element {

        public int     frameCount = DEFAULT_FRAME_COUNT;

        public boolean persistent;

        public abstract void draw(PApplet parent, float percentage);
    }

    public static class Line extends Element {

        public final float length;

        public final float angle;

        public final float startX;

        public final float startY;

        public final float endX;

        public final float endY;

        public float       gray = 0;

        private Line(float length, float angle, float startX, float startY, float endX, float endY) {
            this.length = length;
            this.angle = angle;
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
        }

        public static Line ofVector(float length, float angle, float startX, float startY) {
            return new Line(length, angle, startX, startY, startX + cos(angle) * length, startY + sin(angle) * length);
        }

        public static Line ofCoordinates(float startX, float startY, float endX, float endY) {
            return new Line(dist(startX, startY, endX, endY), TwoDimensional.angleBetween(startX, startY, endX, endY)
                    - PI, startX, startY, endX, endY);
        }

        @Override
        public void draw(PApplet parent, float percentage) {
            parent.stroke(gray);
            parent.line(startX, startY, startX + (endX - startX) * percentage, startY + (endY - startY) * percentage);
        }
    }

    public static class ConstructionArc extends Element {

        public final Line constructedLine;

        private float     startAngle;

        private float     endAngle;

        public ConstructionArc(Line constructedLine, float startAngle) {
            this.constructedLine = constructedLine;
            if (constructedLine.angle < startAngle) {
                this.startAngle = startAngle - TWO_PI;
                this.endAngle = constructedLine.angle;
            } else {
                this.startAngle = startAngle;
                this.endAngle = constructedLine.angle;
            }
        }

        @Override
        public void draw(PApplet parent, float percentage) {
            parent.stroke(128);
            parent.arc(
                    constructedLine.startX,
                    constructedLine.startY,
                    constructedLine.length * 2,
                    constructedLine.length * 2,
                    startAngle,
                    startAngle + (endAngle - startAngle) * percentage);
        }
    }

    public static class Bezier extends Element {

        private final float x1;

        private final float y1;

        private final float x2;

        private final float y2;

        private final float x3;

        private final float y3;

        private final float x4;

        private final float y4;

        public Bezier(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.x3 = x3;
            this.y3 = y3;
            this.x4 = x4;
            this.y4 = y4;
        }

        @Override
        public void draw(PApplet parent, float percentage) {
            if (percentage == 1) {
                parent.strokeWeight(5);
                parent.noFill();
                parent.stroke(0);
                parent.bezier(x1, y1, x2, y2, x3, y3, x4, y4);
                return;
            }
            parent.stroke(128);
            parent.line(x1, y1, x2, y2);
            parent.line(x2, y2, x3, y3);
            parent.line(x3, y3, x4, y4);

            float q1x = x1 + (x2 - x1) * percentage;
            float q1y = y1 + (y2 - y1) * percentage;
            float q2x = x2 + (x3 - x2) * percentage;
            float q2y = y2 + (y3 - y2) * percentage;
            float q3x = x3 + (x4 - x3) * percentage;
            float q3y = y3 + (y4 - y3) * percentage;
            parent.stroke(0xff00ff00);
            parent.line(q1x, q1y, q2x, q2y);
            parent.line(q2x, q2y, q3x, q3y);

            float r1x = q1x + (q2x - q1x) * percentage;
            float r1y = q1y + (q2y - q1y) * percentage;
            float r2x = q2x + (q3x - q2x) * percentage;
            float r2y = q2y + (q3y - q2y) * percentage;
            parent.stroke(0xff0000ff);
            parent.line(r1x, r1y, r2x, r2y);

            float bx = r1x + (r2x - r1x) * percentage;
            float by = r1y + (r2y - r1y) * percentage;
            parent.fill(0);
            parent.noStroke();
            parent.ellipse(bx, by, 10, 10);
            parent.stroke(0);
            parent.strokeWeight(5);
            parent.noFill();
            parent.bezier(x1, y1, q1x, q1y, r1x, r1y, bx, by);
        }

        public float getX(PApplet parent, float percentage) {
            return parent.bezierPoint(x1, x2, x3, x4, percentage);
        }

        public float getY(PApplet parent, float percentage) {
            return parent.bezierPoint(y1, y2, y3, y4, percentage);
        }

    }

    private float centerX;

    private float centerY;

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

        final Line line1 = Line.ofCoordinates(centerX, Icon.minY, Icon.minX, Icon.maxY);
        line1.persistent = true;

        elements.add(new ConstructionArc(line1, 0));
        elements.add(line1);
        Line line2 = Line.ofCoordinates(line1.endX, line1.endY, line1.startX, line1.endY);
        line2.persistent = true;
        elements.add(new ConstructionArc(line2, line1.angle - PI));
        elements.add(line2);
        Line line3 = Line.ofCoordinates(line2.endX, line2.endY, line2.endX, line1.startY);
        line3.persistent = true;
        elements.add(new ConstructionArc(line3, line2.angle - PI));
        elements.add(line3);
        elements.add(new ConstructionArc(Line.ofVector(line1.length / 2, PI, line3.endX, line3.endY), line3.angle + PI));
        float length = Icon.width * .2f;
        Line line4 = Line.ofVector(length, 0, Icon.minX + Icon.width * .25f - length / 2, centerY);
        line4.persistent = true;
        elements.add(line4);
        Line constructionLine1 = Line.ofCoordinates(line4.endX, line4.endY, line1.startX, line4.endY);
        constructionLine1.gray = 128;
        elements.add(constructionLine1);
        Bezier bezier =
                new Bezier(constructionLine1.endX, constructionLine1.endY, Icon.maxX, line4.endY, line1.startX
                        + Icon.width * .3f, line1.startY, line1.startX, line1.startY);
        bezier.frameCount = 300;
        bezier.persistent = true;
        elements.add(bezier);

        Line line5 = Line.ofCoordinates(bezier.x4, bezier.y4, Icon.minX, bezier.y4);
        line5.persistent = true;
        elements.add(line5);

        Line line6 = Line.ofCoordinates(bezier.getX(this, .2f), bezier.getY(this, .2f), Icon.maxX, Icon.maxY);
        line6.persistent = true;
        elements.add(line6);

        for (Element element : elements) {
            finishFrame += element.frameCount;
        }
        finishFrame += 120;
    }

    @Override
    public void draw() {
        fill(255, 15);
        noStroke();
        rect(0, 0, width, height);

        strokeWeight(5);
        noFill();
        int relativeFrameCount = frameCounter;
        for (Element element : elements) {
            if (relativeFrameCount <= element.frameCount) {
                element.draw(this, relativeFrameCount / (float) element.frameCount);
                /*
                 * Aktuelles Element wurde gefunden - Schleife abbrechen.
                 */
                break;
            } else {
                if (element.persistent) {
                    element.draw(this, 1);
                }
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

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", ZR_Logo.class.getName() });
    }
}