package util.structures;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Maps {

    private Maps() {}

    public static <K, V extends Comparable<V>> Map<K, V> sortMapByValues(final Map<K, V> map) {
        Comparator<K> bvc = new Comparator<K>() {

            @Override
            public int compare(K a, K b) {
                return map.get(a).compareTo(map.get(b));
            }
        };

        TreeMap<K, V> sortedMap = new TreeMap<K, V>(bvc);
        sortedMap.putAll(map);
        return sortedMap;
    }

}
