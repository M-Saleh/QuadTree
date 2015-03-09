/**
 * 
 */

/**
 * @author mohamed
 * 
 */

package quadTree;

public class Node {

    public Node[] children; // children array is null or has 4 elements
    // NW,NE,SW,SE
    DataEntry[] dataEntries;
    private GeoLocation upperRight;
    private GeoLocation lowerLeft;
    private int maxCapacity;

    public Node(int maxCapacity, GeoLocation upperRight, GeoLocation lowerLeft) {
        this.maxCapacity = maxCapacity;
        dataEntries = new DataEntry[maxCapacity];
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
    }

    public boolean insert(DataEntry dataEntry) {
        // Ensure that DataEntry location lies in this quad
        if (!containsPoint(dataEntry.getGeoLocation())) {
            return false;
        }

        // Search for the leaf node which should containd new point
        if (children != null) {
            for (Node childNode : children) {
                if (childNode.containsPoint(dataEntry.getGeoLocation()))
                    return childNode.insert(dataEntry);
            }
            System.err.println("No Child contains new DE while parent contains it");
            return false; // shouldn't excuted, point should lies in one of the
            // four children
        }

        // try to add new DE if Quad still has empty slots
        if (addDataEntry(dataEntry))
            return true;
        else {
            subdivide();
            return insert(dataEntry);
        }
    }

    private boolean addDataEntry(DataEntry dataEntry) {
        for (int i = 0; i < dataEntries.length; i++) {
            if (dataEntries[i] == null) {
                dataEntries[i] = dataEntry;
                return true;
            }
        }
        return false;
    }

    public boolean liesInBoundingBox(GeoLocation bb_upperRight, GeoLocation bb_lowerLeft) {
        if (bb_upperRight.getLatitude() >= upperRight.getLatitude()
                && bb_upperRight.getLongitude() >= upperRight.getLongitude()
                && bb_lowerLeft.getLatitude() <= lowerLeft.getLatitude()
                && bb_lowerLeft.getLongitude() <= lowerLeft.getLongitude())
            return true;
        else
            return false;
    }

    public boolean containsPoint(GeoLocation point) {
        if (point.getLatitude() >= lowerLeft.getLatitude()
                && point.getLatitude() <= upperRight.getLatitude()
                && point.getLongitude() >= lowerLeft.getLongitude()
                && point.getLongitude() <= upperRight.getLongitude())
            return true;
        else
            return false;
    }

    public boolean interset(GeoLocation bb_upperRight, GeoLocation bb_lowerLeft) {
        if ((upperRight.getLatitude() < bb_lowerLeft.getLatitude())
                || (lowerLeft.getLatitude() > bb_upperRight.getLatitude())
                || (lowerLeft.getLongitude() > bb_upperRight.getLongitude())
                || (upperRight.getLongitude() < bb_lowerLeft.getLongitude()))
            return false;
        else
            return true;
    }

    private void subdivide() {
        // Create the 4 children with specific area
        children = new Node[QuadTree.NUMBER_OF_CHILDREN];

        double latitude_diff = upperRight.getLatitude() - lowerLeft.getLatitude();
        double longitude_diff = upperRight.getLongitude() - lowerLeft.getLongitude();

        children[0] =
                new Node(maxCapacity, new GeoLocation(upperRight.getLatitude(),
                        lowerLeft.getLongitude() + longitude_diff / 2), new GeoLocation(
                        lowerLeft.getLatitude() + latitude_diff / 2, lowerLeft.getLongitude()));

        children[1] =
                new Node(maxCapacity, new GeoLocation(upperRight.getLatitude(),
                        upperRight.getLongitude()), new GeoLocation(lowerLeft.getLatitude()
                        + latitude_diff / 2, lowerLeft.getLongitude() + longitude_diff / 2));

        children[2] =
                new Node(maxCapacity, new GeoLocation(lowerLeft.getLatitude() + latitude_diff / 2,
                        lowerLeft.getLongitude() + longitude_diff / 2), new GeoLocation(
                        lowerLeft.getLatitude(), lowerLeft.getLongitude()));

        children[3] =
                new Node(maxCapacity, new GeoLocation(lowerLeft.getLatitude() + latitude_diff / 2,
                        upperRight.getLongitude()), new GeoLocation(lowerLeft.getLatitude(),
                        lowerLeft.getLongitude() + longitude_diff / 2));

        // Assign points of current node to new children
        for (DataEntry dataEntry : dataEntries) {
            for (Node child : children) {
                if (child.insert(dataEntry))
                    break;
            }
        }
        dataEntries = null;
    }

    public void printIds() {
        System.out.println("Ids in " + "upperRight: " + upperRight.getLatitude() + ","
                + upperRight.getLongitude() + " , lowerLeft: " + lowerLeft.getLatitude() + ","
                + lowerLeft.getLongitude());
        for (DataEntry dataEntry : dataEntries) {
            if (dataEntry != null) {
                System.out.print(dataEntry.getId() + ",");
            }
        }

        System.out.println();
    }

    public void printInternalNode() {
        System.out.println("Internal Node " + "upperRight: " + upperRight.getLatitude() + ","
                + upperRight.getLongitude() + " , lowerLeft: " + lowerLeft.getLatitude() + ","
                + lowerLeft.getLongitude());
    }

    public static void main(String[] args) {

        // TEST dividing function

        GeoLocation two = new GeoLocation(100, 50);
        GeoLocation one = new GeoLocation(20, 10);

        Node testNode = new Node(0, two, one);

        testNode.subdivide();

        System.out.println("TestNode");
        System.out.print("UpperRight => latitude= " + testNode.upperRight.getLatitude()
                + " longitude= " + testNode.upperRight.getLongitude());
        System.out.println(",, LowerLeft => latitude= " + testNode.lowerLeft.getLatitude()
                + " longitude= " + testNode.lowerLeft.getLongitude());
        System.out.println();
        for (Node node : testNode.children) {
            System.out.println(node);
            System.out.print("UpperRight => latitude= " + node.upperRight.getLatitude()
                    + " longitude= " + node.upperRight.getLongitude());
            System.out.println(",, LowerLeft => latitude= " + node.lowerLeft.getLatitude()
                    + " longitude= " + node.lowerLeft.getLongitude());
            System.out.println();
        }

    }
}
