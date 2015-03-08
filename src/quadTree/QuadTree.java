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
	
	public static int numberOfDataEntries = 0 ; 

	public QuadTree(int maxCapacity, GeoLocation upperRight,
			GeoLocation lowerLeft) {
		rootNode = new Node(maxCapacity, upperRight, lowerLeft);
	}

	public void index(ArrayList<DataEntry> dataBulk) {
		for (DataEntry dataEntry : dataBulk) {
			if(!rootNode.insert(dataEntry))
				System.out.println("Not inserted");
		}
	}

	// Search for a given Bounding Box
	public ArrayList<DataEntry> search(GeoLocation upperRight,
			GeoLocation lowerLeft) {
		ArrayList<DataEntry> dataEntries = new ArrayList<DataEntry>();
		search(dataEntries,
				rootNode,
				upperRight,
				lowerLeft,
				new GeoLocation(upperRight.getLatitude(), lowerLeft
						.getLongitude()),
				new GeoLocation(lowerLeft.getLatitude(), upperRight
						.getLongitude()));
		return dataEntries;
	}

	private void search(ArrayList<DataEntry> dataEntries, Node node,
			GeoLocation upperRight, GeoLocation lowerLeft,
			GeoLocation upperLeft, GeoLocation lowerRight) {
		// Check if search boundry box contains a quad, Return all
		// children
		if (node.liesInBoundingBox(upperRight, lowerLeft)) {
			getAllChildren(dataEntries, node);
			return;
		}

		// Check if search boundry box intersect with a quad, Return
		// intersected quad
		if (node.containsPoint(upperRight) || node.containsPoint(lowerLeft)
				|| node.containsPoint(upperLeft)
				|| node.containsPoint(lowerRight)) {
			if (node.children != null) {
				for (Node childNode : node.children) {
					search(dataEntries, childNode, upperRight, lowerLeft,
							upperLeft, lowerRight);
				}
			} else { // leaf Node, get DataEnties in this region
				for (DataEntry dataEntry : node.dataEntries) {
					if (dataEntry != null)
						dataEntries.add(dataEntry);
				}
			}
		}
	}

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

	public void printAllTree(Node root) {
		if (root.dataEntries != null) {
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
