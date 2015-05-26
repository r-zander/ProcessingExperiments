package creativecode.city;

import static creativecode.city.GenerativeCity.*;
import creativecode.city.GenerativeCity.Colors;

public class Grid {

    final static int   COLOR         = 0xff770078;

    final static float WEIGHT        = 1;

    float              cellDimension = 20;

    public Grid() {
        // TODO Auto-generated constructor stub
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

    void replaceCell(int gridX, int gridY) {
        $.stroke(Colors.BACKGROUND);
        $.strokeWeight(WEIGHT);
        $.fill(GenerativeCity.Colors.BACKGROUND);
        $.rect(getX(gridX), getY(gridY), cellDimension, cellDimension);
    }
}
