package creativecode.city;

import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import punktiert.math.Vec;
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

    VPhysics       physics;

    Interaction    currentInteraction;

    float          buildPadding = 3;

    Grid           grid;

    List<Car>      cars         = new ArrayList<Car>();

    public boolean debug        = true;

    @Override
    public void setup() {
        $ = this;
        size(displayWidth, displayHeight, P2D);

        physics = new VPhysics();
        physics.setBox(new Vec(), new Vec(width, height));
        physics.setBounceSpace(false);
        physics.setWrappedSpace(true);

        physics.setfriction(.3f);

        grid = new Grid();

    }

    private void spawnCars() {
        for (int i = 0; i < 500; i++) {
            cars.add(new Car(random(Car.DIAMETER, width - Car.DIAMETER), random(Car.DIAMETER, height - Car.DIAMETER)));
        }
    }

    @Override
    public void draw() {
        fill(Colors.BACKGROUND, 60);
        noStroke();
        rect(0, 0, width, height);

        physics.update();

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

        for (Car car : cars) {
            car.draw();
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