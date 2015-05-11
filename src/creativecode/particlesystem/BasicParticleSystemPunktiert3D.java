package creativecode.particlesystem;

import peasy.PeasyCam;
import processing.core.PApplet;
import punktiert.math.Vec;
import punktiert.physics.BAttraction;
import punktiert.physics.BehaviorInterface;
import punktiert.physics.VParticle;
import punktiert.physics.VPhysics;

public class BasicParticleSystemPunktiert3D extends PApplet {

    VPhysics    physics;

    BAttraction attractor;

    PeasyCam    cam;

    @Override
    public void setup() {
        size(800, 600, OPENGL);
        physics = new VPhysics(new Vec(0, 0, -200), new Vec(width, height, 200), true);
        physics.setfriction(0.01f);

        attractor = new BAttraction(new Vec(width / 3, height / 3), 100, 0.1f);
        physics.addBehavior(attractor);

        cam = new PeasyCam(this, 100);
    }

    @Override
    public void draw() {
        background(255);

        attractor.setAttractor(new Vec(mouseX, mouseY));

        Vec vec = new Vec(width / 2, height / 2, 0);
        VParticle particle = new VParticle(vec, 3f, 2f).addForce(new Vec(random(-1, 1), random(-1, 1), random(-1, 1)));
        particle.addBehavior(new BLifespan(200));
        physics.addParticle(particle);

        physics.update();

        noFill();
        stroke(0);
        for (VParticle p : physics.particles) {
            strokeWeight(p.radius * 2);
            point(p.x, p.y, p.z);
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

    public static void main(String args[]) {
        PApplet.main(new String[] { BasicParticleSystemPunktiert3D.class.getName() });
    }
}