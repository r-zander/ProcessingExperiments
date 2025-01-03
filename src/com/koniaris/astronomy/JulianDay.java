// -*-Java-*-

package com.koniaris.astronomy;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.SimpleTimeZone;

/** The JulianDay supports turning Gregorian and Julian (Caesar) dates
    to/from Julius Scaliger's clever Julian Day Numbers.  In 1583,
    Julius had a great idea, and started to count the number of days
    starting on -4712-01-01 at noon (Z).  This is a wonderful
    "intermediate form" is still used for calendar calculations today!

    @author Kleanthes Koniaris
*/

public class JulianDay {

    private double julianDay;
    private int y;    // year
    private int mo;   // month
    private int day;  // day
    private int h;    // hour
    private int m;    // minute
    private int s;    // second
    private double d; // the day fraction (d <-> {day, h, m, s})

    private static SimpleTimeZone z;
    private static final double THIRTYPLUS;

    private double secondsInDay = 24 * 60 * 60; // how do I declare this constant?

    // ----------------------------------------------------------------------

    /** Returns the Julian Day, i.e., the number of days since 12h GMT
        in the year -4712, aka 4713 B.C. */
    public double julianDay() {return julianDay;}

    /** Reterns the day as a float that also accounts for H, M and S.
        For example, the middle (noon) of the 3rd day would be
        represented as 3.5.  */
    public double d() {return d;}

    /** Returns the year. */
    public int year() {return y;}

    /** Returns the month.  Note that this is the Calendar.JANUARY ==
        0, but we return one for January, so be careful.  What were
        they thinking at Sun to do such a non-standard thing---and for
        no good reason, as far as I can tell! */
    public int month() {return mo;}
    
    /** Returns the day of the month. */
    public int dayOfMonth() {return day;}

    /** Returns the day hour (0..23). */
    public int hour() {return h;}

    /** Returns the minute of the hour (0..59). */
    public int minute() {return m;}

    /** Ruterns the second of the hour. */
    public int second() {return s;}

    // ----------------------------------------------------------------------

    private void set_d() {
        d = (double) day +
            ((double) (s + 60 * (m + 60 * h))) / (double) secondsInDay;
    }

    // ----------------------------------------------------------------------

    // "javac -source 1.4 ...."  I should use the new "assert"
    // language feature, but wonder if it will create problems for
    // people with old Java environments....

    private void sanityCheck() throws Exception {
        if ((mo < 1) || (mo > 12) ||
            (day < 1) || (day > 31) || // we can do more here, of course....
            (d < 0.0) || (d >= 32.0) || // we can be on the 31.999....
            (h < 0) || (h > 23) ||
            (m < 0) || (m > 59) ||
            (s < 0) || (s > 59))
            throw new Exception();
    }

    // ----------------------------------------------------------------------

    /** Turns the current time into a JulianDay. */

    public JulianDay() throws Exception {
        GregorianCalendar g = new GregorianCalendar(z);
        Date now = new Date();
        g.setTime(now);
        y = g.get(Calendar.YEAR);
        mo = 1 + g.get(Calendar.MONTH); // +1 cuz January is 0 to Sun!
        day = g.get(Calendar.DAY_OF_MONTH);
        h = g.get(Calendar.HOUR_OF_DAY);
        m =  g.get(Calendar.MINUTE);
        s =  g.get(Calendar.SECOND);
        set_d();
        setJulianDay();
        sanityCheck();
    }

    // ----------------------------------------------------------------------

    /** Makes a new JulianDay, probably by means of adding offsets to
        JulianDates generated by other means. */

    public JulianDay(double julianDay)
        throws Exception {
        if (julianDay < 0) {
            throw new Exception("We can only handle positive Julian Days.");
        }
        this.julianDay = julianDay;
        double jd = julianDay + 0.5;
        int z = floor(jd);
        double f = jd - z;      // a fraction between zero and one
        int a = z;
        if (z >= 2299161) {     // (typo in the book)
            int alpha = floor((z - 1867216.25) / 36524.25);
            a += 1 + alpha - floor(alpha / 4.0);
        }
        int b = a + 1524;
        int c = floor((b - 122.1) / 365.25);
        int dd = floor(365.25 * c);
        int e = floor((b - dd) / THIRTYPLUS);
        // and then,
        this.d = b - dd - Math.floor(THIRTYPLUS * e) + f;
        setDayHourMinuteSecond();
        mo = e - ((e < 14) ? 1 : 13);
        // sometimes the year is too high by one
        y = c - ((mo > 2) ? 4716 : 4715);
        sanityCheck();
    }

