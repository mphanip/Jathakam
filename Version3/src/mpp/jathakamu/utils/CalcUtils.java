/*
 *
 *
 */
package mpp.jathakamu.utils;

import static mpp.jathakamu.JathakamLogger.LOGGER;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import mpp.jathakamu.types.LordNode;
import mpp.jathakamu.types.Planet;
import mpp.jathakamu.types.Raasi;
import mpp.jathakamu.types.Range;
import mpp.jathakamu.types.VDNode;
import mpp.jathakamu.view.ViewUtils;
import swisseph.SwissLib;


/**
 *
 * @author phani
 */
public final class CalcUtils
{
    public final static double D_HOURS_IN_MILLS = 60D * 60D * 1000D;
    public final static long L_HOURS_IN_MILLS = 60 * 60 * 1000;
    public final static long L_MINS_IN_MILLIS = 60 * 1000;
    private final static SwissLib swephLib = new SwissLib();
    private final static NavigableMap<Range, List<LordNode>> LORDS_TABLE =
            new ConcurrentSkipListMap<Range, List<LordNode>>();
    private final static NavigableMap<Integer, List<Planet>> HORARY_249_TABLE = new ConcurrentSkipListMap<Integer, List<Planet>>();
    private final static NavigableMap<Integer, List<Planet>> HORARY_2193_TABLE = new ConcurrentSkipListMap<Integer, List<Planet>>();
    
    private final static NavigableMap<Integer, Range> HORARY_249_RANGE_TABLE = new ConcurrentSkipListMap<Integer, Range>();
    private final static NavigableMap<Integer, Range> HORARY_2193_RANGE_TABLE = new ConcurrentSkipListMap<Integer, Range>();
    
    private final static ReentrantLock readyLock = new ReentrantLock();
    
    // beginFor249 and endFor249 are used for populating horary tables 
    private static double beginFor249 = 0;
    private static double endFor249 = 0;
    private static int horary249Index = 0;
    
    private static double beginFor2193 = 0;
    private static double endFor2193 = 0;
    private static int horary2193Index = 0;

    /**
     * Normalize the degree.
     * 
     * @param longitude
     * @return normalized degree (0 to 360)
     */
    public static double degnorm(double longitude)
    {
        return swephLib.swe_degnorm(longitude);
    }

    public static double getGeocentricLatitude(double geolat)
    {
        double geolatInRad = Math.toRadians(geolat);
        double t1 = Math.tan(geolatInRad);

        /*
         * For more precession, we can try Polar Radius / Equatorial Radius i.e
         * 6356.755/6378.140
         */
        t1 = t1 * 0.99330546D;

        t1 = Math.atan(t1);

        t1 = Math.toDegrees(t1);

        return t1;
    }

    /**
     * Following method is used for bith time rectification. This Formulae is
     * from a Telugu book SriKrishna Jaimini Sidhanthamu by Sri Sivvala
     * Subramanyam garu.
     * 
     * @param ascPosition
     * @return
     */
    public static double getMoonPositionGivenAsc(double ascPosition)
    {
    	// Lagna Position * 81/360
    	double moonPosition = ascPosition * 81D / 360D;
    	
    	// Round-off moonPostion
    	long iMoonPos = Double.valueOf(moonPosition).longValue();
    	moonPosition = moonPosition - iMoonPos;
    	
    	return moonPosition * 360D;
    }
    
