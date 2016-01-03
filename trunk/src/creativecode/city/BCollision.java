package creativecode.city;

import oscP5.OscMessage;
import punktiert.math.Vec;
import punktiert.physics.BehaviorInterface;
import punktiert.physics.VParticle;

/**
 * Copy of punktiert.physics.BCollision with Event Support
 * 
 * @author rza
 */
public class BCollision implements BehaviorInterface {

    float limit;

    float offset;

    public BCollision() {
        limit = 0.2f;
        offset = 0;
    }

    /**
     * proportional offset of the particle radius for offset (radius*(1-offset))
     */
    public BCollision(float offset) {
        limit = 0.2f;
        this.offset = offset;
    }

    @Override
    public void apply(VParticle p) {
        Vec sum = new Vec();
        int count = 1;
        float radius = p.getRadius() * (1.0f - offset);

        for (VParticle neighbor : p.neighbors) {
            if (neighbor == p)
                continue;
            Vec delta = p.sub(neighbor);

            float dist = delta.mag();
            if (dist < radius + neighbor.getRadius()) {
                Vec f = delta.multSelf((radius + neighbor.getRadius() - dist) / (radius + neighbor.getRadius()));

                sum.addSelf(f);
                count++;
            }
        }

        sum.multSelf(1.0f / count);
        sum.limit(limit);
        p.addForce(sum);
    }

    public void onCollision(VParticle p, Vec force) {
        /* in the following different ways of creating osc messages are shown by example */
        OscMessage message = new OscMessage("/generativeCity/onCollision");

        message.add(p.x());
        message.add(p.y());
        message.add(force.mag());

        GenerativeCity.$.sendOscMessage(message);
    }

    public float getLimit() {
        return limit;
    }

    public float getOffset() {
        return offset;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    public BCollision setLimit(float limit) {
        this.limit = limit;
        return this;
    }

}