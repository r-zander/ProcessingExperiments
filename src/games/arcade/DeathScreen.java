package games.arcade;

import games.arcade.ArcadeConstants.FontSize;
import processing.core.PConstants;

public class DeathScreen extends AbstractScreen {

    public DeathScreen(Arcade app) {
        super(app);
    }

    @Override
    public void draw() {
        app().background(0);
        app().textSize(FontSize.H1);
        app().textAlign(PConstants.CENTER, PConstants.BOTTOM);
        app().fill(255);
        app().text("Dead", app().width / 2, app().height / 2);

        app().textSize(FontSize.H2);
        app().textAlign(PConstants.CENTER, PConstants.TOP);
        app().text(
                app().figures.getFormattedSpeedMajor() + " x " + app().figures.getFormattedDistanceTraveled(),
                app().width / 2,
                app().height / 2);
    }

}
