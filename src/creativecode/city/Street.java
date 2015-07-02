package creativecode.city;

import static creativecode.city.GenerativeCity.*;
import static processing.core.PConstants.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pathfinder.GraphNode;

public class Street {

    List<GraphNode> nodes;

    List<DebugPath> path = new ArrayList<DebugPath>();

    public Street(List<GraphNode> nodes, List<DebugPath> path) {
        this.nodes = nodes;
        this.path = path;
    }

    public void draw() {
//        for (DebugPath debugPath : path) {
//            debugPath.draw();
//        }

        Iterator<GraphNode> iterator = nodes.iterator();
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
            $.text(title, cell.x, cell.y);

            $.rectMode(CORNER);
            $.stroke(128);
            $.strokeWeight(Grid.WEIGHT);
            $.rect(cell.x, cell.y, Grid.cellDimension, Grid.cellDimension);
        }
    }

}
