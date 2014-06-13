package generativeLogo.zr_logo;

import processing.core.PApplet;
import util.TwoDimensional;

public class Line extends Element {

    public final float length;

    public final float angle;

    public final float startX;

    public final float startY;

    public final float endX;

    public final float endY;

    private Line(PApplet parent, float length, float angle, float startX, float startY, float endX, float endY) {
        super(parent);
        this.length = length;
        this.angle = angle;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        calculateFrameCount(length);
    }

    public static Line ofVector(PApplet parent, float length, float angle, float startX, float startY) {
        return new Line(parent, length, angle, startX, startY, startX + ZR_Logo.cos(angle) * length, startY + ZR_Logo.sin(angle)
                * length);
    }

    public static Line ofCoordinates(PApplet parent, float startX, float startY, float endX, float endY) {
        return new Line(parent, ZR_Logo.dist(startX, startY, endX, endY), TwoDimensional.angleBetween(
                startX,
                startY,
                endX,
                endY) - ZR_Logo.PI, startX, startY, endX, endY);
    }

    @Override
    public void drawShape(float percentage) {
        parent.line(startX, startY, startX + (endX - startX) * percentage, startY + (endY - startY) * percentage);
    }
}