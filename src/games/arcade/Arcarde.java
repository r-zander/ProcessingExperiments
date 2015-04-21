package games.arcade;

import static games.arcade.ArcadeConstants.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import processing.core.PApplet;

public class Arcarde extends PApplet {

    private static final long serialVersionUID = -56589606646834162L;

    private Figures           figures;

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
        figures = new Figures(this);

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
                    figures.reset();
                    unit.resetPosition();
                    blocks.clear();
                    blocks.add(new GroundBlock(this, figures.speed()).width(width - 100).height(50)
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
                deathScreen.draw(figures);
                return;
            case GAME:
            default:
                break;
        }

        background(ArcadeConstants.Colors.BACKGROUND);

        for (Iterator<Block> iterator = blocks.iterator(); iterator.hasNext();) {
            Block block = iterator.next();

            switch (block.move(figures.speed(), Direction.LEFT)) {
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
                        blocks.add(new GroundBlock(this, figures.speed()));
                        groundBlock.hasNextSpawned(true);
                        break;
                    }
                }
            }
        }

        if (mousePressed) {
            switch (mouseButton) {
                case LEFT:
                    unit.jump();
                    break;
            }
        }

        if (keyPressed) {
            switch (key) {
                case ' ':
                    unit.jump();
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

        figures.draw();
        figures.tick();
    }

    @Override
    public void keyPressed() {
        switch (key) {
            case ' ':
                if (screen == GameScreen.DEATH) {
                    setScreen(GameScreen.GAME);
                }
                break;
            case ESC:
                if (screen != GameScreen.DEATH) {
                    setScreen(GameScreen.DEATH);
                    // Don't pass ESC through
                    key = 0;
                }
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
                if (screen == GameScreen.DEATH) {
                    setScreen(GameScreen.GAME);
                }
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
//        PApplet.main(new String[] { "--present", Arcarde.class.getName() });
//        PApplet.main(new String[] { "--location=-1920,700", Arcarde.class.getName() });
//        PApplet.main(new String[] { "--location=1920,0", Arcarde.class.getName() });
        PApplet.main(new String[] { Arcarde.class.getName() });
    }
}