package creativecode.particlesystem;

import processing.core.PApplet;
import punktiert.math.Vec;
import punktiert.physics.BAttraction;
import punktiert.physics.BehaviorInterface;
import punktiert.physics.VParticle;
import punktiert.physics.VPhysics;

public class BasicParticleSystemPunktiert extends PApplet {

    VPhysics    physics;

    BAttraction attractor;

    @Override
    public void setup() {
        size(800, 600);
        physics = new VPhysics();
        physics.setfriction(0.01f);

        attractor = new BAttraction(new Vec(width / 3, height / 3), 100, 0.1f);
        physics.addBehavior(attractor);
    }

    @Override
    public void draw() {
        background(255);

        attractor.setAttractor(new Vec(mouseX, mouseY));

        Vec vec = new Vec(width / 2, height / 2);
        VParticle particle = new VParticle(vec, 3f, 2f).addForce(new Vec(random(-1, 1), random(-1, 1)));
        particle.addBehavior(new BLifespan(200));
        physics.addParticle(particle);

        physics.update();

        noStroke();
        fill(0);
        for (VParticle p : physics.particles) {
            ellipse(p.x, p.y, p.getRadius() * 2, p.getRadius() * 2);
        }
    }

    class BLifespan implements BehaviorInterface {

        int lifespan;

        public BLifespan(int lifespan) {
            this.lifespan = lifespan;
        }

        @Override
        public void apply(VParticle p) {
            lifespan--;

            if (lifespan < 0) {
                ellipse(p.x, p.y, p.getRadius() * 4, p.getRadius() * 4);
                physics.removeParticle(p);
            }
        }

    }

    public static void main(String[] args) {
        PApplet.main(new String[] { BasicParticleSystemPunktiert.class.getName() });
    }
}