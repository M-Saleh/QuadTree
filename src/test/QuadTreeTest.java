package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;

import quadTree.DataEntry;
import quadTree.GeoLocation;
import quadTree.Node;
import quadTree.QuadTree;

public class QuadTreeTest {

	// Test Get All DataEntries
	public void testQuadTree_1() {
		double upperLat = 100;
		double upperLong = 50;
		double lowerLat = 20;
		double lowerLong = 10;
		int maxCapacity = 100;
		int numberOfDataEntries = 100000;

		QuadTree qTree = new QuadTree(maxCapacity, new GeoLocation(upperLat,
				upperLong), new GeoLocation(lowerLat, lowerLong));

		ArrayList<DataEntry> dataBulk = new ArrayList<DataEntry>();
		Random random = new Random();

		for (long i = 1; i <= numberOfDataEntries; i++) {
			DataEntry de = new DataEntry();
			de.setId(i);
			de.setGeoLocation(new GeoLocation(random
					.nextInt((int) (upperLat - lowerLat)) + lowerLat, random
					.nextInt((int) (upperLong - lowerLong)) + lowerLong));
			dataBulk.add(de);
		}

		qTree.index(dataBulk);
		ArrayList<DataEntry> result = qTree.search(new GeoLocation(upperLat,
				upperLong), new GeoLocation(lowerLat, lowerLong));

		// Search for all DataEntries in dataBulk in the result
		assertEquals(dataBulk.size(), result.size());
		for (DataEntry dataEntry : dataBulk)
			assertFalse(!result.contains(dataEntry));

	}

	@Test
	// Test Get subset of DataEntries
	public void testQuadTree_2() {
		double upperLat = 360;
		double upperLong = 360;
		double lowerLat = 0;
		double lowerLong = 0;
		int maxCapacity = 4;

		QuadTree qTree = new QuadTree(maxCapacity, new GeoLocation(upperLat,
				upperLong), new GeoLocation(lowerLat, lowerLong));

		Random random = new Random();
		ArrayList<DataEntry> dataBulk = new ArrayList<DataEntry>();
		ArrayList<Long> notFound = new ArrayList<Long>();
		
		// Range => Lat:(1..100), Long(1..200)
		for (long i = 1; i <= 100; i++) {
			DataEntry de = new DataEntry();
			de.setId(i);
			de.setGeoLocation(new GeoLocation(random.nextInt(150) + 1, random
					.nextInt(250) + 1));
			dataBulk.add(de);

			System.out.println(de.getId() + " => "
					+ de.getGeoLocation().getLatitude() + " , "
					+ de.getGeoLocation().getLongitude());
			
			notFound.add(de.getId());
		}

		qTree.index(dataBulk);

		ArrayList<DataEntry> result = qTree.search(new GeoLocation(250,
				250), new GeoLocation(lowerLat, lowerLong));
		
		qTree.printAllTree(qTree.rootNode);
		System.out.println("-----------------------------");
	
		System.out.println(qTree.numberOfDataEntries +" Node Inserted , " + result.size() +" Node fetched");
		for(DataEntry de : result){
			notFound.remove(de.getId());
		}
		
		for(Long id : notFound)
			System.out.println(id);
		
//		boolean [] check = new boolean[100];
//		for(DataEntry dataEntry : result)
//			check[dataEntry.getId().intValue()-1] = true;
//		
//		for(int i=0;i<check.length;i++)
//			if(!check[i])
//				System.out.println(i+1);
		
		// assertEquals(50, result.size());

		// // Range => Lat:(101..150), Long(201..300)
		// dataBulk = new ArrayList<DataEntry>();
		// for (long i = 1; i <= 1000; i++) {
		// DataEntry de = new DataEntry();
		// de.setId(i);
		// de.setGeoLocation(new GeoLocation(random.nextInt(50) + 101, random
		// .nextInt(100) + 201));
		// dataBulk.add(de);
		// }
		// qTree.index(dataBulk);
		//
		// // Range => Lat:(151..180), Long(301..360)
		// dataBulk = new ArrayList<DataEntry>();
		// for (long i = 1; i <= 1000; i++) {
		// DataEntry de = new DataEntry();
		// de.setId(i);
		// de.setGeoLocation(new GeoLocation(random.nextInt(30) + 151, random
		// .nextInt(60) + 301));
		// dataBulk.add(de);
		// }
		// qTree.index(dataBulk);
		//
		// result = qTree.search(new GeoLocation(100, 200),
		// new GeoLocation(0, 0));
		// System.out.println(result.size());
		// assertEquals(2000, result.size());
	}

	@Test
	// Test search out of the bounding box
	public void testQuadTree_3() {
		double upperLat = 100;
		double upperLong = 50;
		double lowerLat = 20;
		double lowerLong = 10;
		int maxCapacity = 100;
		int numberOfDataEntries = 100000;

		GeoLocation bb_upperRight = new GeoLocation(upperLat + 50,
				upperLong + 50);

		GeoLocation bb_lowerLeft = new GeoLocation(upperLat + 10, upperLong + 5);

		QuadTree qTree = new QuadTree(maxCapacity, new GeoLocation(upperLat,
				upperLong), new GeoLocation(lowerLat, lowerLong));

		ArrayList<DataEntry> dataBulk = new ArrayList<DataEntry>();
		Random random = new Random();
		for (long i = 1; i <= numberOfDataEntries; i++) {
			DataEntry de = new DataEntry();
			de.setId(i);
			de.setGeoLocation(new GeoLocation(random
					.nextInt((int) (upperLat - lowerLat)) + lowerLat, random
					.nextInt((int) (upperLong - lowerLong)) + lowerLong));
			dataBulk.add(de);
		}

		qTree.index(dataBulk);
		ArrayList<DataEntry> result = qTree.search(bb_upperRight, bb_lowerLeft);

		// Result should be empty
		assertEquals(result.size(), 0);
	}

}
