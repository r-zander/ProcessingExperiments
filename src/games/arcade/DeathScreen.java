package games.arcade;

import lombok.RequiredArgsConstructor;
import processing.core.PApplet;
import processing.core.PConstants;

@RequiredArgsConstructor
public class DeathScreen {

    private final PApplet app;

    public void draw(Figures figures) {
        app.background(0);
        app.textSize(150);
        app.textAlign(PConstants.CENTER, PConstants.BOTTOM);
        app.fill(255);
        app.text("Dead", app.width / 2, app.height / 2);

        app.textSize(75);
        app.textAlign(PConstants.CENTER, PConstants.TOP);
        app.text(
                figures.getFormattedSpeedMajor() + " x " + figures.getFormattedDistanceTraveled(),
                app.width / 2,
                app.height / 2);
    }

}
