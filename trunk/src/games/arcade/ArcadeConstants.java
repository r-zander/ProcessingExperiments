package games.arcade;

import static processing.core.PApplet.*;

public class ArcadeConstants {

    private ArcadeConstants() {}

    public static final float ASPECT_RATIO = 16f / 5f;

    public static final float GRAVITY      = 3;

    public static class Colors {

        public static final float BACKGROUND = 5;

        public static final int   BLUE       = 0xff18CAE6;

        public static final float FONT       = 225;
    }

    public static class FontSize {

        public static final int H1 = 150;

        public static final int H2 = 75;
    }

    public static final float START_SPEED       = 5;

    public static final float MIN_JUMP_HEIGHT   = 120;

    public static final float MAX_JUMP_HEIGHT   = 400;

    public static final int   JUMP_FRAMES       = 30;

    public static final int   MAX_JUMP_STRENGTH = 20;

    public static float calculateJumpY(int frame) {
        float c = MIN_JUMP_HEIGHT;
        float d = JUMP_FRAMES;
        return -2 * c / sq(d) * frame + 2 * c / d;
    }

    public static void main(String[] args) {
        for (int i = 0; i <= 2 * JUMP_FRAMES; i++) {
            System.out.println(i + "\t" + calculateJumpY(i));
        }
    }

}
