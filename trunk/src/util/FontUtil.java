package util;

import processing.core.PApplet;

public class FontUtil {

    public static void monospace(PApplet applet, String text, float letterWidth, float x,
            float y) {
        for (final char c : text.toCharArray()) {
            applet.text(c, x, y);
            x += letterWidth;
        }
    }
}
