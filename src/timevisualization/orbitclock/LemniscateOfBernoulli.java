package timevisualization.orbitclock;

import static processing.core.PApplet.cos;
import static processing.core.PApplet.sin;
import static processing.core.PApplet.sq;
import static util.Numbers.SQRT_2;
import util.structures.Point;

public class LemniscateOfBernoulli extends CurveFunction {

    public LemniscateOfBernoulli(float resizeFactor) {
        super(resizeFactor);
    }

    @Override
    public Point calculate(float angle) {
        float sin = sin(angle);
        float divisor = sq(sin) + 1;
        float dividend = resizeFactor * SQRT_2 * cos(angle);
        float x = dividend / divisor;
        float y = (dividend * sin) / divisor;
        return new Point(x, y);
    }

}
