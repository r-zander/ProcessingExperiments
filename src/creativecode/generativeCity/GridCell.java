package creativecode.generativeCity;

import static creativecode.generativeCity.GenerativeCity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import pathfinder.GraphNode;
import creativecode.generativeCity.GenerativeCity.Colors;

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
        if (state == CellState.BUILT) {
            $.stroke(Colors.BACKGROUND);
            $.strokeWeight(Grid.WEIGHT);
            $.fill(GenerativeCity.Colors.BACKGROUND);
            $.rect(xf(), yf(), Grid.cellDimension, Grid.cellDimension);

            if (building != null) {
                building.draw();
            }
        }
    }
}
