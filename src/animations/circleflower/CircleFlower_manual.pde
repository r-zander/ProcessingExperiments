    final static int   NUMBER_OF_CIRCLES              = 8;

    final static float RELATIVE_CIRCLE_WIDTH          = 2f / 3f;

    final static float ANGLE_OFFSET_SMALLER_CIRCLES   = (TWO_PI / NUMBER_OF_CIRCLES) / 2f;

    final static float SMALLER_CIRCLE_RELATIVE_RADIUS = 0.9f;

    float              centerX, centerY;

    float              circleRadius;

    float              circleWidth;

    float              circleStrokeWeight;

    float              rotation;

    float              smallerCircleWidth;

    float[]            angles                         = new float[NUMBER_OF_CIRCLES];

    float[]            offsetAngles                   = new float[NUMBER_OF_CIRCLES];

    float[]            xs                             = new float[NUMBER_OF_CIRCLES];

    float[]            ys                             = new float[NUMBER_OF_CIRCLES];

    public void setup() {
        int dimension = min(window.innerWidth, window.innerHeight);
        size(dimension, dimension);
        smooth();
        centerX = width / 2f;
        centerY = width / 2f;
        circleRadius = width * RELATIVE_CIRCLE_WIDTH / 4f;
        circleWidth = circleRadius * 2f;
        smallerCircleWidth = circleWidth * SMALLER_CIRCLE_RELATIVE_RADIUS;

        circleStrokeWeight = width / 180f;

        rotation = 0;
    }

    public void draw() {
        background(0);

        noFill();

        for (int i = 0; i < NUMBER_OF_CIRCLES; i++) {
            angles[i] = rotation + TWO_PI / NUMBER_OF_CIRCLES * i;
            xs[i] = centerX + cos(angles[i]) * circleRadius;
            ys[i] = centerY + sin(angles[i]) * circleRadius;
        }

        stroke(255);
        strokeWeight(circleStrokeWeight);
        for (int i = 0; i < NUMBER_OF_CIRCLES; i++) {
            arc(xs[i], ys[i], circleWidth, circleWidth, angles[i] + rotation * 9, angles[i] + PI + rotation * 9);
        }

        for (int i = 0; i < NUMBER_OF_CIRCLES; i++) {
            angles[i] = rotation + TWO_PI / NUMBER_OF_CIRCLES * i + ANGLE_OFFSET_SMALLER_CIRCLES;
            xs[i] = centerX + cos(angles[i]) * circleRadius;
            ys[i] = centerY + sin(angles[i]) * circleRadius;

        }

        stroke(0);
        for (int i = 0; i < NUMBER_OF_CIRCLES; i++) {
            arc(xs[i], ys[i], smallerCircleWidth, smallerCircleWidth, angles[i] + rotation * 3, angles[i] + PI
                    + rotation * 3);
        }

        stroke(255);
        strokeWeight(circleStrokeWeight / 2);
        for (int i = 0; i < NUMBER_OF_CIRCLES; i++) {
            arc(xs[i], ys[i], smallerCircleWidth, smallerCircleWidth, angles[i] + rotation * 3, angles[i] + PI
                    + rotation * 3);
        }

        rotation += 0.005f;
        rotation %= TWO_PI;

        drawFPS();
    }

    void drawFPS() {
        fill(255, 255, 255);
        textSize(12);
        textAlign(CENTER, BOTTOM);
        text(frameRate, 50, 50);
    }
