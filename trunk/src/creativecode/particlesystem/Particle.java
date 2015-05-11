package creativecode.particlesystem;

import processing.core.PApplet;
import processing.core.PVector;

class Particle {

    PApplet   app;

    PVector   location;

    PVector   velocity;

    PVector   acceleration;

    int       lifespan;

    PVector[] previousLocations;

    public Particle(PApplet app, PVector location) {
        this.app = app;
        acceleration = new PVector(app.random(-0.1f, 0.1f), app.random(0, 0.1f));
        velocity = new PVector(app.random(-1, 1), app.random(-2, 0));
        this.location = location.get();

        lifespan = 150;

        previousLocations = new PVector[lifespan + 1];
    }

    public void run() {
        update();
        draw();
    }

    private void update() {
        velocity.add(acceleration);
        location.add(velocity);
        previousLocations[lifespan] = location.get();
        lifespan--;
    }

    private void draw() {
        app.noStroke();
        app.fill(0);
        app.ellipse(location.x, location.y, 2, 2);

        app.stroke(0);
        app.noFill();
        app.beginShape();
        for (int i = 0; i < previousLocations.length; i++) {
            if (previousLocations[i] != null) {
                app.vertex(previousLocations[i].x, previousLocations[i].y);
            }
        }

        app.endShape();
    }

    public boolean isAlive() {
        return lifespan > 0;
    }
}