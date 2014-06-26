package util.structures;

public class FloatRange {

    public float min;

    public float max;

    public FloatRange() {}

    public FloatRange(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public float distance() {
        return max - min;
    }

}
