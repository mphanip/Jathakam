/* 
 * Yet to decide on the license
 */
package mpp.jathakam.services.calculations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import static java.util.Comparator.comparing;

import mpp.jathakam.services.Constants;
import mpp.jathakam.services.JathakamuException;
import mpp.jathakam.services.Profile;
import mpp.jathakam.services.types.LordNode;
import mpp.jathakam.services.types.Place;
import mpp.jathakam.services.types.Planet;
import mpp.jathakam.services.types.Raasi;
import mpp.jathakam.services.types.Range;
import mpp.jathakam.services.VDNode;
import mpp.jathakam.services.util.Utils;
import swisseph.SweDate;
import swisseph.SwissLib;

import static mpp.jathakam.services.JathakamLogger.LOGGER;


/**
 * Calculation in this class will support EphemerisCalcs.
 *
 * All are static methods
 *
 * @author Phani
 */
public final class SupportCalcs
{
    private final static SwissLib swephLib = new SwissLib();
    
    private final static Map<Range, List<LordNode>> LORDS_TABLE = new HashMap<>();
    private final static Map<Integer, List<Planet>> HORARY_249_TABLE = new HashMap<>();
    private final static Map<Integer, List<Planet>> HORARY_2193_TABLE = new HashMap<>();
    private final static Map<Integer, Range> HORARY_249_RANGE_TABLE = new HashMap<>();
    private final static Map<Integer, Range> HORARY_2193_RANGE_TABLE = new HashMap<>();
    
    // beginFor249 and endFor249 are used for populating horary tables 
    private static double beginFor249 = 0;
    private static double endFor249 = 0;
    private static int horary249Index = 0;
    
    private static double beginFor2193 = 0;
    private static double endFor2193 = 0;
    private static int horary2193Index = 0;
    
    static
    {
        LOGGER.log(Level.INFO, "Initializing Lords Table...");
        initializeLordsTable2();
        LOGGER.log(Level.INFO, "Initialized Lords Table !!");
        
        saveToFile();
    }


    /**
     * Normalize the degree.
     *
     * @param longitude input can be any double value
     *
     * @return normalized degree (0.0 to 360.0)
     */
    public static double degnorm(double longitude)
    {
        return swephLib.swe_degnorm(longitude);
    }

    /**
     * Convert given Geographic latitude to Geocentric latitude using the
     * following formula
     * 
     * tan (Geographic) = tan (Geocentric) x 0.99330546
     * 
     * Geocentric = arctan(tan(Geographic) / 0.99330546)
     * 
     * -----------------------------------------------------
     * 
     * Another formula:
     * Log tan (Geocentric) = Log Tan (Geographic) + 9.9970826
     *
     * @param geographicLatitude Geographic latitude
     * @return Geocentric latitude
     */
    public static double getGeocentricLatitude(double geographicLatitude)
    {
        double geolatInRad = Math.toRadians(geographicLatitude);
        double geocentricLatitude = Math.tan(geolatInRad);

        /*
         * For more precession, we can try Polar Radius / Equatorial Radius i.e
         * 6356.755/6378.140
         */
        geocentricLatitude = geocentricLatitude * 0.99330546D;

        geocentricLatitude = Math.atan(geocentricLatitude);

        geocentricLatitude = Math.toDegrees(geocentricLatitude);

        return geocentricLatitude;

//        The following does not look correct having some slight error above looks good.
//        double equatorialRadius = 6378; //6378.140;
//        double polarRadius = 6357; //6356.755;
//        double twoXgeographicLatitudeInRadians = StrictMath.toRadians(2*geographicLatitude);
//        double sinOf2xGeographicLatitude = StrictMath.sin(twoXgeographicLatitudeInRadians);
//        double ellipticity = (equatorialRadius-polarRadius)/equatorialRadius;
//        double reductionOfLatitude = ellipticity * sinOf2xGeographicLatitude;
//        
//        reductionOfLatitude = StrictMath.toDegrees(reductionOfLatitude);
//        
//        double geocentricLatitude = geographicLatitude - reductionOfLatitude;
//        
//        return geocentricLatitude;
    }

    /**
     * Following method is used for bith time rectification. This Formulae is
     * from a Telugu book SriKrishna Jaimini Sidhanthamu by Sri Sivvala
     * Subramanyam garu.
     *
     * @param lagna Ascendent or Lagna position
     * @return Moon position of the given Lagna
     */
    public static double getMoonPositionGivenLagna(double lagna)
    {
        // Lagna Position * 81/360
        double moonPosition = lagna * 81D / 360D;

        // Round-off moonPostion
        long iMoonPos = Double.valueOf(moonPosition).longValue();
        moonPosition = moonPosition - iMoonPos;

        return moonPosition * 360D;
    }
    
    public static double getJulianDay(long dateTimeInMillis, String timeZone, double longitude)
            throws JathakamuException
    {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        calendar.setTimeInMillis(dateTimeInMillis);
        double jd = getJulianDay(calendar, timeZone, longitude);
        
        return jd;
    }
    
    public static double getJulianDay(long dateTimeInMillis, Place place)
            throws JathakamuException
    {
        String timeZone = place.getTimeZone();
        double longitude = place.getLongitude();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        calendar.setTimeInMillis(dateTimeInMillis);
        double jd = getJulianDay(calendar, timeZone, longitude);
        
        return jd;
    }
    
