package games.arcade;

import static games.arcade.ArcadeConstants.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import processing.core.PApplet;

public class Arcarde extends PApplet {

    private static final long serialVersionUID = -56589606646834162L;

    private float             speed;

    private Direction         screenOrientation;

    private final List<Block> blocks           = new LinkedList<>();

    private Unit              unit;

    private DeathScreen       deathScreen;

    private GameScreen        screen;

    @Override
    public void setup() {
        size(displayWidth, round(displayWidth / ASPECT_RATIO));
        noCursor();

        screenOrientation = Direction.BOTTOM;

        unit = new Unit(this);
        deathScreen = new DeathScreen(this);

        setScreen(GameScreen.GAME);

        ellipseMode(CORNER);
    }

    private void setScreen(GameScreen newScreen) {
        if (screen != newScreen) {
            screen = newScreen;
            switch (screen) {
                case START:
                    break;
                case GAME:
                    speed = 5;
                    unit.resetPosition();
                    blocks.clear();
                    blocks.add(new GroundBlock(this, speed).width(width - 100).height(50)
                            .positionInside(Corner.BOTTOM_LEFT));
                    break;
                case DEATH:
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void draw() {
        switch (screen) {
            case DEATH:
                deathScreen.draw();
                return;
            case GAME:
            default:
                break;
        }

        background(ArcadeConstants.Colors.BACKGROUND);

        for (Iterator<Block> iterator = blocks.iterator(); iterator.hasNext();) {
            Block block = iterator.next();

            switch (block.move(speed, Direction.LEFT)) {
                case OUTSIDE:
                    iterator.remove();
                    break;
                default:
                    break;
            }

            if (block instanceof GroundBlock) {
                GroundBlock groundBlock = (GroundBlock) block;
                if (groundBlock.hasNextSpawned() == false) {
                    float gap = width - block.right();
                    if (gap >= groundBlock.gapSize()) {
                        blocks.add(new GroundBlock(this, speed));
                        groundBlock.hasNextSpawned(true);
                        break;
                    }
                }
            }
        }

        if (mousePressed) {
            switch (mouseButton) {
                case LEFT:
                    unit.enhanceJump();
                    break;
            }
        }

        if (keyPressed) {
            switch (key) {
                case ' ':
                    unit.enhanceJump();
                    break;
            }
        }

        switch (unit.update(getGravityDirection(), blocks)) {
            case DEAD:
                setScreen(GameScreen.DEATH);
                break;
            default:
                break;

        }

        speed += 0.01;
    }

    @Override
    public void keyPressed() {
        switch (key) {
            case ' ':
                jumpPressed();
                break;
            case ENTER:
                setScreen(GameScreen.START);
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
                jumpPressed();
                break;
            case RIGHT:
                frameRate(1);
                break;
            default:
                break;
        }
    }

    private void jumpPressed() {
        switch (screen) {
            case DEATH:
                setScreen(GameScreen.GAME);
                return;
            case GAME:
            default:
                unit.jump(getGravityDirection());
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
//        PApplet.main(new String[] { "--present", Arcarde.class.getName() });
//        PApplet.main(new String[] { "--location=-1920,700", Arcarde.class.getName() });
//        PApplet.main(new String[] { "--location=1920,0", Arcarde.class.getName() });
        PApplet.main(new String[] { Arcarde.class.getName() });
    }
}