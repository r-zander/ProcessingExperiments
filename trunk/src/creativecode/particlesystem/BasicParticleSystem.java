package creativecode.particlesystem;

import processing.core.PApplet;
import processing.core.PVector;

public class BasicParticleSystem extends PApplet {

    ParticleSystem particleSystem1;

    ParticleSystem particleSystem2;

    @Override
    public void setup() {
        size(800, 600);
        particleSystem1 = new ParticleSystem(this, new PVector(width / 2f, height / 2f));
        particleSystem2 = new ParticleSystem(this, new PVector(10, 10));
    }

    @Override
    public void draw() {
        background(255);
        particleSystem1.run();
        particleSystem1.addParticle();

        particleSystem2.run();
        particleSystem2.addParticle();
    }

    public static void main(String[] args) {
        PApplet.main(new String[] { BasicParticleSystem.class.getName() });
    }
}