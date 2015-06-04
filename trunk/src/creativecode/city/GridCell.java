package creativecode.city;

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
}
