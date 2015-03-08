package quadTree;

/**
 * @author elmongui
 *
 */
public class GeoLocation {

    private double latitude;
    private double longitude;

    /**
     * 
     */
    public GeoLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }
}
