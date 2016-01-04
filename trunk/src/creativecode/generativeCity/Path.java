package creativecode.generativeCity;

import static creativecode.generativeCity.GenerativeCity.*;
import static processing.core.PConstants.*;

import java.util.ArrayList;

import punktiert.math.Vec;
// The Nature of Code
// Daniel Shiffman
// http://natureofcode.com

// Path Following

class Path {

    // A Path is an arraylist of points (PVector objects)
    ArrayList<Vec> points;

    // A path has a radius, i.e how far is it ok for the boid to wander off
    float          radius;

    Path() {
        // Arbitrary radius of 20
        radius = 20;
        points = new ArrayList<Vec>();
    }

    // Add a point to the path
    void addPoint(float x, float y) {
        Vec point = new Vec(x, y);
        points.add(point);
    }

    Vec getPoint(int index) {
        return points.get(index);
    }

    int length() {
        return points.size();
    }

    // Draw the path
    void display() {
        $.strokeJoin(ROUND);

        // Draw thick line for radius
        $.stroke(175);
        $.strokeWeight(radius * 2);
        $.noFill();
        $.beginShape();
        for (Vec v : points) {
            $.vertex(v.x, v.y);
        }
        $.endShape(CLOSE);
        // Draw thin line for center of path
        $.stroke(0);
        $.strokeWeight(1);
        $.noFill();
        $.beginShape();
        for (Vec v : points) {
            $.vertex(v.x, v.y);
        }
        $.endShape(CLOSE);
    }
}