    public static double getJulianDay(Date date, String timeZone, double longitude)
            throws JathakamuException
    {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        calendar.setTime(date);
        double jd = getJulianDay(calendar, timeZone, longitude);
        
        return jd;
    }
    
    public static double getJulianDay(Date date, Place place)
            throws JathakamuException
    {
        String timeZone = place.getTimeZone();
        double longitude = place.getLongitude();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        calendar.setTime(date);
        double jd = getJulianDay(calendar, timeZone, longitude);
        
        return jd;
    }
    
    public static double getJulianDay(Calendar calendar, Place place)
            throws JathakamuException
    {
        String timeZone = place.getTimeZone();
        double longitude = place.getLongitude();
        double jd = getJulianDay(calendar, timeZone, longitude);
        
        return jd;
    }
    
    public static double getJulianDay(Calendar calendar, String timeZone, double longitude)
        throws JathakamuException
    {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        double seconds = calendar.get(Calendar.SECOND) + calendar.get(Calendar.MILLISECOND)/1000D;
        double jd = getJulianDay(year, month, dayOfMonth, hours, minutes, seconds, timeZone,
                longitude);
        
        return jd;
    }
    
    public static double getJulianDay(int year, int month, int dayOfMonth,
            int hours, int minutes, double seconds, String timeZone,
            double longitude)
        throws JathakamuException
    {
        ZoneId zoneId = null;
        try
        {
            zoneId = ZoneId.of(timeZone);
        }
        catch (Exception e)
        {
            LOGGER.log(Level.WARNING,
                    "Invalid Time Zone " + timeZone + " provided.", e);
            throw new JathakamuException("Invalid Time Zone provided");
        }

        int nSeconds = Double.valueOf(seconds).intValue();
        int nanoSeconds = Double.valueOf((seconds - nSeconds) * 1000000000D).intValue();

        ZonedDateTime zdt = ZonedDateTime.of(year, month, dayOfMonth, hours,
                minutes, nSeconds, nanoSeconds, zoneId);
        LOGGER.log(Level.INFO, "zdt: {0}", zdt.toString());
        
//        long jd = zdt.getLong(JulianFields.JULIAN_DAY);
//        LOGGER.log(Level.INFO, "Given date is {0} and Julian Day {1}",
//                new Object[] {zdt.toString(), jd });
        
        // get local mean time and appropriate adjust the give time.
        int stdLogitudeOffset = zdt.getOffset().getTotalSeconds();
//        int givenLogitudeOffset = Double.valueOf((longitude * 4D) * 60D).intValue();
//        int diffOffset = (givenLogitudeOffset - stdLogitudeOffset);
//        LOGGER.log(Level.INFO,
//                "stdLogitudeOffset = {0}, givenLogitudeOffset = {1}, diffOffset: {2}",
//                new Object[]
//                {
//                    String.valueOf(stdLogitudeOffset),
//                    String.valueOf(givenLogitudeOffset),
//                    String.valueOf(diffOffset)
//                });

        if (longitude > 0)
        {
            zdt = zdt.minusSeconds(stdLogitudeOffset);
        }
        else
        {
            zdt = zdt.plusSeconds(stdLogitudeOffset);
        }
        
        year = zdt.getYear();
        month = zdt.getMonthValue();
        dayOfMonth = zdt.getDayOfMonth();
        double dHours = zdt.getHour() + zdt.getMinute()/60D
                + zdt.getSecond()/3600D + zdt.getNano()/(3600D * 1000000000D);
        LOGGER.log(Level.INFO, "Date and Time (UT): {0}/{1}/{2,number,####}T{3}",new Object[] {dayOfMonth, month, year, dHours});
        
        SweDate sweDate = new SweDate(year, month, dayOfMonth, dHours, true);
        boolean flag = sweDate.checkDate();
        
        if (!flag)
        {
            throw new JathakamuException("Date verification failed");
        }
        LOGGER.log(Level.INFO, "SweDate.toString: {0}", sweDate.toString());

        double jd_ut = sweDate.getJulDay();

        return jd_ut;
    }
    
    public static boolean isDifferentZodiacSign(double begin, double end)
    {
        if (end - begin > 30.0)
        {
            return true;
        }
        
        boolean rtnValue;
        Raasi beginRaasi = Raasi.getRaasi(begin);
        Range beginRaasiExtent = beginRaasi.getExtent();
        
        rtnValue = beginRaasiExtent.getMax().doubleValue() < end;
        
        return rtnValue;
    }
    
        /**
     * This method should be called only after calling isLordTableReady. If 
     * isLordTableReady returns true then only call this method. otherwise it
     * may return null
     * 
     * @param horaryNum Number between 1 to 249
     * @return List of lords
     */
    public static List<Planet> getPlanetsFor249Horary(int horaryNum)
    {
        List<Planet> planetList = HORARY_249_TABLE.get(horaryNum);
        
        return planetList;
    }
    
    public static double getAscendent(int horaryNum)
    {
        Range range = HORARY_249_RANGE_TABLE.get(horaryNum);
        return range.getMin().doubleValue();
    }
    
