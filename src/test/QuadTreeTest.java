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
	@Test
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
		// Ensure no redundant in the result
		for (DataEntry dataEntry : dataBulk)
			assertFalse(!result.contains(dataEntry));
	}

	// Test Get subset of DataEntries
	@Test
	public void testQuadTree_2() {
		double upperLat = 180;
		double upperLong = 360;
		double lowerLat = 0;
		double lowerLong = 0;
		int maxCapacity = 10;
		final int numberOfEntriesInRang = 1000;

		QuadTree qTree = new QuadTree(maxCapacity, new GeoLocation(upperLat,
				upperLong), new GeoLocation(lowerLat, lowerLong));

		Random random = new Random();
		ArrayList<DataEntry> dataBulk = new ArrayList<DataEntry>();
		// (1) Range => Lat:(1..100), Long(1..200)
		for (long i = 1; i <= numberOfEntriesInRang; i++) {
			DataEntry de = new DataEntry();
			de.setId(i);
			de.setGeoLocation(new GeoLocation(random.nextInt(90) + 1, random
					.nextInt(180) + 1));
			dataBulk.add(de);
		}
		qTree.index(dataBulk);

		ArrayList<DataEntry> result = qTree.search(new GeoLocation(90, 180),
				new GeoLocation(0, 0));
		assertEquals(numberOfEntriesInRang, result.size());

		// (2) Range => Lat:(101..150), Long(201..300)
		dataBulk = new ArrayList<DataEntry>();
		for (long i = numberOfEntriesInRang + 1; i <= 2 * numberOfEntriesInRang; i++) {
			DataEntry de = new DataEntry();
			de.setId(i);
			de.setGeoLocation(new GeoLocation(random.nextInt(45) + 91, random
					.nextInt(90) + 181));
			dataBulk.add(de);
		}
		qTree.index(dataBulk);

		// (3) Range => Lat:(151..180), Long(301..360)
		dataBulk = new ArrayList<DataEntry>();
		for (long i = 2 * numberOfEntriesInRang + 1; i <= 3 * numberOfEntriesInRang; i++) {
			DataEntry de = new DataEntry();
			de.setId(i);
			de.setGeoLocation(new GeoLocation(random.nextInt(45) + 136, random
					.nextInt(90) + 271));
			dataBulk.add(de);
		}
		qTree.index(dataBulk);

		result = qTree.search(new GeoLocation(135, 270), new GeoLocation(0, 0));
		assertTrue(result.size() >= 2 * numberOfEntriesInRang
				&& result.size() < 3 * numberOfEntriesInRang);

		result = qTree.search(new GeoLocation(180, 360), new GeoLocation(0, 0));
		assertEquals(3 * numberOfEntriesInRang, result.size());
	}

	// Test search out of the bounding box
	@Test
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
