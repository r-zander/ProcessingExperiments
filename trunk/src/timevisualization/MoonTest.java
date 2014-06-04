package timevisualization;

import com.koniaris.astronomy.JulianDay;
import com.koniaris.astronomy.Moon;

public class MoonTest {

    public MoonTest() {}

    public static void main(String[] args) throws Exception {
        JulianDay j = new JulianDay(2014, 01, 02);
        Moon m = new Moon(j);
        System.out.println("The moon is " + m.illuminatedPercentage() + "% full and "
                + (m.isWaning() ? "waning" : "waxing"));
    }

}
