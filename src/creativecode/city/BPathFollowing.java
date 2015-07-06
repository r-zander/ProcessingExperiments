package creativecode.city;

import static creativecode.city.GenerativeCity.*;
import static processing.core.PApplet.*;

import java.util.List;

import punktiert.math.Vec;
import punktiert.physics.BehaviorInterface;
import punktiert.physics.VParticle;

public class BPathFollowing implements BehaviorInterface {

    Path  path;

    float maxforce = 3;   // Maximum steering force

    float maxspeed = 0.3f;    // Maximum speed

    public BPathFollowing(Path path) {
        this.path = path;
    }

    @Override
    public void apply(VParticle p) {
        // Follow path force
        Vec f = follow(p);
        // Separate from other boids force
        Vec s = separate(p, $.cars);
        // Arbitrary weighting
        f.mult(3);
        s.mult(1);
        // Accumulate in acceleration
        p.addForce(f);
        p.addForce(s);

    }

    // This function implements Craig Reynolds' path following algorithm
    // http://www.red3d.com/cwr/steer/PathFollow.html
    Vec follow(VParticle p) {

        // Predict location 25 (arbitrary choice) frames ahead
        Vec predict = p.getVelocity().copy();
        predict.normalize();
        predict.mult(25);
        Vec predictLoc = p.add(predict);

        // Now we must find the normal to the path from the predicted location
        // We look at the normal for each line segment and pick out the closest one
        Vec normal = null;
        Vec target = null;
        float worldRecord = 1000000;  // Start with a very high worldRecord distance that can easily be beaten

        // Loop through all points of the path
        for (int i = 0; i < path.points.size(); i++) {

            // Look at a line segment
            Vec a = path.points.get(i);
            Vec b = path.points.get((i + 1) % path.points.size()); // Note Path has to wraparound

            // Get the normal point to that line
            Vec normalPoint = getNormalPoint(predictLoc, a, b);

            // Check if normal is on line segment
            Vec dir = b.sub(a);
            // If it's not within the line segment, consider the normal to just be the end of the line segment (point b)
            // if (da + db > line.mag()+1) {
            if (normalPoint.x < min(a.x, b.x) || normalPoint.x > max(a.x, b.x) || normalPoint.y < min(a.y, b.y)
                    || normalPoint.y > max(a.y, b.y)) {
                normalPoint = b.copy();
                // If we're at the end we really want the next line segment for looking ahead
                a = path.points.get((i + 1) % path.points.size());
                b = path.points.get((i + 2) % path.points.size());  // Path wraps around
                dir = b.sub(a);
            }

            // How far away are we from the path?
            float d = predictLoc.dist(normalPoint);
            // Did we beat the worldRecord and find the closest line segment?
            if (d < worldRecord) {
                worldRecord = d;
                normal = normalPoint;

                // Look at the direction of the line segment so we can seek a little bit ahead of the normal
                dir.normalize();
                // This is an oversimplification
                // Should be based on distance to path & velocity
                dir.mult(25);
                target = normal.copy();
                target.add(dir);

            }
        }

        // Draw the debugging stuff
        if ($.debug) {
            // Draw predicted future location
            $.stroke(0);
            $.fill(0);
            $.line(p.x, p.y, predictLoc.x, predictLoc.y);
            $.ellipse(predictLoc.x, predictLoc.y, 4, 4);

            // Draw normal location
            $.stroke(0);
            $.fill(0);
            $.ellipse(normal.x, normal.y, 4, 4);
            // Draw actual target (red if steering towards it)
            $.line(predictLoc.x, predictLoc.y, target.x, target.y);
            if (worldRecord > path.radius)
                $.fill(255, 0, 0);
            $.noStroke();
            $.ellipse(target.x, target.y, 8, 8);
        }

        // Only if the distance is greater than the path's radius do we bother to steer
        if (worldRecord > path.radius) {
            return seek(p, target);
        } else {
            return new Vec(0, 0);
        }
    }

    // A function to get the normal point from a point (p) to a line segment (a-b)
    // This function could be optimized to make fewer new Vector objects
    Vec getNormalPoint(Vec p, Vec a, Vec b) {
        // Vector from a to p
        Vec ap = p.sub(a);
        // Vector from a to b
        Vec ab = b.sub(a);
        ab.normalize(); // Normalize the line
        // Project vector "diff" onto line by using the dot product
        ab.mult(ap.dot(ab));
        Vec normalPoint = a.add(ab);
        return normalPoint;
    }

    // A method that calculates and applies a steering force towards a target
    // STEER = DESIRED MINUS VELOCITY
    Vec seek(VParticle p, Vec target) {
        Vec desired = target.sub(p);  // A vector pointing from the location to the target

        // Normalize desired and scale to maximum speed
        desired.normalize();
        desired.mult(maxspeed);
        // Steering = Desired minus Velocationity
        Vec steer = desired.sub(p.getVelocity());
        steer.limit(maxforce);  // Limit to maximum steering force

        return steer;
    }

    // Separation
    // Method checks for nearby boids and steers away
    Vec separate(VParticle p, List<Car> cars) {
        float desiredseparation = p.getRadius() * 2;
        Vec steer = new Vec(0, 0, 0);
        int count = 0;
        // For every boid in the system, check if it's too close
        for (int i = 0; i < cars.size(); i++) {
            VParticle other = cars.get(i).particle;
            float d = p.dist(other);
            // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
            if ((d > 0) && (d < desiredseparation)) {
                // Calculate vector pointing away from neighbor
                Vec diff = p.sub(other);
                diff.normalize();
                diff.div(d);        // Weight by distance
                steer.add(diff);
                count++;            // Keep track of how many
            }
        }
        // Average -- divide by how many
        if (count > 0) {
            steer.div((float) count);
        }

        // As long as the vector is greater than 0
        if (steer.mag() > 0) {
            // Implement Reynolds: Steering = Desired - Velocity
            steer.normalize();
            steer.mult(maxspeed);
            steer.sub(p.getVelocity());
            steer.limit(maxforce);
        }
        return steer;
    }

}
