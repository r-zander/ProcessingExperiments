package util;

import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PGraphics;

public class Colors {

    public static final int RED = 0xFFFF0000;

    public static int randomColor() {
        return Numbers.random(0xFF000000, 0xFFFFFFFF);
    }

    private static final Map<GradientKey, PGraphics> GRADIENT_CACHE = new HashMap<Colors.GradientKey, PGraphics>();

    public static void drawGradient(PApplet parent, int x, int y, float width, float height, int colorFrom,
            int colorTo, Axis axis) {

        GradientKey gradientKey = new GradientKey(width, height, colorFrom, colorTo, axis);
        PGraphics gradient = GRADIENT_CACHE.get(gradientKey);
        if (gradient == null) {

            gradient = new PGraphics();
            gradient.beginDraw();

            gradient.noFill();

            switch (axis) {
                case Y_AXIS: // Top to bottom gradient
                    for (int i = 0; i <= 0 + height; i++) {
                        float inter = PApplet.map(i, 0, height, 0, 1);
                        int c = gradient.lerpColor(colorFrom, colorTo, inter);
                        gradient.stroke(c);
                        gradient.line(0, i, width, i);
                    }
                    break;
                case X_AXIS: // Left to right gradient
                    for (int i = 0; i <= 0 + width; i++) {
                        float inter = PApplet.map(i, 0, width, 0, 1);
                        int c = gradient.lerpColor(colorFrom, colorTo, inter);
                        gradient.stroke(c);
                        gradient.line(i, 0, i, 0 + height);
                    }
                    break;
            }

        }

        parent.image(gradient, x, y);
    }

    private static class GradientKey {

        private float width;

        private float height;

        private int   colorFrom;

        private int   colorTo;

        private Axis  axis;

        public GradientKey(float width, float height, int colorFrom, int colorTo, Axis axis) {
            this.width = width;
            this.height = height;
            this.colorFrom = colorFrom;
            this.colorTo = colorTo;
            this.axis = axis;
        }

        public float getWidth() {
            return width;
        }

        public void setWidth(float width) {
            this.width = width;
        }

        public float getHeight() {
            return height;
        }

        public void setHeight(float height) {
            this.height = height;
        }

        public int getColorFrom() {
            return colorFrom;
        }

        public void setColorFrom(int colorFrom) {
            this.colorFrom = colorFrom;
        }

        public int getColorTo() {
            return colorTo;
        }

        public void setColorTo(int colorTo) {
            this.colorTo = colorTo;
        }

        public Axis getAxis() {
            return axis;
        }

        public void setAxis(Axis axis) {
            this.axis = axis;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((axis == null) ? 0 : axis.hashCode());
            result = prime * result + colorFrom;
            result = prime * result + colorTo;
            result = prime * result + Float.floatToIntBits(height);
            result = prime * result + Float.floatToIntBits(width);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            GradientKey other = (GradientKey) obj;
            if (axis != other.axis)
                return false;
            if (colorFrom != other.colorFrom)
                return false;
            if (colorTo != other.colorTo)
                return false;
            if (Float.floatToIntBits(height) != Float.floatToIntBits(other.height))
                return false;
            if (Float.floatToIntBits(width) != Float.floatToIntBits(other.width))
                return false;
            return true;
        }
    }

}
