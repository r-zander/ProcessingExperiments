package games.arcade;

import static games.arcade.ArcadeConstants.*;
import games.arcade.ArcadeConstants.Colors;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import processing.core.PApplet;

public class Arcarde extends PApplet {

    private static final long serialVersionUID = -56589606646834162L;

    private float             backgroundColor  = 5;

    private float             speed;

    private Direction         screenOrientation;

    private final List<Block> blocks           = new LinkedList<>();

    private Unit              unit;

    @Override
    public void setup() {
        size(displayWidth, round(displayWidth / ASPECT_RATIO));
        noCursor();
        frameRate(1);

        speed = 5;
        screenOrientation = Direction.BOTTOM;

        blocks.add(new GroundBlock(this).width(width - 100).height(50).positionInside(Corner.BOTTOM_LEFT));

        unit = new Unit(this);

        stroke(Colors.BLUE);
        strokeWeight(2);
        noFill();
        ellipseMode(CORNER);
    }

    @Override
    public void draw() {
        background(backgroundColor);

        for (Iterator<Block> iterator = blocks.iterator(); iterator.hasNext();) {
            Block block = iterator.next();

            switch (block.move(speed, Direction.LEFT)) {
                case OUTSIDE:
                    iterator.remove();
                    break;
                default:
                    break;
            }

            float gap = width - block.right();
            if (gap >= 200 && gap < (200 + speed)) {
                blocks.add(new GroundBlock(this));
                break;
            }
        }

        unit.update(getGravityDirection(), blocks);
    }

    @Override
    public void keyPressed() {
        switch (key) {
            case ' ':
                unit.jump(getGravityDirection());
                break;
            case ESC:
                /*
                 * return to start screen
                 */
                break;
            case CODED:
                switch (keyCode) {
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void mousePressed() {
        switch (mouseButton) {
            case LEFT:
                unit.jump(getGravityDirection());
                break;
            case RIGHT:
                frameRate(1);
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseReleased() {
        switch (mouseButton) {
            case RIGHT:
                frameRate(60);
                break;
            default:
                break;
        }
    }

    private Direction getGravityDirection() {
        return screenOrientation;
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { /* "--present", */"--location=-1920,700", Arcarde.class.getName() });
    }
}