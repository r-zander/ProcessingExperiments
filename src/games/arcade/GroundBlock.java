package games.arcade;

import processing.core.PApplet;

public class GroundBlock extends Block {

    public GroundBlock(PApplet app) {
        super(app, Shape.RECTANGLE);

        width(app.random(100, 300));
        height(app.random(20, 200));
        positionOutside(Direction.RIGHT);
        positionInside(Direction.BOTTOM);
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
