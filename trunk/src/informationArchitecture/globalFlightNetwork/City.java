package informationArchitecture.globalFlightNetwork;

import lombok.Data;
import lombok.EqualsAndHashCode;
import processing.core.PVector;

@Data
@EqualsAndHashCode(of = "name")
public class City implements Comparable<City> {

    private final String name;

    private PVector      location;

    @Override
    public int compareTo(City otherCity) {
        return name.compareTo(otherCity.name);
    }
}
