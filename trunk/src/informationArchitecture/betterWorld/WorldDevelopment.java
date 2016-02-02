package informationArchitecture.betterWorld;

import g4p_controls.GEvent;
import g4p_controls.GSlider;
import g4p_controls.GValueControl;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;
import processing.event.MouseEvent;
import util.Numbers;

public class WorldDevelopment extends PApplet {

    float            centerX;

    float            centerY;

    static final int MIN_YEAR       = 1970;

    static final int MAX_YEAR       = 2011;

    static final int YEAR_STEPS     = 5;

    int              year           = MIN_YEAR;

    GSlider          slider;

    List<Indicator>  indicators;

    static final int ICONS_PER_LINE = 10;

    @Override
    public void setup() {
        size(displayWidth, displayHeight);
        centerX = width / 2;
        centerY = height / 2;

        slider = new GSlider(this, 30, 180, width - 2 * 30, 50, 10) {

            @Override
            protected void drawLabels() {
                super.drawLabels();

                if (isShowValue()) {
                    drawValue();
                }
            }
        };

        int numberOfSteps = (MAX_YEAR - MIN_YEAR) / YEAR_STEPS + 1;
        String[] labels = new String[numberOfSteps];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = Integer.toString(MIN_YEAR + YEAR_STEPS * i);
        }
        slider.setTickLabels(labels);
        slider.setStickToTicks(false);
        slider.setLimits(year, MIN_YEAR, MAX_YEAR);
        slider.setShowDecor(false, true, true, true);
        slider.setLocalColorScheme(6);

        loadIndicators();
    }

    private void loadIndicators() {
        indicators = new ArrayList<Indicator>();

        int numberOfValues = MAX_YEAR - MIN_YEAR + 1;

        indicators.add(new Indicator(
                "are living in poverty.",
                "poverty",
                new Float[numberOfValues],
                loadImage("worlddevelopement/poverty.png")));
        indicators.add(new Indicator(
                "starve every day.",
                "hunger",
                new Float[numberOfValues],
                loadImage("worlddevelopement/poverty.png")));
        Indicator mortalityIndicator =
                new Indicator(
                        "die before their\n5th birthday.",
                        "mortality",
                        new Float[numberOfValues],
                        loadImage("worlddevelopement/poverty.png"));
        mortalityIndicator.multiplier = 0.1f;
        indicators.add(mortalityIndicator);

        Table table = loadTable("worlddevelopement/WorldDevelopment.csv", "header");

        int index = 0;
        for (TableRow row : table.rows()) {
            if (row.getInt("year") < MIN_YEAR) {
                continue;
            }
            if (row.getInt("year") > MAX_YEAR) {
                continue;
            }
            for (Indicator indicator : indicators) {
                float value = row.getFloat(indicator.tableHeader);
                if (!Numbers.isNan(value)) {
                    indicator.setValue(index, value);
                }
            }
            index++;
        }

        index = 0;
        float widthPerIndicator = width / (float) indicators.size();
        for (Indicator indicator : indicators) {
            indicator.interpolateMissingValues();
            indicator.x = widthPerIndicator / 2 + widthPerIndicator * index;
            indicator.y = height - 310;
            indicator.width = widthPerIndicator;
            index++;
        }
    }

    @Override
    public void draw() {
        background(255);
        fill(0);
        noStroke();

        textSize(50);
        textAlign(CENTER, TOP);
        text("Is the world getting better?", centerX, 30);

        textSize(30);
        text("In the year", centerX, 130);
        text("Out of 100 people ...", centerX, 230);

        drawIndicators();
    }

    private int getYearAsIndex() {
        return year - MIN_YEAR;
    }

    private void drawIndicators() {
        int yearAsIndex = getYearAsIndex();
        textAlign(CENTER, TOP);
        for (Indicator indicator : indicators) {
            textSize(30);
            text(indicator.title, indicator.x, height - 200);

            Integer value = indicator.getValue(yearAsIndex);
            if (value == null) {
                text("null", indicator.x, height - 250);
            } else {
                for (int i = 0; i < value; i++) {
                    float iconWidth = indicator.width / ICONS_PER_LINE / 2;
                    int iconHeight = 30; // TODO mit Aspect Ratio ausrechnen
                    float x = indicator.x - (indicator.width * 0.25f) + (iconWidth * (i % (ICONS_PER_LINE)));
                    float y = indicator.y - floor(i / (ICONS_PER_LINE) * iconHeight);
                    image(indicator.icon, x, y, iconWidth, iconHeight);
                }
                text(value, indicator.x, height - 250);
            }
        }
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        year -= event.getCount();
        if (year > MAX_YEAR) {
            year = MAX_YEAR;
        } else if (year < MIN_YEAR) {
            year = MIN_YEAR;
        }
        slider.setValue(year);
    }

    public void handleSliderEvents(GValueControl slider, GEvent event) {
        year = slider.getValueI();
    }

    public static void main(String[] args) {
        PApplet.main(new String[] { "--present", WorldDevelopment.class.getName() });
    }
}
