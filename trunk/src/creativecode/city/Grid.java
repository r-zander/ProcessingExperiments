package creativecode.city;

import static creativecode.city.GenerativeCity.*;

import java.util.ArrayList;
import java.util.List;

import creativecode.city.GridCell.CellState;

public class Grid {

    final static int   COLOR         = 0xff770078;

    final static float WEIGHT        = 1;

    final static float cellDimension = 20;

    GridCell[][]       cellGrid;

    List<GridCell>     cells;

//    FConstantVolumeJoint volumeJoint;

    public Grid() {
        cellGrid = new GridCell[(int) ($.width / cellDimension)][(int) ($.height / cellDimension)];
        cells = new ArrayList<GridCell>();

//        volumeJoint = new FConstantVolumeJoint();
//        $.world.add(volumeJoint);
    }

    void draw() {
        $.stroke(COLOR);
        $.strokeWeight(WEIGHT);

        for (float x = cellDimension; x < $.width; x += cellDimension) {
            $.line(x, 0, x, $.height);
        }

        for (float y = cellDimension; y < $.height; y += cellDimension) {
            $.line(0, y, $.width, y);
        }

        for (GridCell cell : cells) {
            cell.draw();
        }
    }

    float getX(int gridX) {
        return gridX * cellDimension;
    }

    float getY(int gridY) {
        return gridY * cellDimension;
    }

    int getGridX(float x) {
        return (int) (x / cellDimension);
    }

    int getGridY(float y) {
        return (int) (y / cellDimension);
    }

    boolean isState(int gridX, int gridY, CellState state) {
        GridCell cell = cellGrid[gridX][gridY];
        if (cell == null) {
            return state == CellState.EMPTY;
        }
        return state == cell.state;
    }

    void changeState(int gridX, int gridY, CellState newState) {
        float x = getX(gridX);
        float y = getY(gridY);

        switch (newState) {
            case BUILT:
//                $.stroke(Colors.BACKGROUND);
//                $.strokeWeight(WEIGHT);
//                $.fill(GenerativeCity.Colors.BACKGROUND);
//                $.rect(x, y, cellDimension, cellDimension);

                cellGrid[gridX][gridY] = new GridCell(newState);
                cellGrid[gridX][gridY].x = x;
                cellGrid[gridX][gridY].y = y;
                cellGrid[gridX][gridY].building =
                        new Building(
                                x + $.buildPadding,
                                y + $.buildPadding,
                                cellDimension - $.buildPadding * 2,
                                cellDimension - $.buildPadding * 2);
//                cellGrid[gridX][gridY].building.volumeJoint = volumeJoint;
                cells.add(cellGrid[gridX][gridY]);
//                building.draw();
                break;
            case EMPTY:
//                $.stroke(COLOR);
//                $.strokeWeight(WEIGHT);
//                $.fill(GenerativeCity.Colors.BACKGROUND);
//                $.rect(x, y, cellDimension, cellDimension);
                if (cellGrid[gridX][gridY] != null) {
                    cells.remove(cellGrid[gridX][gridY]);
                    cellGrid[gridX][gridY] = null;
                }
                break;
            default:
        }

    }

    public int getMaxGridX() {
        return cellGrid.length;
    }

    public int getMaxGridY() {
        return cellGrid[0].length;
    }

    public void step() {
        for (GridCell cell : cells) {
            cell.step();
        }
    }
}
