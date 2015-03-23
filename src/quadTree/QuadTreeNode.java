/**
 * QuadTree Node, Each Node has Bounding Box, 4 or zero children
 */

/**
 * @author saleh
 * 
 */

package quadTree;

import java.util.ArrayList;

public class QuadTreeNode {

    // children array is null or has 4 elements NW,NE,SW,SE
    private QuadTreeNode[] children;
    private ArrayList<DataEntry> dataEntries;
    private GeoLocation lowerLeft;
    private GeoLocation upperRight;

    /**
     * @param lowerLeft
     * @param upperRight
     */
    public QuadTreeNode(GeoLocation upperRight, GeoLocation lowerLeft) {
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
        setDataEntries(new ArrayList<DataEntry>());
    }

    /**
     * @param dataEntries
     */
    public void setDataEntries(ArrayList<DataEntry> dataEntries) {
        this.dataEntries = dataEntries;
    }

    /**
     * @return children
     */
    public QuadTreeNode[] getChildren() {
        return children;
    }

    /**
     * Set Children
     * 
     * @param children
     */
    public void setChildren(QuadTreeNode[] children) {
        this.children = children;
    }

    /**
     * @return DataEntries
     */
    public ArrayList<DataEntry> getDataEntries() {
        return dataEntries;
    }

    /**
     * @return lowerLeft
     */
    public GeoLocation getLowerLeft() {
        return lowerLeft;
    }

    /**
     * @return upperRight
     */
    public GeoLocation getUpperRight() {
        return upperRight;
    }

    /**
     * Insert new dataEntry to the node
     * 
     * @param dataEntry
     * @return true if new dataEntry successfully inserted,else return false
     */
    public boolean insert(DataEntry dataEntry) {
        // Ensure that DataEntry location lies in this quad
        if (!contains(dataEntry.getGeoLocation())) {
            return false;
        }

        // Insert into the leaf node which contains new dataEntry location
        if (!isLeaf()) {
            return getBoundingChild(dataEntry).insert(dataEntry);
        }

        // add new dataEntry current node, even if size is more than the max(subdivided called after
        // inserting all the bulk)
        dataEntries.add(dataEntry);
        return true;
    }

    /**
     * Check if BB contains the node
     * 
     * @param bb_upperRight
     * @param bb_lowerLeft
     * @return true if node lies in the bounding box, else return false
     */
    public boolean isInside(GeoLocation bb_upperRight, GeoLocation bb_lowerLeft) {
        if (bb_upperRight.getLatitude() >= upperRight.getLatitude()
                && bb_upperRight.getLongitude() >= upperRight.getLongitude()
                && bb_lowerLeft.getLatitude() <= lowerLeft.getLatitude()
                && bb_lowerLeft.getLongitude() <= lowerLeft.getLongitude())
            return true;
        else
            return false;
    }


    /**
     * Add dataEntries which lies in BB to the results
     * 
     * @param dataEntryResult
     * @param bb_upperRight
     * @param bb_lowerLeft
     */
    public void addIntersectedDataEntries(ArrayList<DataEntry> dataEntriesResult,
            GeoLocation bb_upperRight, GeoLocation bb_lowerLeft) {
        for (DataEntry dataEntry : dataEntries) {
            double latitude = dataEntry.getGeoLocation().getLatitude();
            double longitude = dataEntry.getGeoLocation().getLongitude();
            if (latitude >= bb_lowerLeft.getLatitude() && latitude <= bb_upperRight.getLatitude()
                    && longitude >= bb_lowerLeft.getLongitude()
                    && longitude <= bb_upperRight.getLongitude())
                dataEntriesResult.add(dataEntry);
        }
    }


    /**
     * @return true if node is a leaf, else return false
     */
    public boolean isLeaf() {
        return (dataEntries != null);
    }


    /**
     * While subdividing, divided node's DataEntries will reassign to its children Given that
     * Node(Quad) divide equally to 4 Quads, This function return which children should containg the
     * passed DataEntry
     * 
     * @param dataEntry
     * @return Node(Quad) child which contains passed DataEntry
     */
    public QuadTreeNode getBoundingChild(DataEntry dataEntry) {
        double halfHeight = (upperRight.getLatitude() - lowerLeft.getLatitude()) / 2;
        double halfWidth = (upperRight.getLongitude() - lowerLeft.getLongitude()) / 2;

        return children[(((upperRight.getLongitude() - dataEntry.getGeoLocation().getLongitude()) >= halfWidth) ? 0
                : 1)
                | ((((upperRight.getLatitude() - dataEntry.getGeoLocation().getLatitude()) >= halfHeight) ? 1
                        : 0) << 1)];
    }


    /**
     * @param point
     * @return true if the Node(Quad) contains the passed point
     */
    private boolean contains(GeoLocation point) {
        if (isSameSideOfTheWorld()) {
            return point.getLongitude() >= lowerLeft.getLongitude()
                    && point.getLongitude() <= upperRight.getLongitude()
                    && point.getLatitude() >= lowerLeft.getLatitude()
                    && point.getLatitude() <= upperRight.getLatitude();
        } else if (point.getLongitude() > 0) {
            return point.getLongitude() >= lowerLeft.getLongitude()
                    && point.getLatitude() >= lowerLeft.getLatitude()
                    && point.getLatitude() <= upperRight.getLatitude();
        } else {
            return point.getLongitude() <= upperRight.getLongitude()
                    && point.getLatitude() >= lowerLeft.getLatitude()
                    && point.getLatitude() <= upperRight.getLatitude();
        }
    }


    /**
     * @return true if the Node(Quad) lies in the same side of the world
     */
    private boolean isSameSideOfTheWorld() {
        return lowerLeft.getLongitude() < upperRight.getLongitude();
    }


    /**
     * @param upperRightBB
     * @param lowerLeftBB
     * @return true if Bounding Box intersects with the Node(Quad)
     */
    public boolean intersects(GeoLocation upperRightBB, GeoLocation lowerLeftBB) {
        return this.upperRight.getLatitude() >= lowerLeftBB.getLatitude()
                && upperRightBB.getLatitude() >= this.lowerLeft.getLatitude()
                && this.upperRight.getLongitude() >= lowerLeftBB.getLongitude()
                && upperRightBB.getLongitude() >= this.lowerLeft.getLongitude();
    }
}
