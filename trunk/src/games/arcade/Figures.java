package games.arcade;

import static games.arcade.ArcadeConstants.*;
import static processing.core.PApplet.*;
import static processing.core.PConstants.*;
import games.arcade.ArcadeConstants.Colors;
import lombok.Data;
import lombok.experimental.Accessors;
import processing.core.PApplet;

@Data
@Accessors(fluent = true)
public class Figures {

    private final PApplet app;

    private float         distanceTraveled;

    private float         speed = START_SPEED;

    public void tick() {
        distanceTraveled += speed;
        speed += 0.01;
    }

    public int getFormattedSpeedMajor() {
        return floor(speed / START_SPEED);
    }

    public int getFormattedSpeedMinor() {
        return round((speed / 5) % 1 * 10);
    }

    public int getFormattedDistanceTraveled() {
        return floor(distanceTraveled);
    }

    public void draw() {
        app.fill(Colors.FONT);
        app.textSize(12);
        app.textAlign(CENTER, BOTTOM);
        app.text(app.frameRate, 50, 50);

        app.textAlign(RIGHT, BOTTOM);
        app.text(getFormattedSpeedMajor(), app.width - 200, 50);
        app.textAlign(LEFT, BOTTOM);
        app.textSize(8);
        app.text(getFormattedSpeedMinor(), app.width - 200, 50);

        app.textSize(12);
        app.textAlign(RIGHT, BOTTOM);
        app.text(getFormattedDistanceTraveled(), app.width - 50, 50);
    }

    public void reset() {
        distanceTraveled = 0;
        speed = floor(speed);
    }

}
