package creativecode.city;

import punktiert.math.Vec;
import punktiert.physics.BehaviorInterface;
import punktiert.physics.VParticle;

public class BPathFollowing2 implements BehaviorInterface {

    float   maxVelocity = 3;

    float   MAX_FORCE   = maxVelocity * 3;

    Street  path;

    int     currentNodeIndex;

    boolean forward;

    public BPathFollowing2(Street path, boolean forward) {
        this.path = path;
        this.forward = forward;
        if (forward) {
            currentNodeIndex = 0;
        } else {
            currentNodeIndex = path.length() - 1;
        }
    }

    @Override
    public void apply(VParticle p) {
        Vec steering = pathFollowing(p);

        if (steering == null) {
            return;
        }

//        p.addForce(steering);
        p.addVelocity(steering);
    }

    private Vec seek(VParticle p, Vec target) {
        Vec force;

        Vec desired = target.sub(p);
        desired.normalize();
        desired.multSelf(maxVelocity);

        force = desired.sub(p.getVelocity());

        return force;
    }

    private Vec pathFollowing(VParticle p) {
        Vec target = null;

        if (path != null) {
            target = path.getPoint(currentNodeIndex);

            if (p.dist(target) <= path.radius) {
                currentNodeIndex += forward ? 1 : -1;

                if (currentNodeIndex >= path.length() || currentNodeIndex < 0) {
                    forward = !forward;
                    currentNodeIndex += forward ? 1 : -1;
                    path.disableCarSpawn();
                }
            }
        }

        return target != null ? seek(p, target) : new Vec();
    }

    public void truncate(Vec vector, float max) {
        float i;

        i = max / vector.mag();
        i = i < 1.0f ? i : 1.0f;

        vector.multSelf(i);
    }

}
