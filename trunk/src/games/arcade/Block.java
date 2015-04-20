package games.arcade;

import games.arcade.ArcadeConstants.Colors;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import processing.core.PApplet;

@Data
@Accessors(fluent = true)
public class Block {

    private final PApplet app;

    private final Shape   shape;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private float         x;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private float         y;

    private float         width;

    private float         height;

    public Block positionOutside(Corner screenCorner) {
        positionOutside(screenCorner.getHorizontal());
        positionOutside(screenCorner.getVertical());
        return this;
    }

    public Block positionInside(Corner screenCorner) {
        positionInside(screenCorner.getHorizontal());
        positionInside(screenCorner.getVertical());
        return this;
    }

    public Block positionOutside(Direction screenSide) {
        switch (screenSide) {
            case TOP:
                y(0 - height());
                break;
            case BOTTOM:
                y(app.height + 1);
                break;
            case RIGHT:
                x(app.width + 1);
                break;
            case LEFT:
                x(0 - width());
                break;
        }
        return this;
    }

    public Block positionInside(Direction screenSide) {
        switch (screenSide) {
            case TOP:
                y(0);
                break;
            case BOTTOM:
                y(app.height - height() - 1);
                break;
            case RIGHT:
                x(app.width - width() - 1);
                break;
            case LEFT:
                x(0);
                break;
        }
        return this;
    }

    public Block offset(Direction direction, float value) {
        switch (direction) {
            case TOP:
                return offsetY(-value);
            case RIGHT:
                return offsetX(value);
            case BOTTOM:
                return offsetY(value);
            case LEFT:
                return offsetX(-value);
        }
        return this;
    }

    public Block offsetX(float value) {
        return x(x() + value);
    }

    public Block offsetY(float value) {
        return y(y() + value);
    }

    public Position move(float speed, Direction orientation) {
        switch (orientation) {
            case TOP:
                y += speed;
                break;
            case BOTTOM:
                y -= speed;
                break;
            case LEFT:
                x -= speed;
                break;
            case RIGHT:
                x += speed;
                break;
        }

        if (((x + width) < 0 || (x > app.width) && ((y < 0) || (y - height) > app.width))) {
            return Position.OUTSIDE;
        } else {
            draw();
            return Position.INSIDE;
        }
    }

    protected void draw() {
        app.stroke(Colors.BLUE);
        app.strokeWeight(2);
        app.noFill();

        switch (shape()) {
            case RECTANGLE:
                app.rect(x, y, width, height);
                break;
            case ELLIPSE:
                app.ellipse(x, y, width, height);
                break;
        }
    }

    public float top() {
        return y();
    }

    public float right() {
        return x() + width();
    }

    public float bottom() {
        return y() + height();
    }

    public float left() {
        return x();
    }

    public Block top(float top) {
        return y(top);
    }

    public Block right(float right) {
        return x(right - width());
    }

    public Block bottom(float bottom) {
        return y(bottom - height());
    }

    public Block left(float left) {
        return x(left);
    }

    public float centerX() {
        return x() + width() / 2;
    }

    public float centerY() {
        return y() + height() / 2;
    }
}
