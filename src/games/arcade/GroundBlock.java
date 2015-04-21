package games.arcade;

import lombok.Data;
import lombok.experimental.Accessors;
import processing.core.PApplet;

@Data
@Accessors(fluent = true)
public class GroundBlock extends Block {

    private final float gapSize;

    private boolean     hasNextSpawned;

    public GroundBlock(PApplet app, float speed) {
        super(app, Shape.RECTANGLE);

        width(app.random(100, 1400));
        height(app.random(20, 200));
        positionOutside(Direction.RIGHT);
        positionInside(Direction.BOTTOM);

        gapSize = app.random(10, 40) * speed;
    }

    @Override
    public Block positionInside(Direction screenSide) {
        super.positionInside(screenSide);

        switch (screenSide) {
            case BOTTOM:
                offsetY(2);
                break;
            case TOP:
                offsetY(-2);
                break;
            default:
                break;
        }
        return this;
    }

}
