/**
 * Test QuadTree
 * 
 * @author saleh
 * 
 */

package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import org.junit.Test;

import quadTree.DataEntry;
import quadTree.GeoLocation;
import quadTree.QuadTree;

public class QuadTreeTest {

    /**
     * Test search in whole world
     */
    @Test
    public void testSearchingWorld() {
        int maxCapacity = 1000;
        int numberOfDataEntries = 100000;

        QuadTree QuadTree = new QuadTree(maxCapacity);

        Queue<DataEntry> dataEntries = new LinkedList<DataEntry>();
        Random random = new Random();

        for (long i = 1; i <= numberOfDataEntries; i++) {
            DataEntry de = new DataEntry();
            de.setId(i);
            de.setGeoLocation(new GeoLocation(random.nextInt((int) (90 + 90)) + -90, random
                    .nextInt((int) (180 + 180)) + -180));
            dataEntries.add(de);
        }
        QuadTree.index(dataEntries);
        List<DataEntry> result =
                QuadTree.search(new GeoLocation(-90, -180), new GeoLocation(90, 180));

        // Search for all DataEntries in dataBulk in the result
        assertEquals(dataEntries.size(), result.size());
        // Ensure no redundant in the result
        for (DataEntry dataEntry : dataEntries)
            assertFalse(!result.contains(dataEntry));
    }

    /**
     * Test Get subset of DataEntries
     * 
     */
    @Test
    public void testSearchSubsets() {
        int maxCapacity = 256000;
        final int numberOfEntriesInRang = 1000000;

        QuadTree QuadTree = new QuadTree(maxCapacity);

        Random random = new Random();
        Queue<DataEntry> dataEntries = new LinkedList<DataEntry>();
        // (1) Range => Lat:(1..90), Long(1..180)
        for (long i = 1; i <= numberOfEntriesInRang; i++) {
            DataEntry de = new DataEntry();
            de.setId(i);
            de.setGeoLocation(new GeoLocation(random.nextInt(90) + 1 - 90,
                    random.nextInt(180) + 1 - 180));
            dataEntries.add(de);
        }
        QuadTree.index(dataEntries);
        List<DataEntry> result =
                QuadTree.search(new GeoLocation(0 - 90, 0 - 180), new GeoLocation(90 - 90,
                        180 - 180));

        assertEquals(numberOfEntriesInRang, result.size());

        // (2) Range => Lat:(91..181), Long(135..270)
        dataEntries = new LinkedList<DataEntry>();
        for (long i = numberOfEntriesInRang + 1; i <= 2 * numberOfEntriesInRang; i++) {
            DataEntry de = new DataEntry();
            de.setId(i);
            de.setGeoLocation(new GeoLocation(random.nextInt(45) + 91 - 90,
                    random.nextInt(90) + 181 - 180));
            dataEntries.add(de);
        }
        QuadTree.index(dataEntries);

        // (3) Range => Lat:(136..271), Long(180..360)
        dataEntries = new LinkedList<DataEntry>();
        for (long i = 2 * numberOfEntriesInRang + 1; i <= 3 * numberOfEntriesInRang; i++) {
            DataEntry de = new DataEntry();
            de.setId(i);
            de.setGeoLocation(new GeoLocation(random.nextInt(45) + 136 - 90,
                    random.nextInt(90) + 271 - 180));
            dataEntries.add(de);
        }
        QuadTree.index(dataEntries);

        result =
                QuadTree.search(new GeoLocation(0 - 90, 0 - 180), new GeoLocation(90 - 90,
                        180 - 180));
        assertEquals(result.size(), numberOfEntriesInRang);

        result =
                QuadTree.search(new GeoLocation(91 - 90, 181 - 180), new GeoLocation(135 - 90,
                        270 - 180));
        assertEquals(result.size(), numberOfEntriesInRang);

        result =
                QuadTree.search(new GeoLocation(0 - 90, 0 - 180), new GeoLocation(135 - 90,
                        270 - 180));
        assertEquals(result.size(), 2 * numberOfEntriesInRang);

        result =
                QuadTree.search(new GeoLocation(0 - 90, 0 - 180), new GeoLocation(180 - 90,
                        360 - 180));
        assertEquals(result.size(), 3 * numberOfEntriesInRang);
    }

    /**
     * Test search out of the bounding box
     */
    @Test
    public void testSearchingOutOfWorld() {
        double upperLat = 100;
        double upperLong = 50;
        double lowerLat = 20;
        double lowerLong = 10;
        int maxCapacity = 15;
        int numberOfDataEntries = 1000;

        GeoLocation bb_upperRight = new GeoLocation(upperLat + 50, upperLong + 50);

        GeoLocation bb_lowerLeft = new GeoLocation(upperLat + 10, upperLong + 5);

        QuadTree QuadTree = new QuadTree(maxCapacity);

        Queue<DataEntry> dataEntries = new LinkedList<DataEntry>();
        Random random = new Random();
        for (long i = 1; i <= numberOfDataEntries; i++) {
            DataEntry de = new DataEntry();
            de.setId(i);
            de.setGeoLocation(new GeoLocation(random.nextInt((int) (upperLat - lowerLat))
                    + lowerLat, random.nextInt((int) (upperLong - lowerLong)) + lowerLong));
            dataEntries.add(de);
        }

        QuadTree.index(dataEntries);
        List<DataEntry> result = QuadTree.search(bb_lowerLeft, bb_upperRight);

        assertEquals(result.size(), 0);
    }

    /**
     * Test search where there is no data
     */
    @Test
    public void testSearchingEmptyTree() {
        double upperLat = 100;
        double upperLong = 50;
        double lowerLat = 20;
        double lowerLong = 10;
        int maxCapacity = 100;

        QuadTree QuadTree = new QuadTree(maxCapacity);

        List<DataEntry> result =
                QuadTree.search(new GeoLocation(lowerLat, lowerLong), new GeoLocation(upperLat,
                        upperLong));

        assertEquals(result.size(), 0);
    }

    /**
     * Check processing Time, try inserting 15 bulk(100K dataEntry)
     */
    // @Test
    public void testInsertMaxNumOfDataEntries() {
        int maxCapacity = 256000;
        final int numberOfEntriesInRang = 100000;
        int numberOfTrials = 80;

        QuadTree QuadTree = new QuadTree(maxCapacity);
        Random random = new Random();

        Queue<DataEntry> dataEntries;
        while (numberOfTrials-- > 0) {
            dataEntries = new LinkedList<DataEntry>();
            for (long i = 1; i <= numberOfEntriesInRang; i++) {
                DataEntry de = new DataEntry();
                de.setId(i);
                de.setGeoLocation(new GeoLocation(random.nextInt(180) - 90,
                        random.nextInt(360) - 180));
                dataEntries.add(de);
            }
            double begin = System.currentTimeMillis();

            QuadTree.index(dataEntries);

            System.err.println("Trial # " + (80 - numberOfTrials) + " ,Time=  "
                    + (System.currentTimeMillis() - begin));
        }
    }
}
