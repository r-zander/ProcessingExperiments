package creativecode.city;

import static creativecode.city.GenerativeCity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import pathfinder.GraphNode;
import creativecode.city.GenerativeCity.Colors;

public class GridCell {

    enum CellState {
        EMPTY,
        BUILT,
        STREET;
    }

    private static final AtomicInteger SEQUENCER         = new AtomicInteger(1);

    final int                          nodeId;

    CellState                          state;

    Building                           building;

    final float                        x, y;

    final GraphNode                    graphNode;

    List<Street>                       associatedStreets = new ArrayList<Street>();

    public GridCell(float x, float y) {
        this.state = CellState.EMPTY;
        this.x = x;
        this.y = y;
        this.nodeId = SEQUENCER.getAndIncrement();
        this.graphNode = new GraphNode(nodeId, x, y);
    }

    public void step() {
        if (building != null) {
            building.step();
        }
    }

    public void draw() {
        if (building != null) {
            $.stroke(Colors.BACKGROUND);
            $.strokeWeight(Grid.WEIGHT);
            $.fill(GenerativeCity.Colors.BACKGROUND);
            $.rect(x, y, Grid.cellDimension, Grid.cellDimension);
            building.draw();
        }
    }
}
