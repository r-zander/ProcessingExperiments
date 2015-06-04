package informationArchitecture.got_analysis;

class Moment {

    int hour;

    int minute;

    int second;

    int milli;

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d,%03d", hour, minute, second, milli);
    }
}
