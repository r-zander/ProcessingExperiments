package informationArchitecture.globeVisualization;

import lombok.Data;

@Data
public class City {

    private String name;

    private String country;

    private long   population;

    private float  lat;

    private float  lng;

    private float  x, y, z;

    public void set3dCoordinates(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
