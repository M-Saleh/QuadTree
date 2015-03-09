/**
 * 
 */

/**
 * @author mohamed
 * 
 */

package quadTree;

import java.util.ArrayList;
import java.util.HashMap;

public class QuadTree {

    public static final int NUMBER_OF_CHILDREN = 4; // couldn't be changed
    public Node rootNode;
    public static int numberOfQuads = 0; // testing

    public QuadTree(int maxCapacity, GeoLocation upperRight, GeoLocation lowerLeft) {
        rootNode = new Node(maxCapacity, upperRight, lowerLeft);
    }

    public void index(ArrayList<DataEntry> dataBulk) {
        for (DataEntry dataEntry : dataBulk) {
            if (!rootNode.insert(dataEntry))
                System.err.println("Not inserted");
        }
    }

    // Search for a given Bounding Box
    public ArrayList<DataEntry> search(GeoLocation upperRight, GeoLocation lowerLeft) {
        ArrayList<DataEntry> dataEntries = new ArrayList<DataEntry>();
        search(dataEntries, rootNode, upperRight, lowerLeft);
        return dataEntries;
    }

    private void search(ArrayList<DataEntry> dataEntries, Node node, GeoLocation upperRight,
            GeoLocation lowerLeft) {
        // Check if search boundry box contains a quad, Return all children, no
        // need to recheck on children
        if (node.liesInBoundingBox(upperRight, lowerLeft)) {
            getAllChildren(dataEntries, node);
            return;
        }

        // Check if search boundry box intersect with a quad,
        // Return intersected quad
        if (node.interset(upperRight, lowerLeft)) {
            if (node.children != null) {
                for (Node childNode : node.children) {
                    search(dataEntries, childNode, upperRight, lowerLeft);
                }
            } else { // leaf Node, get DataEnties in this region
                for (DataEntry dataEntry : node.dataEntries) {
                    if (dataEntry != null)
                        dataEntries.add(dataEntry);
                }
            }
        }
    }

    // Add all DataEntries in this subtree to result
    private void getAllChildren(ArrayList<DataEntry> dataEntries, Node node) {
        if (node.children != null) {
            for (Node childNode : node.children) {
                getAllChildren(dataEntries, childNode);
            }
        } else { // leaf Node, get DataEnties in this region
            for (DataEntry dataEntry : node.dataEntries) {
                if (dataEntry != null)
                    dataEntries.add(dataEntry);
            }
        }
    }

    // Print DataEntries ids
    public void printAllTree(Node root) {
        if (root.dataEntries != null) {
            numberOfQuads++;
            root.printIds();
            return;
        } else {
            root.printInternalNode();
        }

        for (Node node : root.children) {
            printAllTree(node);
        }
    }
}
