package timevisualization.orbitclock;

import java.text.SimpleDateFormat;
import java.util.Date;

import processing.core.PApplet;
import processing.core.PFont;
import util.FontUtil;
// @Ignore
import util.Gradient;
import util.Gradient.Axis;
import util.TwoDimensional;
import util.structures.Point;
import _converter.RecordingUtil;
import _converter.RecordingUtil.RecordingArguments;
import _converter.RecordingUtil.RecordingListener;

public class OrbitClock extends PApplet {

    /**
     * Currently running instance
     */
    public static OrbitClock $;

    // @Ignore
    private static final int COLOR_MODE = ColorMode.BLACK_ALPHA;

    private static final int STEPS      = 240;

    private CurveFunction    curveFunction;

    // @Ignore 3
    private Gradient         gradient;

    private static int       YELLOW;

    private static int       BLUE;

    private float            centerX;

    private float            centerY;

    private TimeShape        minutesShape;

    private TimeShape        secondsShape;

    private TimeShape        millisShape;

    @Override
    public void setup() {
//        size(displayWidth, displayHeight);
        RecordingUtil.setupRecording(new RecordingArguments(
                this,
                displayWidth,
                displayHeight).framesRecorded(RecordingUtil.FPS * 60).listener(
                new RecordingListener() {

                    PFont            font;

                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");

                    @Override
                    public void onSetupRecording(PApplet $) {
                        font = createFont("Orbitron Light.otf", 20, true);
                    }

                    @Override
                    public void onBeforeFrameRecording(PApplet $) {
                        fill(0, 192);
                        noStroke();
                        int rectHeight = 2 * 16 + 26;
                        int rectWidth = 2 * 32 + 195;
                        int rectX = 16;
                        int rectY = height - 16 - rectHeight;
                        rect(rectX, rectY, rectWidth, rectHeight);

                        fill(255);
                        textFont(font);
                        textAlign(CENTER, CENTER);
                        FontUtil.monospace(
                                $,
                                format.format(new Date()),
                                17,
                                rectX + 32,
                                rectY + rectHeight / 2);
                    }
                }));
        noSmooth();

        $ = this;

        centerX = width / 2;
        centerY = height / 2;

        curveFunction = new LemniscateOfBernoulli(width * .30f);
//        function = new Hippopede(width * .2f, 1f, 1f);

        // @Ignore 2
        YELLOW = color(255, 192, 0);
        BLUE = color(0, 0, 192);

        strokeCap(SQUARE);

        minutesShape = new TimeShape.Minutes();
        minutesShape.radius = width * .05f;
        minutesShape.steps = 60;
        minutesShape.width = 10;
        minutesShape.mode = OrbMode.ORBIT;

        secondsShape = new TimeShape.Seconds();
        secondsShape.radius = minutesShape.radius * .5f;
        secondsShape.steps = 60;
        secondsShape.width = minutesShape.width * .2f;
        secondsShape.mode = OrbMode.ORBIT;

        millisShape = new TimeShape.Millis();
        millisShape.radius = secondsShape.radius * .4f;
        millisShape.steps = 100;
        millisShape.width = secondsShape.width * .5f;
        millisShape.mode = OrbMode.ORBIT;
    }

    @Override
    public void draw() {
        drawBackground();
        drawLemniscate();
        drawFPS();
    }

    private void drawBackground() {
        // @Ignore 11
        switch (COLOR_MODE) {
            case ColorMode.RANDOM:
                for (int i = 0; i <= 0 + width; i++) {
                    stroke(color(random(255), random(255), random(255)));
                    line(i, 0, i, height);
                }
                stroke(color(random(255), random(255), random(255)));
                fill(color(random(255), random(255), random(255)));
                break;
            case ColorMode.BLACK_ALPHA:
            case ColorMode.BLACK_WHITE:
                background(0, 0);
                // @Ignore 13
                if (gradient == null) {
                    gradient =
                            new Gradient(
                                    this,
                                    0,
                                    0,
                                    width,
                                    height,
                                    YELLOW,
                                    BLUE,
                                    Axis.X_AXIS);
                }
                gradient.draw();

//                noFill();
//                stroke(0);
                // @Ignore 7
                break;
            case ColorMode.YELLOW_BLUE:
                background(255);
                noFill();
                stroke(0);
                break;
        }
//        strokeWeight(3);
//        Shapes.sun(this, centerX - 0.3f * width, centerY, width * .1f);
//        Shapes.moon(this, centerX + 0.3f * width, centerY, width * .05f);
    }

