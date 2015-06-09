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

    VPhysics    physics;

    Interaction currentInteraction;

    float       buildPadding = 3;

    Grid        grid;

    List<Car>   cars         = new ArrayList<Car>();

    @Override
    public void setup() {
        $ = this;
        size(displayWidth, displayHeight, P2D);

        physics = new VPhysics(new Vec(), new Vec(width, height), false);

        grid = new Grid();
    }

    @Override
    public void draw() {
        background(Colors.BACKGROUND);

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
    }

    void drawFPS() {
        fill(Car.COLOR);
        textSize(12);
        textAlign(CENTER, BOTTOM);
        text(frameRate, 50, 50);
    }

    private void changeGrid(CellState newState) {

        int intensity = currentInteraction.frames;

        float buildDiameter = intensity * grid.cellDimension;

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
                if (grid.isState(gridX, gridY, newState)) {
                    continue;
                }
                float x = grid.getX(gridX);
                float y = grid.getY(gridY);
                if (ellipse.contains(x + grid.cellDimension / 2, y + grid.cellDimension / 2)) {
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
                currentMouseButton = mouseButton;
            } else if (newGridX != gridX || newGridY != gridY) {
                float frameChange = sqrt(sq(newGridX - gridX) + sq(newGridY - gridY));
                frames -= round(frameChange);
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
