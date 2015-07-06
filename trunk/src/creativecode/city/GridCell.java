package creativecode.city;

import static creativecode.city.GenerativeCity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import pathfinder.GraphNode;
import creativecode.city.GenerativeCity.Colors;

public class GridCell extends GraphNode {

    enum CellState {
        EMPTY,
        BUILT,
        STREET,
        BLOCKED;
    }

    private static final AtomicInteger SEQUENCER         = new AtomicInteger(1);

    CellState                          state;

    Building                           building;

    List<Street>                       associatedStreets = new ArrayList<Street>();

    public GridCell(float x, float y) {
        super(SEQUENCER.getAndIncrement(), x, y);
        this.state = CellState.EMPTY;
    }

    public void step() {
        if (building != null) {
            building.step();
        }
    }

    public void draw() {
        if (state == CellState.BUILT && building != null) {
            $.stroke(Colors.BACKGROUND);
            $.strokeWeight(Grid.WEIGHT);
            $.fill(GenerativeCity.Colors.BACKGROUND);
            $.rect(xf(), yf(), Grid.cellDimension, Grid.cellDimension);
            building.draw();
        }
    }
}
