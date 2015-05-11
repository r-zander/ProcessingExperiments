package games.arcade;

public class Screens {

    public final StartScreen START;

    public final GameScreen  GAME;

    public final DeathScreen DEATH;

    public Screens(Arcade app) {
        START = new StartScreen(app);
        GAME = new GameScreen(app);
        DEATH = new DeathScreen(app);
    }

}
