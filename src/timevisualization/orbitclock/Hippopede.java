package timevisualization.orbitclock;

import static processing.core.PApplet.cos;
import static processing.core.PApplet.sin;
import static processing.core.PApplet.sqrt;
import util.structures.Point;

public class Hippopede extends CurveFunction {

    private float a;

    private float b;

    public Hippopede(float resizeFactor, float a, float b) {
        super(resizeFactor);
        this.a = a;
        this.b = b;
    }

    @Override
    public Point calculate(float angle) {
        float sin = sin(angle);
        float sqrt = sqrt(a - b * sin * sin);
        float x = resizeFactor * 2 * cos(angle) * sqrt;
        float y = resizeFactor * 2 * sin(angle) * sqrt;
        return new Point(x, y);
    }

}
