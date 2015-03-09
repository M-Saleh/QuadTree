package quadTree;

import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static double test() {
        double upperLat = 100;
        double upperLong = 50;
        double lowerLat = 20;
        double lowerLong = 10;

        int maxCapacity = 10;
        int numberOfDataEntries = 1000;

        QuadTree qTree =
                new QuadTree(maxCapacity, new GeoLocation(upperLat, upperLong), new GeoLocation(
                        lowerLat, lowerLong));

        ArrayList<DataEntry> list = new ArrayList<DataEntry>();
        Random random = new Random();

        for (long i = 1; i <= numberOfDataEntries; i++) {
            DataEntry de = new DataEntry();
            de.setId(i);
            de.setGeoLocation(new GeoLocation(random.nextInt((int) (upperLat - lowerLat))
                    + lowerLat, random.nextInt((int) (upperLong - lowerLong)) + lowerLong));
            list.add(de);
            // System.out.println(de.getId() + " => "
            // + de.getGeoLocation().getLatitude() + " , "
            // + de.getGeoLocation().getLongitude());
        }

        double begin = System.currentTimeMillis();
        qTree.index(list);

        return (System.currentTimeMillis() - begin);
    }

    public static void main(String[] args) {

        double time1 = test();
        double time2 = test();
        double time3 = test();
        double time4 = test();

        System.out.println("Average Time = " + (time1 + time2 + time3 + time4) / 4 + " MS");

        // double upperLat = 90;
        // double upperLong = 180;
        // double lowerLat = -90;
        // double lowerLong = -180;
        // int maxCapacity = 3;
        // int numberOfDataEntries = 150;


        // qTree.printAllTree(qTree.rootNode);

        // ArrayList<DataEntry> result = qTree.search(new GeoLocation(99, 29),
        // new GeoLocation(61, 11));
        //
        //
        // System.out.println("Search Results Size : " + result.size());
        // for (DataEntry dataEntry : result) {
        // System.out.println(dataEntry.getId());
        //
        // if (qTree.rootNode.children[1].containsPoint(dataEntry
        // .getGeoLocation()))
        // System.out.println(dataEntry.getGeoLocation().getLatitude()
        // + " , " + dataEntry.getGeoLocation().getLongitude());
        // }
    }
}