    public static double getAscendent2193(int horaryNum)
    {
        Range range = HORARY_2193_RANGE_TABLE.get(horaryNum);
        return range.getMin().doubleValue();
    }
    
    /**
     * This method should be called only after calling isLordTableReady. If 
     * isLordTableReady returns true then only call this method. otherwise it
     * may return null
     * 
     * @param horaryNum number between 1 to 249 (both inclusive)
     * @return  beginning longitude of the horary number
     */
    public static Range getRangeFor249Horary(int horaryNum)
    {
        Range range = HORARY_249_RANGE_TABLE.get(horaryNum);
        
        return range;
    }
    
    /**
     * This method should be called only after calling isLordTableReady. If 
     * isLordTableReady returns true then only call this method. otherwise it
     * may return null
     * 
     * @param horaryNum Horary number between 1 to 2193 (both inclusive)
     * @return List of lords
     */
    public static List<Planet> getPlanetsFor2193Horary(int horaryNum)
    {
        List<Planet> planetList = HORARY_2193_TABLE.get(horaryNum);
        
        return planetList;
    }
    
    /**
     * This method should be called only after calling isLordTableReady. If 
     * isLordTableReady returns true then only call this method. otherwise it
     * may return null
     * 
     * @param horaryNum Horary number between 1 to 2193 (both inclusive)
     * @return beginning longitude for the horary number
     */
    public static Range getRangeFor2193Horary(int horaryNum)
    {
        Range range = HORARY_2193_RANGE_TABLE.get(horaryNum);
        
        return range;
    }
    
    public static List<LordNode> getLords(double planetLongitude)
    {
        Optional<Range> key = LORDS_TABLE.keySet().stream()
                .filter(r -> r.inRange((planetLongitude == 360.0) ? 0.0 : planetLongitude))
                .findFirst();
        
        if (!key.isPresent())
        {
            return new ArrayList<>();
        }
        
        List<LordNode> entry = LORDS_TABLE.get(key.get());
        return entry;
    }
    
    public static List<Planet> getLordsWithSignLord(double planetLongitude)
    {
        List<LordNode> entry = getLords(planetLongitude);
        
        Raasi raasi = Raasi.getRaasi(planetLongitude);
        Planet signLord = Planet.getSignLord(raasi);
        
        List<Planet> rtnList = entry.stream()
                .map((LordNode t) -> t.getPlanet())
                .collect(Collectors.toList());
        
        rtnList.add(0, signLord);

        return rtnList;
    }
    
    /**
     * 
     * @param dob date of birth or time for which Vimshottari dasa tree is needed
     * @param planetLongitude   longitude of planet
     * @param profile   profile to get profile settings to use while calculating.
     * 
     * @return  Root TreeNode of Vimshottari dasa
     */
    public static TreeNode getVDTree(long dob, double planetLongitude, Profile profile)
    {
        long t1 = System.currentTimeMillis();
        List<LordNode> entry = getLords(planetLongitude);
        LordNode[] startingDasaPlanets = new LordNode[5];
        double[] remainDasaFraction = new double[5];
        
        entry.toArray(startingDasaPlanets);
        
        int i = 0;
        for (LordNode node: startingDasaPlanets)
        {
            remainDasaFraction[i++] = node.getRemainingDurationForLongitude(planetLongitude);
        }
        
        long[] dasaEndDate = {dob, dob, dob, dob, dob}; 
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Vimshottari Dasa", true);
        Planet startingMahaDasaPlanet = startingDasaPlanets[0].getPlanet();
        Planet startingBhuktiPlanet = startingDasaPlanets[1].getPlanet();
        Planet startingPratyantaraPlanet = startingDasaPlanets[2].getPlanet();
        Planet startingSookshmaPlanet = startingDasaPlanets[3].getPlanet();
        Planet startingPranaPlanet = startingDasaPlanets[4].getPlanet();
         
        for (Planet mahaDasaPlanet: Planet.getVDPlanetArray(startingMahaDasaPlanet))
        {
            double mdDuration = mahaDasaPlanet.getDasaDuration() * remainDasaFraction[0]; // Required for 1st iteration only
            DefaultMutableTreeNode mahaDasaNode = addNode(mahaDasaPlanet, root, true, mdDuration, dasaEndDate, 0, profile);
            remainDasaFraction[0] = 1;
            mdDuration = mahaDasaPlanet.getDasaDuration(); // Required for use by bukti loop below
            
            for (Planet bp : Planet.getVDPlanetArray((startingBhuktiPlanet == null)? mahaDasaPlanet : startingBhuktiPlanet ))
            {
                double bdDuration = mdDuration * bp.getDasaDuration()/120D * remainDasaFraction[1];
                remainDasaFraction[1] = 1;
                DefaultMutableTreeNode bukthiDasaNode = addNode(bp, mahaDasaNode, true, bdDuration, dasaEndDate, 1, profile);
                bdDuration = mdDuration * bp.getDasaDuration()/120D;
                
                for (Planet pap : Planet.getVDPlanetArray((startingPratyantaraPlanet == null) ? bp : startingPratyantaraPlanet))
                {
                    double paDuration = bdDuration * pap.getDasaDuration()/120D * remainDasaFraction[2];
                    remainDasaFraction[2] = 1;
                    DefaultMutableTreeNode pratyantaraDasaNode = addNode(pap, bukthiDasaNode, true, paDuration, dasaEndDate, 2, profile);
                    paDuration = bdDuration * pap.getDasaDuration()/120D;
                    
                    for (Planet sp : Planet.getVDPlanetArray((startingSookshmaPlanet == null) ? pap : startingSookshmaPlanet))
                    {
                        double sdDuration = paDuration * sp.getDasaDuration()/120D * remainDasaFraction[3];
                        remainDasaFraction[3] = 1;
                        DefaultMutableTreeNode sookshmaDasaNode = addNode(sp, pratyantaraDasaNode, true, sdDuration, dasaEndDate, 3, profile);
                        sdDuration = paDuration * sp.getDasaDuration()/120D;
                        
                        for (Planet pp : Planet.getVDPlanetArray((startingPranaPlanet == null) ? sp : startingPranaPlanet))
                        {
                            double pdDuration = sdDuration * pp.getDasaDuration()/120D * remainDasaFraction[4];
                            remainDasaFraction[4] = 1;
                            addNode(pp, sookshmaDasaNode, false, pdDuration, dasaEndDate, 4, profile);
                        }
                        startingPranaPlanet = null;
                    }
                    startingSookshmaPlanet = null;
                }
                startingPratyantaraPlanet = null;
            }
            startingBhuktiPlanet = null;
        }
        
        long t2 = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "Time taken to generate vimshottari dasa is {0}msecs", (t2-t1));
        