    // ----------------------------------------------------------------------

    /** Creates a new JulianDay given a year, month, day, hour (24h),
        minute and second. */

    public JulianDay(int y, int mo, int day, int h, int m, int s)
    throws Exception {
        this.y = y;
        this.mo = mo;
        this.day = day;
        this.h = h;
        this.m = m;
        this.s = s;
        set_d();
        setJulianDay();
        sanityCheck();
    }

    // ----------------------------------------------------------------------

    /** Creates a new JulianDay given a year, month, and day-fraction,
        where 12.5 means noon on the 12th day of the month. */

    public JulianDay(int y, int mo, double d) throws Exception {
        this.y = y;
        this.mo = mo;
        this.d = d;
        setDayHourMinuteSecond();
        setJulianDay();
    }

    // ----------------------------------------------------------------------

    /** Sets day, hour, minute and second on the basis of d. */

    private void setDayHourMinuteSecond() throws Exception {
        int seconds = (int) Math.round(secondsInDay * d); // how many seconds in the day, d
        s = seconds % 60;
        int minutes = (seconds - s) / 60;
        m = minutes % 60;
        int hours = (minutes - m) / 60;
        h = hours % 24;
        int days = (hours - h) / 24;
        day = days;
    }

    // ----------------------------------------------------------------------

    static {
        JulianDay.z = new SimpleTimeZone(0, "Z");
        THIRTYPLUS = 30.6001;
    }

    // ----------------------------------------------------------------------

    /** Meeus claims that floor will never see a negative number in
        this chapter, so I wanted to be sure. */

    private int floor(double f) throws Exception {
        if (f < 0) {
            throw new Exception("Hey, this is negative: " + f);
        }
        return (int) Math.floor(f);
    }

    // ----------------------------------------------------------------------

    /** Is the point in time after 4 October 1582? */

    public boolean dateIsGregorian() {
        // 4 October 1582
        if (y > 1582) return true;
        if (y < 1582) return false;
        // shit, somebody is messing with us!
        if (mo > 10) return true;
        if (mo < 10) return false;
        // now it is evident that they're fucking with us---or it is
        // my Monte-Carlo testing.  :)
        if (day > 4) return true;
        if (day < 4) return false;
        return false;           // this is probably the last non-Gregorian date?
    }

    // ----------------------------------------------------------------------

    /** Returns the Juilan Day, the number of days since 12h GMT in
        the year -4712, aka 4713 B.C. */

    private void setJulianDay() throws Exception {
        int year = y;
        int month = mo;

        // here we go with Jean Meeus, p. 61 of 2nd edition of Astronomical Algorithms
        switch (month) {
        case 1:                 // January (don't dare say Calendar.JANUARY here, it will be wrong)
        case 2:                 // February
            year += -1;
            month += 12;
            break;
        }
        int b = 0;
        if (dateIsGregorian()) {
            int a = floor(((double) year) / 100.0);
            b = 2 - a + floor(a / 4.0);
        }
        // finally
        julianDay = Math.floor(365.25 * (year + 4716)) + Math.floor(THIRTYPLUS * (month + 1)) + d + b - 1524.5;
    }

    // ----------------------------------------------------------------------

    /** Returns an ISO-like date to show what's going on in here. */

    @Override
    public String toString() {
        return y + "-" + mo + "-" + d + "(" + day + ")" + "--" + h + ":" + m + ":" + s + "--" + julianDay;
    }

    // ----------------------------------------------------------------------

    /** Just a simple hack to help me debug. */

    private void showTime() {
        System.out.println(toString());
    }

    // ----------------------------------------------------------------------

    // This stuff was more for debugging....

    private boolean compare(int a, int b, String what) {
        if (a != b) {
            System.err.println(what + ": " + a + " and " + b + "; delta = " + (b - a));
            return false;
        }
        return true;
    }

    private boolean compare(double a, double b, String what) {
        if (Math.abs(a - b) > 1e-4) {
            System.err.println(what + ": " + a + " and " + b + "; delta = " + (b - a));
            return false;
        }
        return true;
    }

    // ----------------------------------------------------------------------

    /** Compares things that were supposed to be identical! */

