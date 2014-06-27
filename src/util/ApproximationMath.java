package util;

/**
 * @author rza
 * @see <a href="https://code.google.com/p/fastapprox/">Fast approximate functions by Machined Learnings</a>
 */
public class ApproximationMath {

    private ApproximationMath() {}

    /**
     * By Machined Learnings
     * 
     * @param x
     * @return
     */
    public static float fasterlog(float x) {
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

    /**
     * By http://martin.ankerl.com/2007/02/11/optimized-exponential-functions-for-java/
     * <b>Do not use!</b> Only here for comparison. Very poor accuracy and only half as fast as
     * {@link #fasterlog(float)}.
     * 
     * @param x
     * @return
     */
    private static double fastLog(double x) {
        return 6 * (x - 1) / (x + 1 + 4 * (Math.sqrt(x)));
    }

    public static void main(String[] args) {
        float[] valuesToTest = new float[] { 0.01f, 0.1f, 1, 10, 100, 1000, 10000 };
        for (float value : valuesToTest) {
            double mathLog = Math.log(value);
            double fastLog = fastLog(value);
            float fasterlog = fasterlog(value);

            System.out.println(String.format(
                    "%f: mathLog = %f | fastLog = %f | fasterLog = %f",
                    value,
                    mathLog,
                    fastLog,
                    fasterlog));
        }

        long numberOfCalls = Math.round(Math.pow(10, 7));
        System.out.println("Number of calls: " + numberOfCalls);
        long startTime, estimatedTime;
        float log;

        log = 0.01f;
        startTime = System.nanoTime();
        for (int i = 0; i < numberOfCalls; i++) {
            log = (float) Math.log(log);
        }
        estimatedTime = System.nanoTime() - startTime;
        System.out.println("MathLog   = " + estimatedTime + "ns");

        startTime = System.nanoTime();
        for (int i = 0; i < numberOfCalls; i++) {
            log = (float) fastLog(log);
        }
        estimatedTime = System.nanoTime() - startTime;
        System.out.println("fastLog   = " + estimatedTime + "ns");

        startTime = System.nanoTime();
        for (int i = 0; i < numberOfCalls; i++) {
            log = fasterlog(log);
        }
        estimatedTime = System.nanoTime() - startTime;
        System.out.println("fasterlog = " + estimatedTime + "ns");
    }
}
