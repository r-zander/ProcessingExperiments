package generativeLogo.zr_logo;

import generativeLogo.zr_logo.Element.DrawAttributes;
import processing.core.PApplet;

public class Bezier extends Element {

    private final float    x1;

    private final float    y1;

    private final float    x2;

    private final float    y2;

    private final float    x3;

    private final float    y3;

    final float    x4;

    final float    y4;

    private DrawAttributes constructionDrawAttributes = newConstructedDrawAttributes();

    public Bezier(PApplet parent, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        super(parent);
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
    public void drawShape(float percentage) {
        if (percentage == 1) {
            parent.bezier(x1, y1, x2, y2, x3, y3, x4, y4);
            return;
        }

        constructionDrawAttributes.apply();
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
        drawAttributes.apply();
        parent.bezier(x1, y1, q1x, q1y, r1x, r1y, bx, by);
    }

    public float getX(PApplet parent, float percentage) {
        return parent.bezierPoint(x1, x2, x3, x4, percentage);
    }

    public float getY(PApplet parent, float percentage) {
        return parent.bezierPoint(y1, y2, y3, y4, percentage);
    }

}