    private void compare(JulianDay o, String what) {
        boolean r = true;
        r &= compare(year(), o.year(), "year");
        r &= compare(month(), o.month(), "month");
        r &= compare(d(), o.d(), "day (as fraction)");
        r &= compare(dayOfMonth(), o.dayOfMonth(), "day");
        r &= compare(hour(), o.hour(), "hour") ;
        r &= compare(minute(), o.minute(), "minute");
        r &= compare(second(), o.second(), "second");
        r &= compare(julianDay(), o.julianDay(), "Julian Day");
        if (! r) {
            System.err.println(what);
            showTime();
            o.showTime();
            System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }

    // ----------------------------------------------------------------------

    /** Tests all of the Meeus examples and then does a
        Monte-Carlo. */

    private static void test() throws Exception {

        // from the book....

        // Sputnik 1
        new JulianDay(1957, 10, 4.81).compare(new JulianDay(2436116.31), "Sputnik 1");

        // 333
        new JulianDay(333, 1, 27, 12, 0, 0).compare(new JulianDay(1842713.0), "333");

        // as well as....
        new JulianDay(2000, 1, 1.5).compare(new JulianDay(2451545.0), "Meeus example 1");
        new JulianDay(1999, 1, 1.0).compare(new JulianDay(2451179.5), "Meeus example 2");
        new JulianDay(1987, 1, 27.0).compare(new JulianDay(2446822.5), "Meeus example 3");
        new JulianDay(1987, 6, 19.5).compare(new JulianDay(2446966.0), "Meeus example 4");
        new JulianDay(1988, 1, 27.0).compare(new JulianDay(2447187.5), "Meeus example 5");
        new JulianDay(1988, 6, 19.5).compare(new JulianDay(2447332.0), "Meeus example 6");
        new JulianDay(1900, 1, 1.0).compare(new JulianDay(2415020.5), "Meeus example 7");
        new JulianDay(1600, 1, 1.0).compare(new JulianDay(2305447.5), "Meeus example 8");
        new JulianDay(1600, 12, 31.0).compare(new JulianDay(2305812.5), "Meeus example 9");
        new JulianDay(837, 4, 10.3).compare(new JulianDay(2026871.8), "Meeus example 10");
        new JulianDay(-123, 12, 31.0).compare(new JulianDay(1676496.5), "Meeus example 11");
        new JulianDay(-122, 1, 1.0).compare(new JulianDay(1676497.5), "Meeus example 12");
        new JulianDay(-1000, 7, 12.5).compare(new JulianDay(1356001.0 ), "Meeus example 13");
        new JulianDay(-1000, 2, 29.0).compare(new JulianDay(1355866.5), "Meeus example 14");
        new JulianDay(-1001, 8, 17.9).compare(new JulianDay(1355671.4), "Meeus example 15");
        new JulianDay(-4712, 1, 1.5).compare(new JulianDay(0.0), "Meeus example 16");

        // and then random conversions back-and-forth....
        Random r = new Random();
        for (int i=0; i<5000000; i++) {
            double f = r.nextFloat() * 3e6;
            JulianDay d1 = new JulianDay(f);
            int y = d1.year();
            int mo = d1.month();
            double d = d1.d();
            JulianDay d2 = new JulianDay(y, mo, d);
            int day = d1.dayOfMonth();
            int h = d1.hour();
            int m = d1.minute();
            int s = d1.second();
            JulianDay d3 = new JulianDay(y, mo, day, h, m, s);
            d1.compare(d2, "d1 with d2");
            d1.compare(d3, "d1 with d3");
        }
    }

    // ----------------------------------------------------------------------

    /** Asks for your birthday and suggests when you are 10,000 days
        old.  Then, it checks every relevant example from Jean Meeus's
        book, and invokes a Monte-Carlo that makes a million random
        Julian Date numbers and converts them back-and-forth checking
        for self-consistency. */

    public static void main(String[] arg)
        throws Exception {

        System.out.println("The current Z time is : ");
        new JulianDay().showTime();

        if (arg.length != 3) {
            throw new Exception("Provide year month day for your birthday.");
        }
        JulianDay birthday = 
            new JulianDay(new Integer(arg[0]).intValue(), new Integer(arg[1]).intValue(), new Double(arg[2]).doubleValue());
        System.out.println("You will be 25000 days old on:");
        new JulianDay(birthday.julianDay() + 25e3).showTime();
        System.err.println("Running the test suite....");
        test();
    }

    // ----------------------------------------------------------------------

}
