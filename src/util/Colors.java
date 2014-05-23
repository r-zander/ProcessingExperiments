package util;

public class Colors {

    public static final int RED = 0xFFFF0000;

    public static int randomColor() {
        return Numbers.random(0xFF000000, 0xFFFFFFFF);
    }
}
