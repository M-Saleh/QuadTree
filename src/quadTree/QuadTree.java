/**
 * QuadTree Indexing Assumption : Index bounding is the whole world Create QuadTree with the
 * maxCapacity Integer which is the max number of Items(DataEntry for this Implementation).
 */

/**
 * @author saleh
 * 
 */

package quadTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class QuadTree {

    // couldn't be changed
    private static final int NUMBER_OF_CHILDREN = 4;
    private QuadTreeNode rootNode;
    private final int maxCapacity;
    private final static GeoLocation WORLD_UPPER_RIGHT = new GeoLocation(90, 180);
    private final static GeoLocation WORLD_LOWER_LEFT = new GeoLocation(-90, -180);

    /**
     * @param maxCapacity
     */
    public QuadTree(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        rootNode = new QuadTreeNode(WORLD_UPPER_RIGHT, WORLD_LOWER_LEFT);
    }

    /**
     * @return rootNode
     */
    public QuadTreeNode getRootNode() {
        return rootNode;
    }

    /**
     * @return NUMBER_OF_CHILDREN
     */
    public static int getNumberOfChildren() {
        return NUMBER_OF_CHILDREN;
    }

    /**
     * Index bulk of dataEntries
     * 
     * @param dataEntries
     */
    public void index(Queue<DataEntry> dataEntries) {
        for (DataEntry dataEntry : dataEntries) {
            rootNode.insert(dataEntry);
        }

        // subdivide if node has more than max capacity
        subdivideExceedingCapacityNodes(rootNode);
    }

    /**
     * Starting from the root, If node is a leaf, divides if its dataEntries size is mode than max
     * capacity
     * 
     * @param node
     */
    private void subdivideExceedingCapacityNodes(QuadTreeNode node) {
        if (!node.isLeaf()) {
            for (QuadTreeNode childNode : node.getChildren())
                subdivideExceedingCapacityNodes(childNode);
        } else if (node.getDataEntries().size() > maxCapacity) {
            QuadTreeNode[] children = new QuadTreeNode[QuadTree.getNumberOfChildren()];

            double halfHeight =
                    (node.getUpperRight().getLatitude() - node.getLowerLeft().getLatitude()) / 2;
            double halfWidth =
                    (node.getUpperRight().getLongitude() - node.getLowerLeft().getLongitude()) / 2;

            // Create the 4 children with equal quad regions
            for (int i = 0; i < children.length; i++) {
                children[i] =
                        new QuadTreeNode(new GeoLocation(node.getUpperRight().getLatitude()
                                - (i >> 1) * halfHeight, node.getUpperRight().getLongitude()
                                - halfWidth * (1 - i % 2)), new GeoLocation(node.getUpperRight()
                                .getLatitude() - (i >> 1) * halfHeight - halfHeight, node
                                .getUpperRight().getLongitude()
                                - halfWidth
                                * (1 - i % 2)
                                - halfWidth));
            }

            node.setChildren(children);

            // Assign points of current node to new children
            for (DataEntry dataEntry : node.getDataEntries()) {
                // insert new dataEntry to child which should have it
                node.getBoundingChild(dataEntry).insert(dataEntry);
            }

            node.setDataEntries(null);
        }
    }

    /**
     * Search for dataEntries in a given Bounding Box in the QuadTree
     * 
     * @param WORLD_UPPER_RIGHT
     * @param WORLD_LOWER_LEFT
     * @return ArrayList of dataEntries
     */
    public List<DataEntry> search(GeoLocation lowerLeft, GeoLocation upperRight) {
        ArrayList<DataEntry> dataEntries = new ArrayList<DataEntry>();
        addIntersectedDataEntries(dataEntries, rootNode, upperRight, lowerLeft);
        return dataEntries;
    }

    /**
     * search and Add founded dataEntries to result ArrayList for a given node.
     * 
     * @param dataEntriesResult
     * @param node
     * @param bb_upperRight
     * @param bb_lowerLeft
     */
    private void addIntersectedDataEntries(ArrayList<DataEntry> dataEntriesResult,
            QuadTreeNode node, GeoLocation upperRight, GeoLocation lowerLeft) {
        // Check if search boundry box contains a quad, Return all children, no
        // need to recheck on children
        if (node.isInside(upperRight, lowerLeft)) {
            addLeafDataEntries(dataEntriesResult, node);
            return;
        }

        // Check if search boundry box intersect with a quad,
        // Return intersected quad
        if (node.intersects(upperRight, lowerLeft)) {
            if (node.isLeaf()) {
                node.addIntersectedDataEntries(dataEntriesResult, upperRight, lowerLeft);
            } else {
                for (QuadTreeNode childNode : node.getChildren()) {
                    addIntersectedDataEntries(dataEntriesResult, childNode, upperRight, lowerLeft);
                }
            }
        }
    }

    /**
     * Add all dataEntries in this subtree to result, called when node lies inside the searching BB.
     * 
     * @param dataEntriesResult
     * @param node
     */
    private void addLeafDataEntries(ArrayList<DataEntry> dataEntriesResult, QuadTreeNode node) {
        if (node.isLeaf()) {
            dataEntriesResult.addAll(node.getDataEntries());
        } else {
            for (QuadTreeNode childNode : node.getChildren()) {
                addLeafDataEntries(dataEntriesResult, childNode);
            }
        }
    }
}