    private void drawLemniscate() {
        noSmooth();
        float angle;
        // @Ignore 5
        switch (COLOR_MODE) {
            case ColorMode.YELLOW_BLUE:
                angle = PI;
                break;
            default:
                angle = hour2angle(hour(), minute());

                // @Ignore
        }
        Point startPoint = curveFunction.calculate(angle);
        float startX = centerX + startPoint.x;
        float startY = centerY + startPoint.y;
        float lastX = startX;
        float lastY = startY;
        float lastX1 = -1;
        float lastY1 = -1;
        float lastX2 = -1;
        float lastY2 = -1;
        for (int currentStep = 0; currentStep <= STEPS; currentStep++) {
            angle += TWO_PI / STEPS;
            Point point = curveFunction.calculate(angle);
            float x = centerX + point.x;
            float y = centerY + point.y;

            float angleBetween =
                    TwoDimensional.angleBetween(lastX, lastY, x, y) + HALF_PI;
            noStroke();

            setFill(currentStep, STEPS);

            int lineWidth = 30;
            float distX = cos(angleBetween) * lineWidth;
            float distY = sin(angleBetween) * lineWidth;
            float x1 = x - distX;
            float y1 = y - distY;
            float x2 = x + distX;
            float y2 = y + distY;
            if (lastX1 > -1) {
                quad(lastX1, lastY1, x1, y1, x2, y2, lastX2, lastY2);
//                break;
            }
//            line(x1, y1,x2 , y2);
            lastX1 = x1;
            lastY1 = y1;
            lastX2 = x2;
            lastY2 = y2;

            lastX = x;
            lastY = y;
        }
        // @Ignore 10
        switch (COLOR_MODE) {
            case ColorMode.YELLOW_BLUE:
                angle = hour2angle(hour(), minute());
                Point point = curveFunction.calculate(angle);
                startX = centerX + point.x;
                startY = centerY + point.y;
                break;
            default:
                break;
        }
        smooth();
        drawMinutes(startX, startY);
    }

    private void drawFPS() {
        if (mousePressed) {
            fill(0);
            text(String.format("%.1f", frameRate), width - 100, height - 100);
        }
    }

    void setStroke(float currentStep, float steps) {
        // @Ignore 9
        switch (COLOR_MODE) {
            case ColorMode.RANDOM:
                stroke(color(random(255), random(255), random(255)));
                break;
            case ColorMode.BLACK_WHITE:
                stroke(255 * (currentStep / steps), 255);
                break;
            case ColorMode.YELLOW_BLUE:
            case ColorMode.BLACK_ALPHA:
                stroke(0, 255 * (1 - currentStep / steps));
                // @Ignore 2
                break;
        }
    }

    void setFill(float currentStep, float steps) {
        // @Ignore 9
        switch (COLOR_MODE) {
            case ColorMode.RANDOM:
                fill(color(random(255), random(255), random(255)));
                break;
            case ColorMode.BLACK_WHITE:
                fill(255 * (currentStep / steps), 255);
                break;
            case ColorMode.BLACK_ALPHA:
            default:
                fill(0, 255 * (1 - currentStep / steps));
                // @Ignore 2
                break;
        }
    }

    private void drawMinutes(float x, float y) {
        minutesShape.draw(x, y);
        drawSeconds(x + cos(minutesShape.getStartAngle()) * minutesShape.radius, y
                + sin(minutesShape.getStartAngle()) * minutesShape.radius);
    }

    private void drawSeconds(float x, float y) {
        secondsShape.draw(x, y);
        drawMillis(x + cos(secondsShape.getStartAngle()) * secondsShape.radius, y
                + sin(secondsShape.getStartAngle()) * secondsShape.radius);
    }

    private void drawMillis(float x, float y) {
        millisShape.draw(x, y);
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
    private static float hour2angle(int hours, int minutes) {
        return map(hours + minutes / 60f, 0, 24, TWO_PI, 0);
    }

    public static void main(String[] args) {
        PApplet.main(new String[] { "--present", OrbitClock.class.getName() });
    }
}