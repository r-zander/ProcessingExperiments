package creativecode.particlesystem;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class ParticleSystem {

    private PApplet     app;

    ArrayList<Particle> particles;

    PVector             emitter;

    public ParticleSystem(PApplet app, PVector location) {
        this.app = app;
        emitter = location.get();
        particles = new ArrayList<Particle>();
    }

    public void addParticle() {
        particles.add(new Particle(app, emitter));
    }

    public void run() {
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle particle = particles.get(i);
            particle.run();
            if (!particle.isAlive()) {
                particles.remove(i);
            }
        }
    }

}
