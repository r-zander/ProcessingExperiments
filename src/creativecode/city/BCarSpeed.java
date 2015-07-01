package creativecode.city;

import punktiert.math.Vec;
import punktiert.physics.BehaviorInterface;
import punktiert.physics.VParticle;

public class BCarSpeed implements BehaviorInterface {

    Vec previousVelocity;

    @Override
    public void apply(VParticle p) {

        if (previousVelocity != null) {
            float angleBetween = previousVelocity.heading() - p.getVelocity().heading();
            if (angleBetween == 0) {
                // Speed up
                p.scaleVelocity(3f);
            } else {
                // Speed down
                p.scaleVelocity(0.2f);
            }
        }

        previousVelocity = p.getVelocity().copy();
    }

}
