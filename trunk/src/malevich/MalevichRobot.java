package malevich;

import processing.core.PApplet;

public class MalevichRobot extends MalevichBasic {

    private static final long serialVersionUID = -56589606646834162L;

    @Override
    public void setup() {
        squareColor = (int) random(0, 255);
        smallElementColor = (int) random(0, 255);
        quadColor = (int) random(0, 255);
        bgColor = (int) random(0, 255);
        rectColor = (int) random(0, 255);

        super.setup();
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", MalevichRobot.class.getName() });
    }
}