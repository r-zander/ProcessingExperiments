package games.arcade;

public class StartScreen extends AbstractScreen {

    public StartScreen(Arcade app) {
        super(app);
    }

    @Override
    public void draw() {
        app().background(0);

        app().noFill();
        app().stroke(ArcadeConstants.Colors.BLUE);
        app().strokeWeight(3);

        app().rect(
                app().percentageWidth(100 / 9f),
                app().percentageHeight(20),
                app().percentageWidth(100 / 3f),
                app().percentageHeight(20),
                0,
                20,
                20,
                20);

        app().rect(
                app().percentageWidth(100 / 9f),
                app().percentageHeight(20 * 3),
                app().percentageWidth(100 / 3f),
                app().percentageHeight(20),
                0,
                20,
                20,
                20);

    }

}
