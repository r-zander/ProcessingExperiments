package informationArchitecture.twitter;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import twitter4j.GeoLocation;

public class TwitterMap extends PApplet {

    PImage map;

    @Override
    public void setup() {
        size(1350, 675);
        frameRate(1);

        map = loadImage("globeVisualization/world.topo.bathy.200401.3x1350x675.jpg");
    }

    @Override
    public void draw() {

        image(map, 0, 0, width, height);

        GeoLocation berlinLocation = new GeoLocation(52.5243700, 13.4105300);
        PVector vector = geoLocationToPoint(berlinLocation);

        fill(255, 255, 0);
        stroke(255, 255, 0);
        strokeWeight(20);

        point(vector.x, vector.y);
    }

    private PVector geoLocationToPoint(GeoLocation geoLocation) {
//        latitude    = 41.145556; // (φ)
//        longitude   = -73.995;   // (λ)

        int mapWidth = width;
        int mapHeight = height;

//        // get x value
//        float x = (float) ((geoLocation.getLongitude() + 180) * (mapWidth / 360));
//
//        // convert from degrees to radians
//        float latRad = (float) (geoLocation.getLatitude() * PI / 180);
//
//        // get y value
//        float mercN = log(tan((PI / 4) + (latRad / 2)));
//        float y = (mapHeight / 2) - (mapWidth * mercN / (2 * PI));
//        return new PVector(x, y);

        float x = (float) ((180 + geoLocation.getLongitude()) * (mapWidth / 360));
        float y = (float) ((90 - geoLocation.getLatitude()) * (mapHeight / 180));

        return new PVector(x, y);
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", TwitterMap.class.getName() });
    }
}