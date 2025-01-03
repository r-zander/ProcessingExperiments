package timevisualization.orbitclock;

import util.structures.Point;

/**
 * Base for all possible curve functions.
 * 
 * @author rza
 */
public abstract class CurveFunction {

    float resizeFactor;

    public CurveFunction(float resizeFactor) {
        this.resizeFactor = resizeFactor;
    }

    public abstract Point calculate(float angle);
}
