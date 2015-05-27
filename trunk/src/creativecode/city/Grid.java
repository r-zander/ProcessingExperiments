package creativecode.city;

import static creativecode.city.GenerativeCity.*;
import creativecode.city.GenerativeCity.Colors;

public class Grid {

    final static int   COLOR         = 0xff770078;

    final static float WEIGHT        = 1;

    float              cellDimension = 20;

    CellState[][]      cells;

    public Grid() {
        cells = new CellState[(int) ($.width / cellDimension)][(int) ($.height / cellDimension)];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = CellState.EMPTY;
            }
        }

        draw();
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
        return cells[gridX][gridY] == state;
    }

    void changeState(int gridX, int gridY, CellState newState) {
        float x = getX(gridX);
        float y = getY(gridY);

        switch (newState) {
            case BUILT:
                $.stroke(Colors.BACKGROUND);
                $.strokeWeight(WEIGHT);
                $.fill(GenerativeCity.Colors.BACKGROUND);
                $.rect(x, y, cellDimension, cellDimension);

                new Building(x + $.buildPadding, y + $.buildPadding, cellDimension - $.buildPadding * 2, cellDimension
                        - $.buildPadding * 2).draw();

                break;
            case EMPTY:
                $.stroke(COLOR);
                $.strokeWeight(WEIGHT);
                $.fill(GenerativeCity.Colors.BACKGROUND);
                $.rect(x, y, cellDimension, cellDimension);
                break;
            default:
        }

        cells[gridX][gridY] = newState;
    }

    enum CellState {
        EMPTY,
        BUILT;
    }

    public int getMaxGridX() {
        return cells.length;
    }

    public int getMaxGridY() {
        return cells[0].length;
    }
}
