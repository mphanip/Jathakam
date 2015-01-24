/* 
 * Yet to decide on the license
 */

package mpp.samples;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import mpp.jathakamu.Constants;
import mpp.jathakamu.Profile;
import mpp.jathakamu.ProfileSettings;
import mpp.jathakamu.calculations.SupportCalcs;
import mpp.jathakamu.types.Place;
import mpp.jathakamu.types.Planet;
import mpp.jathakamu.types.Planet;
import mpp.jathakamu.types.Range;
import mpp.jathakamu.types.VDNode;
import mpp.jathakamu.utils.DateTime;
import mpp.jathakamu.utils.ViewUtils;
import swisseph.SweDate;

import static mpp.jathakamu.types.Planet.getVimshottariDasaPlanets;

/**
 *
 * @author phani
 */
public class LambdaExpTest
{
    public static void main(String[] args)
            throws Exception
    {
//        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
//        List<Integer> newList = new ArrayList<>();
//        
//        numbers.forEach(System.out::println);
//        
//        numbers.stream()
//                .map(value -> value * 3)
//                .forEach(newList::add)
//                ;
//        System.out.println("New List = " + newList);
//        
//        numbers.stream()
//                .filter(value ->
//                        {
//                            boolean flag = newList.contains(value);
//                            return value % 2 == 0 && !flag;
//                        })
//                .forEach(newList::add)
//        ;
//        
//        List<Integer> collectedList = numbers.stream()
//                .filter(value ->
//                        {
//                            boolean flag = newList.contains(value);
//                            return value % 2 == 0 && !flag;
//                        })
//                .collect(Collectors.<Integer>toList())
//                ;
//        
//        System.out.println("Collected List = " + collectedList);
//        
//        testStreams();
//        testParallelStreams();
//        testRange();
        testVD();
    }
    
    private static void testStreams()
    {
        IntStream.iterate(1, number -> number + 1)
                .map(number -> number * number)
                .limit(25)
                .forEach(number -> System.out.print(number + " "));
        System.out.println("");
    }
    
    private static void testParallelStreams()
    {
        IntStream.iterate(1, number -> number + 1)
                .parallel()
                .filter(value -> value % 3 == 0)
                .limit(25)
                .forEach(number -> System.out.print(number + " "));
        System.out.println("");
    }
    
    private static void testRange()
    {
        BiFunction<Range, Number, Boolean> compare;
        compare = (Range x1, Number x2) ->
        {
            double min = x1.getMin().doubleValue();
            double max = x1.getMax().doubleValue();
            double num = x2.doubleValue();
            boolean flag = (num >= min && num <= max);
            return flag;
        };
        
        boolean flag = compare.apply(new Range.Builder().min(10).max(20).build(), 15);
        System.out.println("Flag = " + flag);
        
        flag = compare.apply(new Range.Builder().min(10).max(20).build(), 25);
        System.out.println("Flag = " + flag);
        
        Map<Range,Number> hRangeMap = getRanges();
        
        System.out.println("Hash Map size = " + hRangeMap.size());
        
        double []searchNums = {39.99999999999999D, 40D, 2345, 5123, 10000, 12345, 15234, 23456, 29999};
        
        long t1 = System.currentTimeMillis();
        Set<Range>  keys = hRangeMap.keySet();
        long t2 = System.currentTimeMillis();
        System.out.println("Time taken to get keys = " + (t2-t1));
        
        for (double searchNum : searchNums)
        {
            t1 = System.currentTimeMillis();
            Stream<Range> foundRange = keys.stream().filter(r -> r.inRange(searchNum)).limit(1);
            t2 = System.currentTimeMillis();

            foundRange.forEach(range -> System.out.print(searchNum + " ==> "
                    + range + ", value = "
                    + hRangeMap.get(range)));
            System.out.println(", Time taken is " + (t2-t1));
        }
    }
    
