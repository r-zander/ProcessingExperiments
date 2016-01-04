package creativecode.treeFractal;

import processing.core.PApplet;

public class TreeFractal extends PApplet {

    private static final int DEPTH_LIMIT        = 8;

    private static final int NUMBER_OF_BRANCHES = 3;

    @Override
    public void setup() {
        size(displayWidth, displayHeight);
        background(255);
//        noLoop();
//        frameRate(2);
    }

    @Override
    public void draw() {
        background(0);

        randomSeed(1);
        drawBranch(width / 2, height, radians(270), 0);

        drawFPS();
    }

    private void drawFPS() {
        fill(255);
        text(String.format("%d (%.1f / %.1f)", frameCount, frameRate, frameRateTarget), width - 100, height - 100);
    }

    private void drawBranch(float x, float y, float angle, int depth) {
        if (depth >= DEPTH_LIMIT) {
            return;
        }

        stroke(255 - (255 / (DEPTH_LIMIT - 1) * depth));

        float offset = random(100, 150);

        float x2 = x + cos(angle) * offset;
        float y2 = y + sin(angle) * offset;
        strokeWeight(1);
        line(x, y, x2, y2);

        if (depth > 4) {
            stroke(0, 128, 0);
            strokeWeight(random(3, 15));
            point(x2, y2);
        }

        float angleLimit = PI / 16;
        float additionalAngle = radians(frameCount / 2);
        int timesQuarterPi = ceil(additionalAngle / angleLimit) % 4;
        additionalAngle %= angleLimit;

        switch (timesQuarterPi) {
            case 0:
//                additionalAngle = 0;
//                break;
            case 1:
                break;
            case 2:
                additionalAngle = angleLimit - additionalAngle;
                break;
            case 3:
                additionalAngle = -additionalAngle;
                break;
            case 4:
                additionalAngle = -angleLimit + additionalAngle;
                break;
            default:
                break;
        }

        for (int i = 0; i < NUMBER_OF_BRANCHES; i++) {
            drawBranch(x2, y2, angle + random(-QUARTER_PI, QUARTER_PI) + additionalAngle, depth + 1);
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", TreeFractal.class.getName() });
    }
}
