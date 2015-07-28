package informationArchitecture.betterWorld;

import processing.core.PApplet;
import processing.core.PImage;
import util.Interpolator;

public class Indicator {

    String          title;

    String          tableHeader;

    private Float[] values;

    Integer[]       intValues;

    float           x, y, width;

    float           multiplier = 1;

    PImage          icon;

    public Indicator(String title, String tableHeader, Float[] values, PImage icon) {
        this.title = title;
        this.tableHeader = tableHeader;
        this.values = values;
        this.icon = icon;
    }

    public void setValue(int index, Float value) {
        values[index] = value;
    }

    public Integer getValue(int index) {
        return intValues[index];
    }

    public void interpolateMissingValues() {
        Interpolator.Interpolate(values, "linear");

        intValues = new Integer[values.length];
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                intValues[i] = PApplet.round(values[i] * multiplier);
            }
        }
    }
}
