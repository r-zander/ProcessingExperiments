package games.arcade;

import static games.arcade.ArcadeConstants.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.experimental.PackagePrivate;
import processing.core.PApplet;

public class Arcade extends PApplet {

    @PackagePrivate
    Figures           figures;

    @PackagePrivate
    Direction         screenOrientation;

    @PackagePrivate
    final List<Block> blocks = new LinkedList<>();

    @PackagePrivate
    Unit              unit;

    @PackagePrivate
    Screens           screens;

    @PackagePrivate
    AbstractScreen    screen;

    @Override
    public void setup() {
        size(displayWidth, round(displayWidth / ASPECT_RATIO));
        noCursor();

        screenOrientation = Direction.BOTTOM;

        unit = new Unit(this);
        figures = new Figures(this);

        screens = new Screens(this);

        setScreen(screens.START);

        ellipseMode(CORNER);
    }

    private void setScreen(AbstractScreen newScreen) {
        if (screen != newScreen) {
            screen = newScreen;
            screen.activate();
        }
    }

    @Override
    public void draw() {
        screen.draw();
    }

    @PackagePrivate
    void drawGame() {
        background(ArcadeConstants.Colors.BACKGROUND);

        for (Iterator<Block> iterator = blocks.iterator(); iterator.hasNext();) {
            Block block = iterator.next();

            switch (block.move(figures.speed(), Direction.LEFT)) {
                case OUTSIDE:
                    // If it's a ground block, only remove it when its successor already spawned
                    if (block instanceof GroundBlock) {
                        if (((GroundBlock) block).hasNextSpawned() == false) {
                            break;
                        }
                    }
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
                setScreen(screens.DEATH);
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
                if (screen == screens.DEATH || screen == screens.START) {
                    setScreen(screens.GAME);
                }
                break;
            case ESC:
                if (screen != screens.DEATH) {
                    setScreen(screens.DEATH);
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
                if (screen == screens.DEATH || screen == screens.START) {
                    setScreen(screens.GAME);
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

    public float percentageWidth(float percentage) {
        return percentage / 100 * width;
    }

    public float percentageHeight(float percentage) {
        return percentage / 100 * height;
    }

    public static void main(String args[]) {
//        PApplet.main(new String[] { "--present", Arcarde.class.getName() });
//        PApplet.main(new String[] { "--location=-1920,700", Arcarde.class.getName() });
//        PApplet.main(new String[] { "--location=1920,0", Arcarde.class.getName() });
        PApplet.main(new String[] { Arcade.class.getName() });
    }
}