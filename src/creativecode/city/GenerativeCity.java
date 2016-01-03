package creativecode.city;

import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import netP5.NetAddress;
import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;
import punktiert.math.Vec;
import punktiert.physics.VParticle;
import punktiert.physics.VPhysics;
import creativecode.city.GridCell.CellState;

public class GenerativeCity extends PApplet {

    /**
     * Currently running instance
     */
    public static GenerativeCity $;

    static class Colors {

        static final int BACKGROUND = 0x00000000;

    }

    VPhysics               physics;

    Interaction            currentInteraction;

    float                  buildPadding     = 3;

    Grid                   grid;

    List<Car>              cars             = new ArrayList<Car>();

    public boolean         debug            = false;

    public List<VParticle> particlesToReAdd = new ArrayList<VParticle>();

    OscP5                  osc;

    private NetAddress     remoteLocation;

    @Override
    public void setup() {
        $ = this;
        size(displayWidth, displayHeight, P2D);

        /* start oscP5, listening for incoming messages at port 12000 */
        osc = new OscP5(this, 12000);
        /*
         * myRemoteLocation is a NetAddress. a NetAddress takes 2 parameters,
         * an ip address and a port number. myRemoteLocation is used as parameter in
         * oscP5.send() when sending osc packets to another computer, device,
         * application. usage see below. for testing purposes the listening port
         * and the port of the remote location address are the same, hence you will
         * send messages back to this sketch.
         */
        remoteLocation = new NetAddress("127.0.0.1", 12000);

        physics = new VPhysics();
        physics.setBox(new Vec(), new Vec(width, height));
        physics.setBounceSpace(false);
        physics.setWrappedSpace(false);

        physics.setfriction(.3f);

        grid = new Grid();

        sendOscMessage(new OscMessage("/generativeCity/onClear"));
    }

    /* incoming osc message are forwarded to the oscEvent method. */
    void oscEvent(OscMessage theOscMessage) {
        /* print the address pattern and the typetag of the received OscMessage */
        print("### received an osc message.");
        print(" addrpattern: " + theOscMessage.addrPattern());
        println(" typetag: " + theOscMessage.typetag());
    }

    @Override
    public void draw() {
        fill(Colors.BACKGROUND, 60);
        noStroke();
        rect(0, 0, width, height);

        physics.update();

        for (VParticle particle : particlesToReAdd) {
            physics.addParticle(particle);
        }
        particlesToReAdd.clear();

        if (mousePressed) {
            if (currentInteraction == null) {
                currentInteraction = new Interaction();
            } else {
                currentInteraction.update();
            }

            switch (mouseButton) {
                case LEFT:
                    changeGrid(CellState.BUILT);
                    break;
                case RIGHT:
                    changeGrid(CellState.EMPTY);
                    break;
                default:
                    break;
            }
        }

        grid.draw();

        if (!debug) {
            for (Car car : cars) {
                car.draw();
            }
        }

        grid.step();

        drawFPS();

//        filter(GRAY);
    }

//    int colorMode = RGB;
//
//    @Override
//    public void colorMode(int mode) {
//        super.colorMode(mode);
//        colorMode = mode;
//    }
//
//    @Override
//    public void fill(float v1, float v2, float v3) {
//        if (colorMode == HSB) {
//            super.fill(v1);
//        } else {
//            super.fill(brightness(color(v1, v2, v3)));
//        }
//    }
//
//    @Override
//    public void fill(int rgb) {
//        super.fill(brightness(rgb));
//    }
//
//    @Override
//    public void stroke(float v1, float v2, float v3) {
//        if (colorMode == HSB) {
//            super.stroke(v1);
//        } else {
//            super.stroke(brightness(color(v1, v2, v3)));
//        }
//    }
//
//    @Override
//    public void stroke(int rgb) {
//        super.stroke(brightness(rgb));
//    }

    @Override
    public void mouseReleased() {
        if (currentInteraction.currentMouseButton == LEFT) {
            grid.finishBlock();
        }
    }

    @Override
    public void keyPressed() {
        if (key == 'd') {
            debug = !debug;
        }
    }

    void drawFPS() {
        fill(Car.COLOR);
        textSize(12);
        textAlign(CENTER, BOTTOM);
        text(frameRate, 50, 50);
    }

    private void changeGrid(CellState newState) {

        int intensity = currentInteraction.frames;

        float buildDiameter = intensity * Grid.cellDimension;

        Ellipse2D.Float ellipse =
                new Ellipse2D.Float(
                        mouseX - buildDiameter / 2,
                        mouseY - buildDiameter / 2,
                        buildDiameter,
                        buildDiameter);

        int half_intensity = intensity / 2; // automatically rounded down
        for (int gridX = currentInteraction.gridX - half_intensity; gridX <= currentInteraction.gridX + half_intensity; gridX++) {
            if (gridX < 0) {
                continue;
            }
            if (gridX >= grid.getMaxGridX()) {
                break;
            }
            for (int gridY = currentInteraction.gridY - half_intensity; gridY <= currentInteraction.gridY
                    + half_intensity; gridY++) {
                if (gridY < 0) {
                    continue;
                }
                if (gridY >= grid.getMaxGridY()) {
                    break;
                }
                if (grid.isState(gridX, gridY, newState) || grid.isState(gridX, gridY, CellState.STREET)
                        || grid.isState(gridX, gridY, CellState.BLOCKED)) {
                    continue;
                }
                float x = grid.getX(gridX);
                float y = grid.getY(gridY);
                if (ellipse.contains(x + Grid.cellDimension / 2, y + Grid.cellDimension / 2)) {
                    grid.changeState(gridX, gridY, newState);
                }
            }
        }
    }

    public void sendOscMessage(OscMessage message) {
        /* send the message */
        osc.send(message, remoteLocation);
    }

    class Interaction {

        int frames = 1;

        int gridX;

        int gridY;

        int currentMouseButton;

        public Interaction() {
            gridX = grid.getGridX(mouseX);
            gridY = grid.getGridY(mouseY);
            currentMouseButton = mouseButton;
        }

        void update() {
            int newGridX = grid.getGridX(mouseX);
            int newGridY = grid.getGridY(mouseY);

            if (currentMouseButton != mouseButton) {
                gridX = newGridX;
                gridY = newGridY;
                frames = 1;
                if (currentMouseButton == LEFT) {
                    grid.finishBlock();
                }
                currentMouseButton = mouseButton;
            } else if (newGridX != gridX || newGridY != gridY) {
                float frameChange = sqrt(sq(newGridX - gridX) + sq(newGridY - gridY));
                frames -= round(frameChange);

                if (frames < 0 && currentMouseButton == LEFT) {
                    grid.finishBlock();
                }

                if (frames <= 0) {
                    frames = 1;
                }
                gridX = newGridX;
                gridY = newGridY;
            } else {
                frames++;
            }
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", GenerativeCity.class.getName() });
    }
}
