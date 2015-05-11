package games.arcade;

public class GameScreen extends AbstractScreen {

    public GameScreen(Arcade app) {
        super(app);
    }

    @Override
    public void activate() {
        app().figures.reset();
        app().unit.resetPosition();
        app().blocks.clear();
        app().blocks.add(new GroundBlock(app(), app().figures.speed()).width(app().width - 100).height(50)
                .positionInside(Corner.BOTTOM_LEFT));
    }

    @Override
    public void draw() {
        app().drawGame();
    }

}
