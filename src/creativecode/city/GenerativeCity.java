package creativecode.city;

import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

public class GenerativeCity extends PApplet {

    /**
     * Currently running instance
     */
    public static GenerativeCity $;

    static class Colors {

        static final int BACKGROUND = 0x00000000;

    }

    float       buildPadding = 3;

    Grid        grid         = new Grid();

    Interaction currentInteraction;

    @Override
    public void setup() {
        $ = this;
        size(displayWidth, displayHeight, P2D);

        frameRate(60);
        background(0);
        grid.draw();
    }

    @Override
    public void draw() {

        if (mousePressed) {
            switch (mouseButton) {
                case LEFT:
                    if (currentInteraction == null) {
                        currentInteraction = new Interaction(mouseX, mouseY);
                    } else {
                        currentInteraction.update(mouseX, mouseY);
                    }
                    drawBuildings();
                    break;
                default:
                    break;
            }
        }
    }

    private void drawBuildings() {

        int intensity = currentInteraction.frames;
        float buildDiameter = intensity * grid.cellDimension;
        stroke(0xffffff00);
        noFill();
        ellipse(mouseX, mouseY, buildDiameter, buildDiameter);

        Ellipse2D.Float ellipse = new Ellipse2D.Float(mouseX, mouseY, buildDiameter, buildDiameter);

        List<Integer> xs = new ArrayList<Integer>();
        for (int gridX = currentInteraction.gridX - intensity / 2; gridX < currentInteraction.gridX + intensity / 2; gridX++) {
            xs.add(gridX);
            for (int gridY = currentInteraction.gridY - intensity / 2; gridY < currentInteraction.gridY + intensity / 2; gridY++) {
                float x = grid.getX(gridX);
                float y = grid.getY(gridY);
                if (ellipse.contains(x + grid.cellDimension / 2, y + grid.cellDimension / 2)) {
                    grid.replaceCell(gridX, gridY);
                    new Building(
                            x + buildPadding,
                            y + buildPadding,
                            grid.cellDimension - buildPadding * 2,
                            grid.cellDimension - buildPadding * 2).draw();
                }
            }
        }

//        println("Cell: " + currentInteraction.gridX + " Intensity: " + intensity + " (Cells: " + xs + ")");

    }

    @Override
    public void keyPressed() {
        if (key == ' ') {
            noLoop();
        }
    }

    class Interaction {

        int frames = 1;

        int gridX;

        int gridY;

        public Interaction(float mouseX, float mouseY) {
            gridX = grid.getGridX(mouseX);
            gridY = grid.getGridY(mouseY);
        }

        void update(float mouseX, float mouseY) {
            int newGridX = grid.getGridX(mouseX);
            int newGridY = grid.getGridY(mouseY);

            if (newGridX != gridX || newGridY != gridY) {
                frames = 1;
                gridX = newGridX;
                gridY = newGridY;
            } else {
                frames++;
            }
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[] {/* "--present", */GenerativeCity.class.getName() });
    }
}