        return root;
    }
    
        /**
     * 
     * @param dob date of birth or time for which Vimshottari dasa tree is needed
     * @param planetLongitude   longitude of planet
     * @param profile   profile to get profile settings to use while calculating.
     * 
     * @return  Root TreeNode of Vimshottari dasa
     */
    public static VDNode getVDTree2(long dob, double planetLongitude, Profile profile)
    {
        long t1 = System.currentTimeMillis();
        List<LordNode> entry = getLords(planetLongitude);
        LordNode[] startingDasaPlanets = new LordNode[5];
        double[] remainDasaFraction = new double[5];
        
        entry.toArray(startingDasaPlanets);
        
        int i = 0;
        for (LordNode node: startingDasaPlanets)
        {
            System.out.println(node);
            remainDasaFraction[i++] = node.getRemainingDurationForLongitude(planetLongitude);
        }
        
        long[] dasaEndDate =  new long[5]; 
        Arrays.fill(dasaEndDate, dob);
        VDNode root = VDNode.getRootNode();
        Planet startingMahaDasaPlanet = startingDasaPlanets[0].getPlanet();
        Planet startingBhuktiPlanet = startingDasaPlanets[1].getPlanet();
        Planet startingPratyantaraPlanet = startingDasaPlanets[2].getPlanet();
        Planet startingSookshmaPlanet = startingDasaPlanets[3].getPlanet();
        Planet startingPranaPlanet = startingDasaPlanets[4].getPlanet();
        
        List<Planet> mahaDasaPlanets = Planet.getVDPlanets(startingMahaDasaPlanet, startingMahaDasaPlanet);
        
        for (Planet mahaDasaPlanet: mahaDasaPlanets)
        {
            double mdDuration = mahaDasaPlanet.getDasaDuration() * remainDasaFraction[0]; // Required for 1st iteration only
            VDNode mahaDasaNode = addNode(mahaDasaPlanet, root, mdDuration, dasaEndDate, 0, profile);
            remainDasaFraction[0] = 1;
            mdDuration = mahaDasaPlanet.getDasaDuration(); // Required for use by bukti loop below
            List<Planet> bhuktiPlanets = Planet.getVDPlanets(mahaDasaPlanet, (startingBhuktiPlanet == null)? mahaDasaPlanet : startingBhuktiPlanet );
            
            for (Planet bp : bhuktiPlanets)
            {
                double bdDuration = mdDuration * bp.getDasaDuration()/120D * remainDasaFraction[1];
                remainDasaFraction[1] = 1;
                VDNode bukthiDasaNode = addNode(bp, mahaDasaNode, bdDuration, dasaEndDate, 1, profile);
                bdDuration = mdDuration * bp.getDasaDuration()/120D;
                List<Planet> pratyantaraPlanets = Planet.getVDPlanets(bp, (startingPratyantaraPlanet == null) ? bp : startingPratyantaraPlanet);
                
                for (Planet pap : pratyantaraPlanets)
                {
                    double paDuration = bdDuration * pap.getDasaDuration()/120D * remainDasaFraction[2];
                    remainDasaFraction[2] = 1;
                    VDNode pratyantaraDasaNode = addNode(pap, bukthiDasaNode, paDuration, dasaEndDate, 2, profile);
                    paDuration = bdDuration * pap.getDasaDuration()/120D;
                    List<Planet> sookshmaPlanets = Planet.getVDPlanets(pap, (startingSookshmaPlanet == null) ? pap : startingSookshmaPlanet);
                    
                    for (Planet sp : sookshmaPlanets)
                    {
                        double sdDuration = paDuration * sp.getDasaDuration()/120D * remainDasaFraction[3];
                        remainDasaFraction[3] = 1;
                        VDNode sookshmaDasaNode = addNode(sp, pratyantaraDasaNode, sdDuration, dasaEndDate, 3, profile);
                        sdDuration = paDuration * sp.getDasaDuration()/120D;
                        List<Planet> pranaPlanets = Planet.getVDPlanets(sp, (startingPranaPlanet == null) ? sp : startingPranaPlanet);
                        
                        for (Planet pp : pranaPlanets)
                        {
                            double pdDuration = sdDuration * pp.getDasaDuration()/120D * remainDasaFraction[4];
                            remainDasaFraction[4] = 1;
                            addNode(pp, sookshmaDasaNode, pdDuration, dasaEndDate, 4, profile);
                        }
                        startingPranaPlanet = null;
                    }
                    startingSookshmaPlanet = null;
                }
                startingPratyantaraPlanet = null;
            }
            startingBhuktiPlanet = null;
        }
        
        long t2 = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "Time taken to generate vimshottari dasa is {0}msecs", (t2-t1));
        
        return root;
    }
    
    /*
     * Using initializeLordsTable2 instead of initializeLordsTable
     */
    private static void initializeLordsTable()
    {
        long t1 = System.currentTimeMillis();
        /*
         * Note that most of the below logic is based on minutes.
         * For Example 800'=13Deg 20'
         */
        double dasaduration = 800D;
        double mahaDasaStart;
        double mahaDasaEnd = 0D;
        
        beginFor249 = 0;
        endFor249 = 0;
        horary249Index = 0;
        beginFor2193 = 0;
        endFor2193 = 0;
        horary2193Index = 0;
        
        for (int i = 0; i < 3; i++)
        {
            for (Planet mahaDasaPlanet : Planet.getVDPlanetArray(Planet.KETU))
            {
                mahaDasaStart = mahaDasaEnd;
                mahaDasaEnd += dasaduration;
                Range mahaDasaRange = new Range.Builder().values(mahaDasaStart/60D, mahaDasaEnd/60D).build();
                double mahaDasaTimeFraction = mahaDasaPlanet.getDasaDuration(); 
                LordNode mahaLordNode = new LordNode(mahaDasaPlanet, mahaDasaRange, mahaDasaTimeFraction);
                double buktiStart;
                double buktiEnd = mahaDasaStart;
                
                for (Planet bukthiPlanet : Planet.getVDPlanetArray(mahaDasaPlanet))
                {
                    buktiStart = buktiEnd;
                    buktiEnd += dasaduration * (bukthiPlanet.getDasaDuration() / 120D);
                    double buktiStartRange = Utils.normalize(buktiStart/60D);
                    double buktiEndRange = Utils.normalize(buktiEnd/60D);
                    Range buktiRange = new Range.Builder().values(buktiStartRange, buktiEndRange).build();
                    double buktiTimeFraction = mahaDasaTimeFraction * bukthiPlanet.getDasaDuration() / 120D;
                    LordNode buktiLordNode = new LordNode(bukthiPlanet, buktiRange, buktiTimeFraction);
                    double pratyantaraStart;
                    double pratyantaraEnd = buktiStart;
                    double buktiDuration = buktiEnd - buktiStart;
                    
                    // populate 249 Horary table
                    double duration = 800D;
                    duration *= bukthiPlanet.getDasaDuration();
                    duration = duration / (120D * 60D);
                    setHoraryTableEntry(mahaDasaPlanet, bukthiPlanet, null, duration);
                    
                    for (Planet pratyantaraPlanet : Planet.getVDPlanetArray(bukthiPlanet))
                    {
                        pratyantaraStart = pratyantaraEnd;
                        pratyantaraEnd += buktiDuration * (pratyantaraPlanet.getDasaDuration() / 120D);
                        double pratyantaraStartRange = Utils.normalize(pratyantaraStart/60D);
                        double pratyantaraEndRange = Utils.normalize(pratyantaraEnd/60D);
                        Range pratyantaraRange = new Range.Builder().values(pratyantaraStartRange, pratyantaraEndRange).build();
                        double pratyantaraTimeFraction = buktiTimeFraction * pratyantaraPlanet.getDasaDuration() / 120D;
                        LordNode pratyantaraLordNode = new LordNode(pratyantaraPlanet, pratyantaraRange, pratyantaraTimeFraction);
                        double sookshmaStart;
                        double sookshmaEnd = pratyantaraStart;
                        double pratyantaraDuration = pratyantaraEnd - pratyantaraStart;

                        // populate 2193 Horary table
                        duration = 800D;
                        duration *= bukthiPlanet.getDasaDuration();
                        duration *= pratyantaraPlanet.getDasaDuration();
                        duration = duration / (120D * 120D * 60D);
                        setHoraryTableEntry(mahaDasaPlanet, bukthiPlanet, pratyantaraPlanet, duration);
                        
                        for (Planet sookshmaPlanet : Planet.getVDPlanetArray(pratyantaraPlanet))
                        {
                            sookshmaStart = sookshmaEnd;
                            sookshmaEnd += pratyantaraDuration * (sookshmaPlanet.getDasaDuration() / 120D);
                            double sookshmaStartRange = Utils.normalize(sookshmaStart/60D);
                            double sookshmaEndRange = Utils.normalize(sookshmaEnd/60D);
                            Range sookshmaRange = new Range.Builder().values(sookshmaStartRange, sookshmaEndRange).build();
                            double sookshmaTimeFraction = pratyantaraTimeFraction * sookshmaPlanet.getDasaDuration() / 120D;
                            LordNode sookshmaLordNode = new LordNode(sookshmaPlanet,sookshmaRange, sookshmaTimeFraction);
                            double pranaStart;
                            double pranaEnd = sookshmaStart;
                            double sookshmaDuration = sookshmaEnd - sookshmaStart;
                            
                            for (Planet pranaPlanet : Planet.getVDPlanetArray(sookshmaPlanet))
                            {
                                pranaStart = pranaEnd;
                                pranaEnd += sookshmaDuration * (pranaPlanet.getDasaDuration() / 120D);
                                double pranaStartRange = Utils.normalize(pranaStart/60D);
                                double pranaEndRange = Utils.normalize(pranaEnd/60D);
                                Range pranaRange = new Range.Builder().values(pranaStartRange, pranaEndRange).build();
                                double pranaTimeFraction = sookshmaTimeFraction * pranaPlanet.getDasaDuration() / 120D;
                                LordNode pranaNode = new LordNode(pranaPlanet, pranaRange, pranaTimeFraction);
                                
                                List<LordNode> LordsList = new ArrayList<>();
                                LordsList.add(mahaLordNode);
                                LordsList.add(buktiLordNode);
                                LordsList.add(pratyantaraLordNode);
                                LordsList.add(sookshmaLordNode);
                                LordsList.add(pranaNode);
    
                                LORDS_TABLE.put(pranaRange, LordsList);
                            }
                        }
                    }
                }
            }
        }
        long t2 = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "Time taken to populate LORDS_TABLE is {0}msecs", (t2-t1));
    }
    
    private static void initializeLordsTable2()
    {
        long t1 = System.currentTimeMillis();
        
        beginFor249 = 0;
        endFor249 = 0;
        horary249Index = 0;
        beginFor2193 = 0;
        endFor2193 = 0;
        horary2193Index = 0;
        
        Arrays.asList(0d, 120d, 240d).forEach(entry ->
        {
            /*
             * Note that most of the below logic is based on minutes.
             * For Example 800'=13Deg 20'
             */
            double dasaduration = 800D;
            double mahaDasaStart;
            double mahaDasaEnd = entry;
        
            for (Planet mahaDasaPlanet : Planet.getVDPlanetArray(Planet.KETU))
            {
                mahaDasaStart = mahaDasaEnd;
                mahaDasaEnd += dasaduration;
                Range mahaDasaRange = new Range.Builder().values(mahaDasaStart/60D, mahaDasaEnd/60D).build();
                double mahaDasaTimeFraction = mahaDasaPlanet.getDasaDuration(); 
                LordNode mahaLordNode = new LordNode(mahaDasaPlanet, mahaDasaRange, mahaDasaTimeFraction);
                double buktiStart;
                double buktiEnd = mahaDasaStart;
                
                for (Planet bukthiPlanet : Planet.getVDPlanetArray(mahaDasaPlanet))
                {
                    buktiStart = buktiEnd;
                    buktiEnd += dasaduration * (bukthiPlanet.getDasaDuration() / 120D);
                    double buktiStartRange = Utils.normalize(buktiStart/60D);
                    double buktiEndRange = Utils.normalize(buktiEnd/60D);
                    Range buktiRange = new Range.Builder().values(buktiStartRange, buktiEndRange).build();
                    double buktiTimeFraction = mahaDasaTimeFraction * bukthiPlanet.getDasaDuration() / 120D;
                    LordNode buktiLordNode = new LordNode(bukthiPlanet, buktiRange, buktiTimeFraction);
                    double pratyantaraStart;
                    double pratyantaraEnd = buktiStart;
                    double buktiDuration = buktiEnd - buktiStart;
                    
                    // populate 249 Horary table
                    double duration = 800D;
                    duration *= bukthiPlanet.getDasaDuration();
                    duration = duration / (120D * 60D);
                    setHoraryTableEntry(mahaDasaPlanet, bukthiPlanet, null, duration);
                    
                    for (Planet pratyantaraPlanet : Planet.getVDPlanetArray(bukthiPlanet))
                    {
                        pratyantaraStart = pratyantaraEnd;
                        pratyantaraEnd += buktiDuration * (pratyantaraPlanet.getDasaDuration() / 120D);
                        double pratyantaraStartRange = Utils.normalize(pratyantaraStart/60D);
                        double pratyantaraEndRange = Utils.normalize(pratyantaraEnd/60D);
                        Range pratyantaraRange = new Range.Builder().values(pratyantaraStartRange, pratyantaraEndRange).build();
                        double pratyantaraTimeFraction = buktiTimeFraction * pratyantaraPlanet.getDasaDuration() / 120D;
                        LordNode pratyantaraLordNode = new LordNode(pratyantaraPlanet, pratyantaraRange, pratyantaraTimeFraction);
                        double sookshmaStart;
                        double sookshmaEnd = pratyantaraStart;
                        double pratyantaraDuration = pratyantaraEnd - pratyantaraStart;

                        // populate 2193 Horary table
                        duration = 800D;
                        duration *= bukthiPlanet.getDasaDuration();
                        duration *= pratyantaraPlanet.getDasaDuration();
                        duration = duration / (120D * 120D * 60D);
                        setHoraryTableEntry(mahaDasaPlanet, bukthiPlanet, pratyantaraPlanet, duration);
                        
                        for (Planet sookshmaPlanet : Planet.getVDPlanetArray(pratyantaraPlanet))
                        {
                            sookshmaStart = sookshmaEnd;
                            sookshmaEnd += pratyantaraDuration * (sookshmaPlanet.getDasaDuration() / 120D);
                            double sookshmaStartRange = Utils.normalize(sookshmaStart/60D);
                            double sookshmaEndRange = Utils.normalize(sookshmaEnd/60D);
                            Range sookshmaRange = new Range.Builder().values(sookshmaStartRange, sookshmaEndRange).build();
                            double sookshmaTimeFraction = pratyantaraTimeFraction * sookshmaPlanet.getDasaDuration() / 120D;
                            LordNode sookshmaLordNode = new LordNode(sookshmaPlanet,sookshmaRange, sookshmaTimeFraction);
                            double pranaStart;
                            double pranaEnd = sookshmaStart;
                            double sookshmaDuration = sookshmaEnd - sookshmaStart;
                            
                            for (Planet pranaPlanet : Planet.getVDPlanetArray(sookshmaPlanet))
                            {
                                pranaStart = pranaEnd;
                                pranaEnd += sookshmaDuration * (pranaPlanet.getDasaDuration() / 120D);
                                double pranaStartRange = Utils.normalize(pranaStart/60D);
                                double pranaEndRange = Utils.normalize(pranaEnd/60D);
                                Range pranaRange = new Range.Builder().values(pranaStartRange, pranaEndRange).build();
                                double pranaTimeFraction = sookshmaTimeFraction * pranaPlanet.getDasaDuration() / 120D;
                                LordNode pranaNode = new LordNode(pranaPlanet, pranaRange, pranaTimeFraction);
                                
                                List<LordNode> LordsList = new ArrayList<>();
                                LordsList.add(mahaLordNode);
                                LordsList.add(buktiLordNode);
                                LordsList.add(pratyantaraLordNode);
                                LordsList.add(sookshmaLordNode);
                                LordsList.add(pranaNode);
    
                                LORDS_TABLE.put(pranaRange, LordsList);
                            }
                        }
                    }
                }
            }
        });
        long t2 = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "Time taken to populate LORDS_TABLE is {0}msecs", (t2-t1));
    }
    
    private static void setHoraryTableEntry(Planet mahaDasaPlanet,
                                           Planet bukthiPlanet,
                                           Planet pratyantaraPlanet,
                                           double duration)
    {
        if (pratyantaraPlanet == null)
        {
            endFor249 += duration;
            endFor249 = Utils.normalize(endFor249);
        }
        else
        {
            endFor2193 += duration;
            endFor2193 = Utils.normalize(endFor2193);
        }
        
        if (pratyantaraPlanet == null)
        {
            addToHoraryTable(beginFor249, endFor249, mahaDasaPlanet, bukthiPlanet, pratyantaraPlanet);
        }
        else
        {
            addToHoraryTable(beginFor2193, endFor2193, mahaDasaPlanet, bukthiPlanet, pratyantaraPlanet);
        }
       
        if (pratyantaraPlanet == null)
        {
            beginFor249 = endFor249;
        }
        else
        {
            beginFor2193 = endFor2193;
        }
    }
    
    private static void addToHoraryTable(double begin, double end,
            Planet mahaDasaPlanet, Planet bukthiPlanet, Planet pratyantaraPlanet)
    {
        Map<Integer, List<Planet>> horaryTable = HORARY_2193_TABLE;
        boolean isHorary249 = false;
        
        if (pratyantaraPlanet == null)
        {
            horaryTable = HORARY_249_TABLE;
            isHorary249 = true;
        }

        // populate 249/2193 Horary table
        boolean signChangedFlag = isDifferentZodiacSign(begin, end);
        double newEnd = end;
        
        if (signChangedFlag)
        {
            int nEnd = (int) end;
            newEnd = (nEnd/30)*30;
        }
        
        List<Planet> horaryPlanetList = new ArrayList<>();
        Raasi planetSign = Raasi.getRaasi(begin);

        Planet signStar = Planet.getPlanetSign(planetSign);
        horaryPlanetList.add(signStar);
        horaryPlanetList.add(mahaDasaPlanet);
        horaryPlanetList.add(bukthiPlanet);

        if (pratyantaraPlanet != null)
        {
            horaryPlanetList.add(pratyantaraPlanet);
        }
        
        Range range = new Range.Builder().values(begin, newEnd).build();
        if (isHorary249)
        {
            horaryTable.put(++horary249Index, horaryPlanetList);
            HORARY_249_RANGE_TABLE.put(horary249Index, range);
        }
        else
        {
            horaryTable.put(++horary2193Index, horaryPlanetList);
            HORARY_2193_RANGE_TABLE.put(horary2193Index, range);
        }

        if (signChangedFlag)
        {
            planetSign = Raasi.getRaasi(newEnd);
            signStar = Planet.getPlanetSign(planetSign);
            List<Planet> newPlanetList = new ArrayList<>(horaryPlanetList);

            newPlanetList.set(0, signStar);
            range = new Range.Builder().values(newEnd, end).build();
            if (isHorary249)
            {
                horaryTable.put(++horary249Index, newPlanetList);
                HORARY_249_RANGE_TABLE.put(horary249Index, range);
            }
            else
            {
                horaryTable.put(++horary2193Index, newPlanetList);
                HORARY_2193_RANGE_TABLE.put(horary2193Index, range);
            }
        }
    }
    
    private static VDNode addNode(Planet dasaPlanet,
            VDNode parent, double dasaDuration, long[] dasaEndDate,
            int endDateIndex, Profile profile)
    {
        VDNode vdNode = new VDNode(dasaPlanet, 0L);
        double oneYr = profile.getProfileSetting().getOneYear();
        long inc = Double.valueOf(dasaDuration * oneYr * 24D * Constants.HOURS_IN_MILLS_D).longValue();

        dasaEndDate[endDateIndex] += inc;
        vdNode.setDasaEndPeriod(dasaEndDate[endDateIndex]);
        parent.add(vdNode);

        return vdNode;
    }

    private static DefaultMutableTreeNode addNode(Planet dasaPlanet,
            DefaultMutableTreeNode parent, boolean allowChildren,
            double dasaDuration, long[] dasaEndDate, int endDateIndex,
            Profile profile)
    {
        VDNode vdNode = new VDNode(dasaPlanet, 0L);
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(vdNode,
                allowChildren);
        parent.add(newNode);
        
        double oneYr = profile.getProfileSetting().getOneYear();
        long inc = Double.valueOf(dasaDuration * oneYr * 24D * Constants.HOURS_IN_MILLS_D).longValue();

        dasaEndDate[endDateIndex] += inc;

        vdNode.setDasaEndPeriod(dasaEndDate[endDateIndex]);

        return newNode;
    }
    
    // Debug methods
    private static void saveToFile()
    {
        if (!LOGGER.isLoggable(Level.FINEST))
        {
            return;
        }

        LOGGER.log(Level.INFO, "Storing Lords information...");

        String fileName = System.getProperty("user.home", ".") + File.separator + "LORDS_TABLE.txt";
        String horaray249FileName = System.getProperty("user.home", ".") + File.separator + "Horary249.txt";
        String horaray2193FileName = System.getProperty("user.home", ".") + File.separator + "Horary2193.txt";
        
        try (
                BufferedWriter bw249 = new BufferedWriter(new FileWriter(horaray249FileName));
        )
        {
            for (Entry<Integer, List<Planet>> entry : HORARY_249_TABLE.entrySet())
            {
                Range range = HORARY_249_RANGE_TABLE.get(entry.getKey());
                String str = entry.getKey() + "\t"+ range + " = "+ entry.getValue();
                bw249.write(str);
                bw249.newLine();
                bw249.flush();
                LOGGER.log(Level.FINEST, str);
            }
        }
        catch (IOException ex)
        {
            LOGGER.log(Level.SEVERE, "Failed to write " + horaray249FileName, ex);
        }
        
        try (
            BufferedWriter bw2193 = new BufferedWriter(new FileWriter(horaray2193FileName));
        )
        {    
            for (Entry<Integer, List<Planet>> entry : HORARY_2193_TABLE.entrySet())
            {
                Range range = HORARY_2193_RANGE_TABLE.get(entry.getKey());
                String str = entry.getKey() + "\t" + range + " = "+ entry.getValue();
                bw2193.write(str);
                bw2193.newLine();
                bw2193.flush();
                LOGGER.log(Level.FINEST,str);
            }
        }
        catch (IOException ex)
        {
            LOGGER.log(Level.SEVERE, "Failed to write " + horaray2193FileName, ex);
        }
        
        try (
                FileWriter fw = new FileWriter(fileName);
                BufferedWriter writer = new BufferedWriter(fw);
        )
        {            
            Function<Entry<Range, List<LordNode>>, Double> keyComparator;
            keyComparator = (r -> r.getKey().getMin().doubleValue());
            Comparator<Entry<Range, List<LordNode>>> comp = comparing(keyComparator);
            
            LORDS_TABLE.entrySet().stream()
                    .sorted(comp)
                    .forEach(entry ->
            {
                try
                {
                    writer.write(entry.getKey().toString());
                    writer.write(" = ");
                    List<LordNode> value = entry.getValue();
                    for (LordNode ln : value)
                    {
                        writer.write(ln.getPlanet().getShortName() + "-");
                    }
                    writer.newLine();
                }
                catch (IOException e)
                {
                    LOGGER.log(Level.WARNING, e.toString());
                }
            });
        }
        catch (IOException ex)
        {
            LOGGER.log(Level.SEVERE, "Failed to write LORDS_TABLE", ex);
        }

        LOGGER.log(Level.INFO, "Stored Lords information");
    }
}
