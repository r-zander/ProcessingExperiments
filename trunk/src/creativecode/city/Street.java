package creativecode.city;

import static creativecode.city.GenerativeCity.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pathfinder.GraphNode;
import processing.core.PVector;

public class Street {

    List<GraphNode> nodes;

    List<PVector>   path = new ArrayList<PVector>();

    public Street(List<GraphNode> nodes, List<PVector> path) {
        this.nodes = nodes;
        this.path = path;
    }

    public void draw() {
        $.noStroke();

        int index = 0;

        for (PVector pathVector : path) {
            switch (index) {
                case 0:
                    $.fill(255, 0, 0);
                    break;
                case 1:
                    $.fill(255, 128, 0);
                    break;
                case 2:
                    $.fill(255, 255, 0);
                    break;
                case 3:
                    $.fill(0, 255, 0);
                    break;
                case 4:
                    $.fill(0, 255, 255);
                    break;

                default:
                    $.fill(255, 255, 255);
                    break;
            }
            $.ellipse(pathVector.x, pathVector.y, 8, 8);
            index++;
        }

        Iterator<GraphNode> iterator = nodes.iterator();
        if (!iterator.hasNext()) {
            return;
        }

        $.stroke(Car.COLOR);
        $.strokeWeight(3);

        GraphNode previousNode = iterator.next();

        while (iterator.hasNext()) {
            GraphNode node = iterator.next();
            $.line(previousNode.xf(), previousNode.yf(), node.xf(), node.yf());
            previousNode = node;
        }

    }

}
