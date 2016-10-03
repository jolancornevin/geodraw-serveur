package main.java.fr.insa.ot3.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a list of gps fixes top be drawn connected.
 * @author Max 
 */
public class Segment {
    private final List<LatLng> segment;

    public Segment()
    {
        this.segment = new LinkedList<>();
    }

    public List<LatLng> getSegment()
    {
        return Collections.unmodifiableList(segment);
    }
    
    
    public void addLatLng(LatLng latLng)
    {
        segment.add(latLng);
    }        
    
}
