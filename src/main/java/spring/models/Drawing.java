package spring.models;

import java.util.LinkedList;
import java.util.List;

/**
 * Stocks a list of drawable segments
 *
 * @author Max
 */

public class Drawing {
    private final List<Segment> trace;

    public Drawing() {
        this.trace = new LinkedList<>();
    }

    public void addSegment(Segment segment) {
        trace.add(segment);
    }

    public List<Segment> getSegments() {
        return trace;
    }

    public void addLatLng(LatLng latLng, boolean drawing) {
        if (!drawing) {
            if (trace.size() > 0 && trace.get(trace.size() - 1).getSegment().size() > 0)
                addSegment(new Segment());
            return;
        }
        if (trace.size() == 0)
            addSegment(new Segment());
        trace.get(trace.size() - 1).addLatLng(latLng);
    }

    public String toJson() {
        String str = "[";
        for (Segment segment : this.trace) {
            str += segment.toJson();
        }
        return str + "]";
    }
}
