/**
 * DataEntry, for simplicity DataEntry just has id and location
 */
package quadTree;

/**
 * @author saleh
 * 
 */
public class DataEntry {

    private Long id;
    private GeoLocation geoLocation;

    /**
     * 
     */
    public DataEntry() {}


    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the geoLocation
     */
    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

}
