package creativecode.city;

import static creativecode.city.GenerativeCity.*;
import static processing.core.PConstants.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pathfinder.GraphNode;
import punktiert.math.Vec;
import util.TwoDimensional;

public class Street extends Path {

    List<GridCell>             nodes     = new ArrayList<GridCell>();

    List<DebugPath>            path      = new ArrayList<DebugPath>();

    int                        steps;

    Vec                        spawnPointForward;

    Vec                        spawnPointBackward;

    ShiffmanPath               shiffmanPath;

    ArrayList<ShiffmanVehicle> vehicles  = new ArrayList<ShiffmanVehicle>();

    private boolean            spawnCars = true;

    public Street(List<GridCell> nodes, List<DebugPath> path) {

        cleanOutNodes(nodes);

        this.path = path;
        this.radius = Grid.cellDimension / 2;

//        shiffmanPath = new ShiffmanPath();
        for (GridCell cell : this.nodes) {
            addPoint(cell.xf() + radius, cell.yf() + radius);
//            shiffmanPath.addPoint(cell.xf() + radius, cell.yf() + radius);
        }

        for (DebugPath debugPath : path) {
            if ("Start Cell".equals(debugPath.title)) {
                GridCell spawnCell = debugPath.cell;
                spawnPointForward = new Vec(spawnCell.xf(), spawnCell.yf());
                break;
            }
        }

        for (DebugPath debugPath : path) {
            if ("End Cell".equals(debugPath.title)) {
                GridCell spawnCell = debugPath.cell;
                spawnPointBackward = new Vec(spawnCell.xf(), spawnCell.yf());
                break;
            }
        }

    }

    /**
     * Entferne alle überflüssigen nodes, die keine Ecken des Pfades sind.
     * 
     * @param nodes
     */
    void cleanOutNodes(List<GridCell> nodes) {
        Iterator<GridCell> iterator = nodes.iterator();
        if (iterator.hasNext()) {

            GridCell previousCell = iterator.next();
            float previousAngle = -1;

            while (iterator.hasNext()) {
                GridCell cell = iterator.next();
                float angle = TwoDimensional.angleBetween(previousCell.xf(), previousCell.yf(), cell.xf(), cell.yf());

                if (previousAngle != angle) {
                    this.nodes.add(previousCell);
                    previousAngle = angle;
                }

                previousCell = cell;
            }

            // Add last cell definitely
            this.nodes.add(previousCell);
        }
    }

    public void step() {
        steps++;
        if (spawnCars && steps % 20 == 0) {
            spawnCar(spawnPointForward, true);
            spawnCar(spawnPointBackward, false);
//            float maxspeed = 3;
//            float maxforce = 0.3f;
//            vehicles.add(new ShiffmanVehicle(new PVector(spawnPoint.x, spawnPoint.y), maxspeed, maxforce));
        }
    }

    private void spawnCar(Vec location, boolean forward) {
        Car car = new Car(location.x, location.y);
        car.particle.addBehavior(new BPathFollowing2(this, forward));
        $.cars.add(car);
    }

    public void draw() {
        if ($.debug) {
            for (DebugPath debugPath : path) {
                debugPath.draw();
            }

            Iterator<GridCell> iterator = nodes.iterator();
            if (!iterator.hasNext()) {
                return;
            }

            $.colorMode(HSB);
            $.strokeWeight(3);

            int hue = 0;

            GraphNode previousNode = iterator.next();

            while (iterator.hasNext()) {
                GraphNode node = iterator.next();
                $.stroke(hue, 255, 255);
                $.line(previousNode.xf(), previousNode.yf(), node.xf(), node.yf());
                previousNode = node;
                hue += 3;
                hue %= 256;
            }

            $.colorMode(RGB);

//            for (ShiffmanVehicle v : vehicles) {
//                // Path following and separation are worked on in this function
//                v.applyBehaviors(vehicles, shiffmanPath);
//                // Call the generic run method (update, borders, display, etc.)
//                v.run();
//            }
        }
    }

    public static class DebugPath {

        String   title;

        GridCell cell;

        public DebugPath(String title, GridCell cell) {
            this.title = title;
            this.cell = cell;
        }

        void draw() {
            $.textSize(10);
            $.fill(255);
            $.text(title, cell.xf(), cell.yf());

            $.rectMode(CORNER);
            $.stroke(128);
            $.strokeWeight(Grid.WEIGHT);
            $.rect(cell.xf(), cell.yf(), Grid.cellDimension, Grid.cellDimension);
        }
    }

    public void disableCarSpawn() {
        spawnCars = false;
    }

}
