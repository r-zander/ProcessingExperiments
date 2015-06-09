package creativecode.city;

import static creativecode.city.GenerativeCity.*;
import creativecode.city.GenerativeCity.Colors;

public class GridCell {

    CellState state;

    Building  building;

    float     x, y;

    public GridCell() {
        this(CellState.EMPTY);
    }

    public GridCell(CellState state) {
        this.state = state;
    }

    enum CellState {
        EMPTY,
        BUILT;
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
