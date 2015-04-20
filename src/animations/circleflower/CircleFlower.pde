    final static int   NUMBER_OF_CIRCLES              = 8;

    final static float CIRCLE_WEIGHT                  = 6;

    final static float RELATIVE_CIRCLE_WIDTH          = 2f / 3f;

    final static float ANGLE_OFFSET_SMALLER_CIRCLES   = (TWO_PI / NUMBER_OF_CIRCLES) / 2;

    final static float SMALLER_CIRCLE_RELATIVE_RADIUS = .9f;

    float              centerX, centerY;

    float              circleRadius;

    float              circleWidth;

    float              rotation;

    float              smallerCircleWidth;

    void setup() {
        int dimension = min(displayWidth, displayHeight);
        size(dimension, dimension);

        centerX = width / 2f;
        centerY = width / 2f;
        circleRadius = width * RELATIVE_CIRCLE_WIDTH / 4;
        circleWidth = circleRadius * 2;
        smallerCircleWidth = circleWidth * SMALLER_CIRCLE_RELATIVE_RADIUS;

        rotation = 0;
    }

    void draw() {
        background(0);

        noFill();
        for (int i = 0; i < NUMBER_OF_CIRCLES; i++) {
            float angle = rotation + TWO_PI / NUMBER_OF_CIRCLES * i;
            float x = centerX + cos(angle) * circleRadius;
            float y = centerY + sin(angle) * circleRadius;

            stroke(255);
            strokeWeight(CIRCLE_WEIGHT);
            arc(x, y, circleWidth, circleWidth, angle + rotation * 9, angle + PI + rotation * 9);

            angle += ANGLE_OFFSET_SMALLER_CIRCLES;
            x = centerX + cos(angle) * circleRadius;
            y = centerY + sin(angle) * circleRadius;
            stroke(0);
            arc(x, y, smallerCircleWidth, smallerCircleWidth, angle + rotation * 3, angle + PI + rotation * 3);

            stroke(255);
            strokeWeight(CIRCLE_WEIGHT / 2);
            arc(x, y, smallerCircleWidth, smallerCircleWidth, angle + rotation * 3, angle + PI + rotation * 3);
        }

        rotation += 0.003;
        rotation %= TWO_PI;
    }