    private static Map<Range,Number> getRanges()
    {
        Map<Range,Number> hRangeMap = new HashMap<>();

        Random rndNum = new Random(1);
        
        long t1 = System.currentTimeMillis();
        IntStream.iterate(0, number -> number + 10)
                .limit(3000)
                .forEach(value -> hRangeMap.putIfAbsent(new Range.Builder().min(value).max(value+10).build(), rndNum.nextInt()));
        long t2 = System.currentTimeMillis();
        System.out.println("Time taken to build hashtable = " + (t2-t1));

//        hRangeMap.putIfAbsent(new Range.PlaceBuilder().min(0).max(10).build(), 1);
//        hRangeMap.putIfAbsent(new Range.PlaceBuilder().min(10).max(20).build(), 2);
//        hRangeMap.putIfAbsent(new Range.PlaceBuilder().min(20).max(30).build(), 3);
//        hRangeMap.putIfAbsent(new Range.PlaceBuilder().min(30).max(40).build(), 4);
//        hRangeMap.putIfAbsent(new Range.PlaceBuilder().min(40).max(50).build(), 5);
        
        return hRangeMap;
    }

    private static void testVD()
            throws Exception
    {
        Planet.getVDPlanetStream(Planet.MERCURY, Planet.SATURN)
                .forEach(planet -> {System.out.println(planet);});
        
        
//        System.out.println("Testing planet VD\nTest1");
//        Stream<Planet> st = Planet.getVDPlanetStream(Planet.KETU);
//        st.forEach(planet -> {System.out.println(planet);});
//        
//        System.out.println("Test2");
//        st = Planet.getVDPlanetStream(Planet.MERCURY, Planet.SATURN);
//        st.forEach(planet -> {System.out.println(planet);});
        
        double[] testData =
        {
            ViewUtils.getDegreeAsDouble(11 * 30 + 29, 38, 46),
            ViewUtils.getDegreeAsDouble(11 * 30 + 20, 49, 58),
            ViewUtils.getDegreeAsDouble(9 * 30 + 12, 28, 23),
            ViewUtils.getDegreeAsDouble(10 * 30 + 24, 13, 53),
            ViewUtils.getDegreeAsDouble(2 * 30 + 18, 55, 21)
        };
        
        ProfileSettings profileSettings = new ProfileSettings();
        profileSettings.setKPNewAynamsa();
        profileSettings.setUseGeocentric(false);

        Place place = new Place.PlaceBuilder().values("", 78.5, 17.333333, Constants.DEFAULT_TIME_ZONE).build();
        DateTime dateTime = new DateTime(1973, 4, 4, 8, 59, 8, 0, Constants.DEFAULT_TIME_ZONE);
        Date dob = dateTime.getDateTime();
        
        double tjd_ut = SupportCalcs.getJulianDay(dob, place);
        
        System.out.println("***** dob = " + dob + ", tjd_ut" + tjd_ut
                    + ", Again coverting it back to date = "
                    + SweDate.getDate(tjd_ut));

        Profile profile = new Profile(tjd_ut, place, profileSettings);
        
//        TreeNode vdTree = SupportCalcs.getVDTree(System.currentTimeMillis(), testData[0], profile);
//        Enumeration<TreeNode> children = vdTree.children();
//        
//        int childCount;
//        
//        System.out.println("-------------------------------------------------");
//        childCount = vdTree.getChildCount();
//        for (int i = 0; i < childCount; i++)
//        {
//            System.out.println(children.nextElement());
//        }
//        System.out.println("-------------------------------------------------");

        VDNode vdNode = SupportCalcs.getVDTree2(dob.getTime(), testData[0], profile);

        System.out.println("-------------------------------------------------");
        System.out.println("Root : " + vdNode);
        vdNode.getChildren().stream().forEach((child) ->
        {
            System.out.println(child);
//            child.getChildren().stream().forEach(bukthiChild ->
//            {
//                System.out.println("\t" + bukthiChild);
//            });
        });
        
        vdNode = SupportCalcs.getVDTree2(dob.getTime(), testData[0], profile);

        System.out.println("-------------------------------------------------");
        System.out.println("Root : " + vdNode);
        vdNode.getChildren().stream().forEach((child) ->
        {
            System.out.println(child);
//            child.getChildren().stream().forEach(bukthiChild ->
//            {
//                System.out.println("\t" + bukthiChild);
//            });
        });
        
    }
}
