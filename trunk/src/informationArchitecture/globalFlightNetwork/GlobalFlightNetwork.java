package informationArchitecture.globalFlightNetwork;

import java.util.Map;
import java.util.TreeMap;

import processing.core.PApplet;
import processing.core.PVector;
import processing.data.Table;
import processing.data.TableRow;

public class GlobalFlightNetwork extends PApplet {

    Table             flightsTable;

    Map<String, City> cities    = new TreeMap<String, City>();

    int               dimension;

    float             centerX;

    float             centerY;

    float             bowFactor = 0.5f;

    @Override
    public void setup() {
        size(displayWidth, displayHeight);
        dimension = min(height, width);
        centerX = width / 2;
        centerY = height / 2;

        background(255);
        fill(0, 64, 128);
        noStroke();
        textSize(10);
//        textAlign(CENTER);

        Table citiesTable = loadTable("globalflightsnetwork/citiesTable.csv", "header");

        for (TableRow row : citiesTable.rows()) {
            String cityName = row.getString("city name");
            cities.put(cityName, new City(cityName));
        }

        float circosRadius = dimension / 2f * 0.9f;

        float angleDistance = TWO_PI / cities.size();

        int index = 0;
        for (City city : cities.values()) {

            PVector vector = new PVector();
            float angle = angleDistance * index;
            vector.x = centerX + cos(angle) * circosRadius;
            vector.y = centerY + sin(angle) * circosRadius;

            city.setLocation(vector);

            pushMatrix();
            translate(vector.x, vector.y);
            if (angle > HALF_PI && angle < PI + HALF_PI) {
                angle -= PI;
                textAlign(RIGHT);
            } else {
                textAlign(LEFT);
            }
            rotate(angle);
            text(city.getName(), 0, 0);
            popMatrix();

            index++;
        }

        flightsTable = loadTable("globalflightsnetwork/citiesToCities.csv", "header");
    }

    @Override
    public void draw() {
        if (frameCount < flightsTable.getRowCount()) {
            TableRow row = flightsTable.getRow(frameCount);
            int offset = 0;

//        for (TableRow row : flightsTable.rows()) {
//        for (int i = flightsTable.getRowCount() - 1; i >= 0; i--) {
//            TableRow row = flightsTable.getRow(i);
            int numberOfRoutes = row.getInt("number of routes");
            if (numberOfRoutes > offset) {
                City departureCity = cities.get(row.getString("departure city"));
                City arrivalCity = cities.get(row.getString("arrival city"));

                noFill();
//              float hue = lerp(288, 0, (numberOfRoutes - 1 - offset) / (17f - offset));
                float alpha = lerp(0, 255, (numberOfRoutes - 1 - offset) / (17f - offset));
//                colorMode(HSB);
//                stroke(hue, 255, 255);
                stroke(0, alpha);
                strokeWeight(1);
                bezier(
                        arrivalCity.getLocation().x,
                        arrivalCity.getLocation().y,
                        (centerX + arrivalCity.getLocation().x) * bowFactor,
                        (centerY + arrivalCity.getLocation().y) * bowFactor,
                        (centerX + departureCity.getLocation().x) * bowFactor,
                        (centerY + departureCity.getLocation().y) * bowFactor,
                        departureCity.getLocation().x,
                        departureCity.getLocation().y);
            }
//        }
        } else {
            noLoop();
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { /* "--present", */GlobalFlightNetwork.class.getName() });
    }
}
