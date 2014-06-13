package seminar09_arrays;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import processing.core.PApplet;
import util.structures.Maps;
import util.structures.Point;

public class ShortestDistance extends PApplet {

    private static final long serialVersionUID = -56589606646834162L;

    private static class PointPair {

        int index1;

        int index2;

        public PointPair(int index1, int index2) {
            this.index1 = index1;
            this.index2 = index2;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + index1;
            result = prime * result + index2;
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
            PointPair other = (PointPair) obj;
            if (index1 != other.index1)
                return false;
            if (index2 != other.index2)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "PointPair [" + index1 + ", " + index2 + "]";
        }
    }

    static final int      ANZAHL    = 40;

    Point[]               points    = new Point[ANZAHL];

    Map<PointPair, Float> distances = new HashMap<ShortestDistance.PointPair, Float>(ANZAHL);

    @Override
    public void setup() {
        size(displayWidth, displayHeight);
        background(255);

        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(random(width), random(height));
        }

        noStroke();
        fill(0);

        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            ellipse(p.x, p.y, 20, 20);
            text(i, p.x, p.y + 20);
            for (int j = i + 1; j < points.length; j++) {
                distances.put(new PointPair(i, j), p.dist(points[j]));
            }
        }
        distances = Maps.sortMapByValues(distances);

        int index = 0;
        strokeWeight(3);
        for (Entry<PointPair, Float> entry : distances.entrySet()) {
            stroke(getColor(index / (float) distances.size()));
            Point point1 = points[entry.getKey().index1];
            Point point2 = points[entry.getKey().index2];
            line(point1.x, point1.y, point2.x, point2.y);
            Point textPosition = point1.intermediate(point2);
            text(entry.getValue(), textPosition.x, textPosition.y);
            index++;
        }

        noLoop();
    }

    private int getColor(float percentage) {
        if (percentage <= 0.5) {
            return color(255 * percentage / 0.5f, 255, 0);
        } else {
            return color(255, map(percentage, 0.5f, 1, 255, 0), 0);
        }
    }

    @Override
    public void draw() {

    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", ShortestDistance.class.getName() });
    }
}