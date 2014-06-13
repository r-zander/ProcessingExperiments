package generativeLogo.zr_logo;

import processing.core.PApplet;

public abstract class Element {

    public class DrawAttributes {

        public float strokeWeight;

        public int   color;

        public DrawAttributes(float strokeWeight, int color) {
            this.strokeWeight = strokeWeight;
            this.color = color;
        }

        public DrawAttributes(float strokeWeight, float gray) {
            this.strokeWeight = strokeWeight;
            gray(gray);
        }

        public void gray(float gray) {
            color = parent.color(gray);
        }

        public Element apply() {
            parent.strokeWeight(strokeWeight);
            parent.stroke(color);
            parent.noFill();
            return Element.this;
        }

    }

    protected final PApplet parent;

    public int              frameCount = ZR_Logo.DEFAULT_FRAME_COUNT;

    public DrawAttributes   drawAttributes;

    public boolean          persistent;

    public Element(PApplet parent) {
        this.parent = parent;
        persistent(true);
    }

    public DrawAttributes newPersistentDrawAttributes() {
        return new DrawAttributes(ZR_Logo.STROKE_WEIGHT, 0f);
    }

    public DrawAttributes newConstructedDrawAttributes() {
        return new DrawAttributes(ZR_Logo.STROKE_WEIGHT_CONSTRUCTED, 128f);
    }

    public Element persistent(boolean persistent) {
        this.persistent = persistent;
        if (persistent) {
            drawAttributes = newPersistentDrawAttributes();
        } else {
            drawAttributes = newConstructedDrawAttributes();
        }
        return this;
    }

    protected void calculateFrameCount(float length) {
        frameCount = ZR_Logo.round(length / ZR_Logo.DRAW_SPEED);
    }

    public void draw(float percentage) {
        drawAttributes.apply();
        drawShape(percentage);
    }

    protected abstract void drawShape(float percentage);

}