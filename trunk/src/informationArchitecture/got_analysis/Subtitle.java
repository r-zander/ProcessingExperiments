package informationArchitecture.got_analysis;

import processing.data.JSONObject;

class Subtitle {

    int    index;

    Moment start = new Moment();

    Moment end   = new Moment();

    String text;

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.setInt("index", index);
        json.setString("start", start.toString());
        json.setString("end", end.toString());
        json.setString("text", text);

        return json;
    }
}
