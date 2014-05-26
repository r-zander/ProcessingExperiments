package timevisualization;

import processing.core.PApplet;
import util.Axis;
import util.Colors;

public class OrbitClock extends PApplet {

    private static final long serialVersionUID = -56589606646834162L;

    private static final int  STEPS            = 120;

    int                       currentStep      = 0;

    float                     a;

    float                     angle;

    static final float        SQRT_2           = sqrt(2);

    float                     centerX;

    float                     centerY;

    float                     lastX;

    float                     lastY;

    @Override
    public void setup() {
        size(displayWidth, displayHeight);
        background(192);
        Colors.drawGradient(this, 0, 0, width, height, color(255, 192, 0), color(0, 0, 192), Axis.X_AXIS);

        a = width * .35f;
        centerX = width / 2;
        centerY = height / 2;

        angle = time2angle(hour(), minute());
        float sin = sin(angle);
        float divisor = sq(sin) + 1;
        float dividend = a * SQRT_2 * cos(angle);
        lastX = centerX + dividend / divisor;
        lastY = centerY + (dividend * sin) / divisor;
        fill(0);
        noStroke();
        ellipse(lastX, lastY, 40, 40);
//        println("(" + width + "/" + height + ")");
//        println("(" + lastX + "/" + lastY + ")");
//        println("(" + lastX / width + "/" + lastY / height + ")");

    }

    @Override
    public void draw() {
//        fill(192, 10);
//        rect(0, 0, width, height);

        float sin = sin(angle);
        float divisor = sq(sin) + 1;
        float dividend = a * SQRT_2 * cos(angle);
        float x = centerX + dividend / divisor;
        float y = centerY + (dividend * sin) / divisor;

//        fill(0);
//        ellipse( x,  + y, 10, 10);
        stroke(0, 255 * (1 - (float) currentStep / STEPS));
        strokeWeight(10);
        line(lastX, lastY, x, y);

        lastX = x;
        lastY = y;
        currentStep++;
        currentStep %= STEPS;
        angle += TWO_PI / STEPS;
    }

    /**
     * 12:00 = PI
     * 24:00 = 0 = TWO_PI
     * 6:00 = -PI/2 = 1.5 * PI
     * 18:00 = PI/2
     * 
     * @param hours
     * @param minutes
     * @return
     */
    static float time2angle(int hours, int minutes) {
        return -TWO_PI * (hours / 24f + minutes / (24f * 60));
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", OrbitClock.class.getName() });
//        System.out.println("23:59 = " + degrees(time2angle(23, 59)));
//        System.out.println("0:00 = " + degrees(time2angle(0, 0)));
//        System.out.println("12:00 = " + degrees(time2angle(12, 0)));
//        System.out.println("6:00 = " + degrees(time2angle(6, 0)));
//        System.out.println("18:00 = " + degrees(time2angle(18, 0)));
    }
}