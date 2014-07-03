package util;

/**
 * @author rza
 * @see <a href="https://code.google.com/p/fastapprox/">Fast approximate functions by Machined Learnings</a>
 */
public class ApproximationMath {

    private ApproximationMath() {}

    public static final double HALF_PI   = Math.PI / 2;

    public static final double DOUBLE_PI = Math.PI * 2;

    /**
     * By Machined Learnings. 20 times faster, about 1.3% derivation.
     * 
     * @param x
     * @return
     */
    public static float log(float x) {
        /*
         * Original C code:
         * union { float f; uint32_t i; } vx = { x };
         * float y = vx.i;
         * y *= 8.2629582881927490e-8f;
         * return y - 87.989971088f;
         */
        float y = Float.floatToIntBits(x);
        y *= 8.2629582881927490e-8f;
        return y - 87.989971088f;
    }

    public static float log2(float x) {
        float y = Float.floatToIntBits(x);
        y *= 1.1920928955078125e-7f;
        return y - 126.94269504f;
    }

    private static final double B = 4 / Math.PI;

    private static final double C = -4 / (Math.PI * Math.PI);

    // private static final double Q = 0.775;
    private static final double P = 0.225;

    /**
     * From http://forum.devmaster.net/t/fast-and-accurate-sine-cosine/9648
     * 
     * @param x
     * @param extraPrecision
     *            increase precision by factor 50
     * @return
     */
    public static double sin(double x, boolean extraPrecision) {
        if (x < -Math.PI) {
            x += DOUBLE_PI;
        } else if (x > Math.PI) {
            x -= DOUBLE_PI;
        }

        return internalSine(x, extraPrecision);
    }

    public static double sin(double x) {
        return sin(x, false);
    }

    /**
     * From http://forum.devmaster.net/t/fast-and-accurate-sine-cosine/9648
     * 
     * @param x
     * @param extraPrecision
     *            increase precision by factor 50
     * @return
     */
    public static double cos(double x, boolean extraPrecision) {
        x += HALF_PI;
        if (x > Math.PI) {
            x -= 2 * Math.PI;
        }

        return internalSine(x, extraPrecision);
    }

    public static double cos(double x) {
        return cos(x, false);
    }

    public static double internalSine(double x, boolean extraPrecision) {
        double y = B * x + C * x * Math.abs(x);

        if (extraPrecision) {

            y = P * (y * Math.abs(y) - y) + y;   // Q * y + P * y * abs(y)
        }
        return y;
    }
}
