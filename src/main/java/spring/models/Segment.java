package spring.models;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a list of gps fixes top be drawn connected.
 *
 * @author Max
 */
public class Segment {
    private final List<LatLng> segment;

    public Segment() {
        this.segment = new LinkedList<>();
    }

    public List<LatLng> getSegment() {
        return Collections.unmodifiableList(segment);
    }

    public void addLatLng(LatLng latLng) {
        segment.add(latLng);
    }

    public String toJson() {
        String str = "[";
        for(LatLng latlng : this.segment){
            str += latlng.toJson();
        }
        return str + "]";
    }
}