    public static void initializeLordsTable()
    {
        Thread th = new Thread(new Runnable() {
            
            @Override
            public void run()
            {
                if (LORDS_TABLE.size() > 0)
                {
                    LOGGER.log(Level.INFO, "LORDS_TABLE already populated");
                    return;
                }

                long t1 = System.currentTimeMillis();
                _initializeLordsTable();
                long t2 = System.currentTimeMillis();

                LOGGER.log(Level.INFO, "Time Taken to construct Lords Table: "
                        + (t2-t1) + " millisec");
                
                if (LOGGER.isLoggable(Level.FINEST))
                {
                    File horaray249File = new File("Horary249.txt");
                    File horaray2193File = new File("Horary2193.txt");
                    
                    try
                    {
                        BufferedWriter bw249 = new BufferedWriter(new FileWriter(horaray249File));
                        BufferedWriter bw2193 = new BufferedWriter(new FileWriter(horaray2193File));
                        
                        LOGGER.log(Level.FINEST, "Lords Table (Longitude and Lords");
                        for (Entry<Integer, List<Planet>> entry : HORARY_249_TABLE.entrySet())
                        {
                            Range range = HORARY_249_RANGE_TABLE.get(entry.getKey());
                            String str = entry.getKey() + "\t"+ range.toStringForRassi() + " = "+ entry.getValue();
                            bw249.write(str);
                            bw249.newLine();
                            bw249.flush();
                            LOGGER.log(Level.FINEST, str);
                        }
                        bw249.close();
                        LOGGER.log(Level.FINEST, "----------------------------------------------------");
                        
                        LOGGER.log(Level.FINEST, "Horary 249 Table");
                        for (Entry<Integer, List<Planet>> entry : HORARY_2193_TABLE.entrySet())
                        {
                            Range range = HORARY_2193_RANGE_TABLE.get(entry.getKey());
                            String str = entry.getKey() + "\t" + range.toStringForRassi() + " = "+ entry.getValue();
                            bw2193.write(str);
                            bw2193.newLine();
                            bw2193.flush();
                            LOGGER.log(Level.FINEST,str);
                        }
                        LOGGER.log(Level.FINEST, "----------------------------------------------------");
                        
                        LOGGER.log(Level.FINEST, "Horary 2193 Table");
                        for (Entry<Integer, List<Planet>> entry : HORARY_2193_TABLE.entrySet())
                        {
                            LOGGER.log(Level.FINEST, entry.getKey()+ " = "+ entry.getValue());
                        }
                        bw2193.close();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        
        // intensional made it run, will use start later.
        th.run();
    }
    
    private static void _initializeLordsTable()
    {
        readyLock.lock();
        try
        {
        List<Planet> vdPlanets = Planet.getVimshottariDasaPlanets();
        CircularList<Planet> planetList = new CircularList<Planet>(vdPlanets, true, 0);
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
            for (Planet mahaDasaPlanet : Planet.getVimshottariDasaPlanets())
            {
                mahaDasaStart = mahaDasaEnd;
                mahaDasaEnd += dasaduration;
                Range mahaDasaRange = new Range(mahaDasaStart/60D, mahaDasaEnd/60D);
                double mahaDasaTimeFraction = mahaDasaPlanet.getDasaDuration(); 
                LordNode mahaLordNode = new LordNode(mahaDasaPlanet, mahaDasaRange, mahaDasaTimeFraction);
                Iterable<Planet> bukthi = planetList.iterable(mahaDasaPlanet.ordinal());
                double buktiStart;
                double buktiEnd = mahaDasaStart;

                for (Planet bukthiPlanet : bukthi)
                {
                    buktiStart = buktiEnd;
                    buktiEnd += dasaduration * (bukthiPlanet.getDasaDuration() / 120D);
                    Range buktiRange = new Range(buktiStart/60D, buktiEnd/60D);
                    double buktiTimeFraction = mahaDasaTimeFraction * bukthiPlanet.getDasaDuration() / 120D;
                    LordNode buktiLordNode = new LordNode(bukthiPlanet, buktiRange, buktiTimeFraction);

                    double pratyantaraStart;
                    double pratyantaraEnd = buktiStart;
                    double buktiDuration = buktiEnd - buktiStart;
                    Iterable<Planet> pratyantara = planetList.iterable(bukthiPlanet.ordinal());
                    
                    // populate 249 Horary table
                    double duration = 800D;
                    duration *= bukthiPlanet.getDasaDuration();

                    duration = duration / (120D * 60D);
                    setHoraryTableEntry(mahaDasaPlanet, bukthiPlanet, null, duration);

                    for (Planet pratyantaraPlanet : pratyantara)
                    {
                        pratyantaraStart = pratyantaraEnd;
                        pratyantaraEnd += buktiDuration * (pratyantaraPlanet.getDasaDuration() / 120D);
                        Range pratyantaraRange = new Range(pratyantaraStart/60D, pratyantaraEnd/60D);
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
                        
                        Iterable<Planet> sookshma = planetList.iterable(pratyantaraPlanet.ordinal());

                        for (Planet sookshmaPlanet : sookshma)
                        {
                            sookshmaStart = sookshmaEnd;
                            sookshmaEnd += pratyantaraDuration * (sookshmaPlanet.getDasaDuration() / 120D);
                            Range sookshmaRange = new Range(sookshmaStart/60D, sookshmaEnd/60D);
                            double sookshmaTimeFraction = pratyantaraTimeFraction * sookshmaPlanet.getDasaDuration() / 120D;
                            LordNode sookshmaLordNode = new LordNode(sookshmaPlanet,sookshmaRange, sookshmaTimeFraction);
                            
                            double pranaStart;
                            double pranaEnd = sookshmaStart;
                            double sookshmaDuration = sookshmaEnd - sookshmaStart;
                            Iterable<Planet> prana = planetList.iterable(sookshmaPlanet.ordinal());
                            
                            for (Planet pranaPlanet : prana)
                            {
                                pranaStart = pranaEnd;
                                pranaEnd += sookshmaDuration * (pranaPlanet.getDasaDuration() / 120D);
                                Range pranaRange = new Range(pranaStart/60D, pranaEnd/60D);
                                double pranaTimeFraction = sookshmaTimeFraction * pranaPlanet.getDasaDuration() / 120D;
                                LordNode pranaNode = new LordNode(pranaPlanet, pranaRange, pranaTimeFraction);
                                
                                List<LordNode> LordsList = new ArrayList<LordNode>();
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
        }
        finally
        {
            readyLock.unlock();
        }

    }

    public static boolean isLordTableReady()
    {
        boolean flag = readyLock.isLocked();
        
        return !flag;
    }

    private static void setHoraryTableEntry(Planet mahaDasaPlanet,
                                           Planet bukthiPlanet,
                                           Planet pratyantaraPlanet,
                                           double duration)
    {
        if (pratyantaraPlanet == null)        {
            endFor249 += duration;
        }
        else
        {
            endFor2193 += duration;
        }
        
        if (pratyantaraPlanet == null)
        {
            addToHoraryTable(beginFor249, endFor249, duration, mahaDasaPlanet, bukthiPlanet, pratyantaraPlanet);
        }
        else
        {
            addToHoraryTable(beginFor2193, endFor2193, duration, mahaDasaPlanet, bukthiPlanet, pratyantaraPlanet);
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
            double duration, Planet mahaDasaPlanet, Planet bukthiPlanet,
            Planet pratyantaraPlanet)
    {
        NavigableMap<Integer, List<Planet>> horaryTable = HORARY_2193_TABLE;

        if (pratyantaraPlanet == null)
        {
            horaryTable = HORARY_249_TABLE;
        }

        // populate 249/2193 Horary table

        
        boolean signChangedFlag = isSignChanged(begin, end);
        double newEnd = end;
        
        if (signChangedFlag)
        {
            int nEnd = (int) end;
            newEnd = (nEnd/30)*30;
        }
        
        List<Planet> horaryPlanetList = new ArrayList<Planet>();
        Raasi planetSign = Raasi.getRaasi(newEnd);

        Planet signStar = Planet.getPlanetSign(planetSign);
        horaryPlanetList.add(signStar);
        horaryPlanetList.add(mahaDasaPlanet);
        horaryPlanetList.add(bukthiPlanet);

        if (pratyantaraPlanet != null)
        {
            horaryPlanetList.add(pratyantaraPlanet);
        }
        
        if (pratyantaraPlanet == null)
        {
            horaryTable.put(++horary249Index, horaryPlanetList);
            HORARY_249_RANGE_TABLE.put(horary249Index, new Range(begin, newEnd));
        }
        else
        {
            horaryTable.put(++horary2193Index, horaryPlanetList);
            HORARY_2193_RANGE_TABLE.put(horary2193Index, new Range(begin, newEnd));
        }

        if (signChangedFlag)
        {
            planetSign = Raasi.getRaasi(end);
            signStar = Planet.getPlanetSign(planetSign);
            List<Planet> newPlanetList = new ArrayList<Planet>(horaryPlanetList);
            newPlanetList.set(0, signStar);
            if (pratyantaraPlanet == null)
            {
                horaryTable.put(++horary249Index, newPlanetList);
                HORARY_249_RANGE_TABLE.put(horary249Index, new Range(newEnd, end));
            }
            else
            {
                horaryTable.put(++horary2193Index, newPlanetList);
                HORARY_2193_RANGE_TABLE.put(horary2193Index, new Range(newEnd, end));
            }
        }
    }
    
    /**
     * 
     * @param start in degrees
     * @param end in degrees
     * @return  return true if zodiac sign (Raasi) is changing between start and end longitudes
     */
    public static boolean isSignChanged(double start, double end)
    {
        DegreeParts dpStart = getDegreeParts(start);
        DegreeParts dpEnd = getDegreeParts(end);
        
        boolean flag = (!dpStart.isRaasiBegining() && !dpEnd.isRaasiBegining()
                            && dpStart.degree > dpEnd.degree);

        return flag;
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
        return range.start();
    }
    
    public static double getAscendent2193(int horaryNum)
    {
        Range range = HORARY_2193_RANGE_TABLE.get(horaryNum);
        return range.start();
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
    
    public static DegreeParts getDegreeParts(double degree)
    {
        int nDeg = (int) degree;
        double degFracPart = degree - nDeg;
        nDeg = nDeg % 30;
        int lDegree = Double.valueOf(Math.round((nDeg + degFracPart) * CalcUtils.D_HOURS_IN_MILLS)).intValue();
        int deg = (int) (lDegree / L_HOURS_IN_MILLS);
        int min = (int) ((lDegree - (deg * L_HOURS_IN_MILLS)) / L_MINS_IN_MILLIS);
        double sec = (lDegree - (deg * L_HOURS_IN_MILLS) - (min * L_MINS_IN_MILLIS)) / 1000D;

        DegreeParts degParts = new DegreeParts(deg, min, sec);

        return degParts;
    }
    
    public static Map.Entry<Range, List<LordNode>> getDasaEntry(double planetLongitude)
    {
        Map.Entry<Range, List<LordNode>> entry = LORDS_TABLE.ceilingEntry(new Range(0D, planetLongitude));

        return entry;
    }
    
    public static long addDate(long inputDate, double incInYears)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(inputDate);
        
        // Convert to days
        double dDays = incInYears * 365.2425;
        int days = (int) dDays;
        double dHours = (dDays - days) * 1000D * 60D * 60D * 24D;
        
        // Convert the remaining time to MilliSeconds
        int milliSecs = Double.valueOf(dHours).intValue();

        cal.add(Calendar.DAY_OF_MONTH, days);
        cal.add(Calendar.MILLISECOND, milliSecs);
        
        long newDate = cal.getTimeInMillis();
        
        return newDate;
    }
    
    /**
     * 
     * @param dob date of birth or time for which Vimshottari dasa tree is needed
     * @param planetLongitude   longitude of planet
     * @return  Root TreeNode of Vimshottari dasa
     */
    public static TreeNode getVDTree(long dob, double planetLongitude)
    {
        System.out.println("Given Longitude = "
                + ViewUtils.toStringDegree(planetLongitude));
        Map.Entry<Range, List<LordNode>> entry = getDasaEntry(planetLongitude);
        LordNode[] startingDasaPlanets = new LordNode[5];
        double[] remainDasaFraction = new double[5];
        
        entry.getValue().toArray(startingDasaPlanets);
        
        int i = 0;
        for (LordNode node: startingDasaPlanets)
        {
            System.out.println(node);
            remainDasaFraction[i++] = node.getRemainingDurationForLongitude(planetLongitude);
        }
        
        long[] dasaEndDate = {dob, dob, dob, dob, dob}; 
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Vimshottari Dasa", true);
        Planet startingMahaDasaPlanet = startingDasaPlanets[0].getPlanet();
        Planet startingBhuktiPlanet = startingDasaPlanets[1].getPlanet();
        Planet startingPratyantaraPlanet = startingDasaPlanets[2].getPlanet();
        Planet startingSookshmaPlanet = startingDasaPlanets[3].getPlanet();
        Planet startingPranaPlanet = startingDasaPlanets[4].getPlanet();
        CircularList<Planet> mahaDasaList = new CircularList<Planet>(Planet.getVimshottariDasaPlanets(), true);
        Iterator<Planet> mdIter = mahaDasaList.iterator(startingMahaDasaPlanet);
         
        while (mdIter.hasNext())
        {
            Planet mahaDasaPlanet = mdIter.next();
            double mdDuration = mahaDasaPlanet.getDasaDuration() * remainDasaFraction[0]; // Required for 1st iteration only
            DefaultMutableTreeNode mahaDasaNode = addNode(mahaDasaPlanet, root, true, mdDuration, dasaEndDate, 0);
            remainDasaFraction[0] = 1;
            mdDuration = mahaDasaPlanet.getDasaDuration(); // Required for use by bukti loop below
            
            List<Planet> bukthiPlanets = getDasaPlanetList(mahaDasaPlanet, startingBhuktiPlanet);
            for (Planet bp : bukthiPlanets)
            {
                double bdDuration = mdDuration * bp.getDasaDuration()/120D * remainDasaFraction[1];
                remainDasaFraction[1] = 1;
                DefaultMutableTreeNode bukthiDasaNode = addNode(bp, mahaDasaNode, true, bdDuration, dasaEndDate, 1);
                bdDuration = mdDuration * bp.getDasaDuration()/120D;
                
                List<Planet> pratyantaraPlanets = getDasaPlanetList(bp, startingPratyantaraPlanet);
                for (Planet pap : pratyantaraPlanets)
                {
                    double paDuration = bdDuration * pap.getDasaDuration()/120D * remainDasaFraction[2];
                    remainDasaFraction[2] = 1;
                    DefaultMutableTreeNode pratyantaraDasaNode = addNode(pap, bukthiDasaNode, true, paDuration, dasaEndDate, 2);
                    paDuration = bdDuration * pap.getDasaDuration()/120D;
                    
                    List<Planet> sookshmaPlanets = getDasaPlanetList(pap, startingSookshmaPlanet);
                    for (Planet sp : sookshmaPlanets)
                    {
                        double sdDuration = paDuration * sp.getDasaDuration()/120D * remainDasaFraction[3];
                        remainDasaFraction[3] = 1;
                        DefaultMutableTreeNode sookshmaDasaNode = addNode(sp, pratyantaraDasaNode, true, sdDuration, dasaEndDate, 3);
                        sdDuration = paDuration * sp.getDasaDuration()/120D;
                        
                        List<Planet> pranaPlanets = getDasaPlanetList(sp, startingPranaPlanet);
                        for (Planet pp : pranaPlanets)
                        {
                            double pdDuration = sdDuration * pp.getDasaDuration()/120D * remainDasaFraction[4];
                            remainDasaFraction[4] = 1;
                            addNode(pp, sookshmaDasaNode, false, pdDuration, dasaEndDate, 4);
                        }
                        startingPranaPlanet = null;
                    }
                    startingSookshmaPlanet = null;
                }
                startingPratyantaraPlanet = null;
            }
            startingBhuktiPlanet = null;
        }
        
        return root;
    }
    
    private static List<Planet> getDasaPlanetList(Planet parentDasa, Planet start)
    {
        CircularList<Planet>  planetList = new CircularList<Planet>(Planet.getVimshottariDasaPlanets(), true);
        List<Planet> rtnList = new ArrayList<Planet>();
        if (start == null)
        {
            Iterator<Planet> iter = planetList.iterator(parentDasa);
            while (iter.hasNext())
            {
                Planet p = iter.next();
                rtnList.add(p);
            }
        }
        else
        {
            Iterator<Planet> iter = planetList.iterator(start);
            while (iter.hasNext())
            {
                Planet p = iter.next();
                if (p.ordinal() == parentDasa.ordinal())
                {
                    break;
                }
                rtnList.add(p);
            }
        }
        return rtnList;
    }
    
    private static DefaultMutableTreeNode addNode(Planet dasaPlanet,
            DefaultMutableTreeNode parent, boolean allowChildren,
            double dasaDuration, long[] dasaEndDate, int endDateIndex)
    {
        VDNode vdNode = new VDNode(dasaPlanet, 0L);
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(vdNode,
                allowChildren);
        parent.add(newNode);
        
        dasaEndDate[endDateIndex] = addDate(dasaEndDate[endDateIndex], dasaDuration);
        
        vdNode.setDasaEndPeriod(dasaEndDate[endDateIndex]);

        return newNode;
    }
    
}
