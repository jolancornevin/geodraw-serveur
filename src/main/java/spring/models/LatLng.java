package spring.models;

/**
 * Respresents  a single GPS fix
 *
 * @author Max
 */
public class LatLng {
    private final double lat;
    private final double lng;

    public LatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String toJson() {
        return "{lat:" + lat + ", lng:" + lng + "}";
    }
}
