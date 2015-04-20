package games.arcade;

import lombok.Getter;

@Getter
public enum Corner {
    TOP_LEFT(Direction.TOP, Direction.LEFT),
    TOP_RIGHT(Direction.TOP, Direction.RIGHT),
    BOTTOM_RIGHT(Direction.BOTTOM, Direction.RIGHT),
    BOTTOM_LEFT(Direction.BOTTOM, Direction.LEFT);

    private Direction vertical, horizontal;

    private Corner(Direction vertical, Direction horizontal) {
        this.vertical = vertical;
        this.horizontal = horizontal;
    }
}
