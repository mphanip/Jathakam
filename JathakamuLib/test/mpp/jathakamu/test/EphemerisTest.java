/*
 * Yet to decide on the License, should either BSD or GPL
 *  Copyright(R) Phani Pramod
 */
package mpp.jathakamu.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import mpp.jathakamu.JathakamuException;
import mpp.jathakamu.Profile;
import mpp.jathakamu.ProfileSettings;
import mpp.jathakamu.calculations.EphemerisCalcs;
import mpp.jathakamu.calculations.SupportCalcs;
import mpp.jathakamu.types.Degree;
import mpp.jathakamu.types.LordNode;
import mpp.jathakamu.types.Place;
import mpp.jathakamu.types.Planet;
import mpp.jathakamu.types.Raasi;
import mpp.jathakamu.types.VDNode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import swisseph.SweConst;
import swisseph.SweDate;

/**
 *
 * @author phani
 */
public class EphemerisTest
{
    private static final int RAHU = 7;
    private static final int KETU = 8;

    public EphemerisTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
        System.setProperty("EPHEMERIS_PATH",
                "/home/phani/prj/3rdParty/SwissEphemeris/EphimerisFiles");
        System.setProperty("JATHAKAMU_HOME", "/home/phani/prj/JathakamuHome");
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    @Test
    @Ignore
    public void julianDayTest()
            throws JathakamuException
    {
        double jd_ut = SupportCalcs.getJulianDay(1973, 4, 4, 8, 59, 8, 0,
                "Asia/Calcutta");
        
        System.out.println("Given Date : " + (new SweDate(jd_ut)));
        
        System.out.println("---- Using Date -----");
        
        jd_ut = SupportCalcs.getJulianDay(new Date(), TimeZone.getDefault().getID());
        
        System.out.println("Given Date 2 : " + (new SweDate(jd_ut)));
    }

    @Test
    public void ephPlanetPositionsTest()
            throws JathakamuException
    {
        final List<List<Object>> planetDetails = new ArrayList<>();
        
        for (int i = 0; i < 9; i++)
        {
            planetDetails.add(new ArrayList<>());
        }
        
        System.out.println("Testing ephPlanetPositionsTest>>>>>>>>>>>>");
//        double jd_ut = SupportCalcs.getJulianDay(1973, 4, 4, 8, 59, 8, 0,
//                "Asia/Calcutta");
        double jd_ut = SupportCalcs.getJulianDay(1973, 4, 4, 8, 55, 8, 65,
                "Asia/Calcutta");
        Place place = (new Place.Builder()).name("Hyderabad")
                .longitude(78.5D)
                .latitude(17.333333333D)
                .build();
        ProfileSettings profileSettings = new ProfileSettings();
//        profileSettings.setEphemerisFlags(SweConst.SEFLG_SPEED | SweConst.SEFLG_SWIEPH);
        profileSettings.setUseGeocentric(false);
//        profileSettings.setKPNewAynamsa();
        profileSettings.setPushyaPakshaAyanamsa();
        Profile profile = new Profile(jd_ut, place, profileSettings);
        EphemerisCalcs ephCalc = new EphemerisCalcs(profile);
        
        double[] moonInfo = {0};
        
        for (int i = 0; i < 7; i++)
        {
            double[] planetInfo = ephCalc.getPlanetDetails(i);
            planetDetails.get(i).add(ephCalc.getPlanetName(i));
            planetDetails.get(i).add(new Degree(planetInfo[0], true));

//            showDetails(ephCalc.getPlanetName(i), planetInfo);
            
            if (i == SweConst.SE_MOON)
            {
                moonInfo = planetInfo;
            }
        }

        double[] planetInfo = ephCalc.getPlanetDetails(SweConst.SE_TRUE_NODE);
        double[] rahuPosition = {planetInfo[0], -1};
        planetDetails.get(7).add("Rahu");
        planetDetails.get(7).add(new Degree(rahuPosition[0], true));
//        showDetails("Rahu", rahuPosition);
        
        double[] ketuPosition = {SupportCalcs.degnorm(planetInfo[0]+180), -1};
        planetDetails.get(8).add("Ketu");
        planetDetails.get(8).add(new Degree(ketuPosition[0], true));
//        showDetails("Ketu", ketuPosition);
        
        setPlanetDetails(planetDetails);
        
        double []cusps = ephCalc.getCuspDetails();
        
        int i = 1;
        for (double cusp : cusps)
        {
//            showDetails("Cusp " + i, new double[] {cusp, 1});
            System.out.println("Cusp " + i + " = " + new Degree(cusp, true));
            List<LordNode> lords = SupportCalcs.getLords(cusp);
            int j = 1;
            for (LordNode ln : lords)
            {
                Planet pln = ln.getPlanet();
                int planetIndex = pln.getIndex();

                if (planetIndex == Planet.RAHU.getIndex())
                {
                    planetIndex = RAHU;
                }
                else if (planetIndex == Planet.KETU.getIndex())
                {
                    planetIndex = KETU;
                }

                Object info = planetDetails.get(planetIndex).get(5+j);
                if (info instanceof String)
                {
                    List<Integer> newValue = new ArrayList<>();
                    planetDetails.get(planetIndex).set(5+j, newValue);
                }

                ((List<Integer>) (planetDetails.get(planetIndex).get(5+j))).add(i);
                
                j++;
                if (j == 4)
                {
                    break;
                }
            }
            i++;
        }
        
        System.out.println("Planet Details");
        planetDetails.stream().forEach((plnInfo -> System.out.println(plnInfo)));
        
        long[] sunriseTime = ephCalc.getSunRiseTime();
        System.out.println("Sun Rise = " + new Date(sunriseTime[0]));
        System.out.println("Next Sun Rise = " + new Date(sunriseTime[1]));
        
        long[] sunSetTime = ephCalc.getSunSetTime();
        System.out.println("Sun Set = " + new Date(sunSetTime[0]));
        System.out.println("Next Sun Set = " + new Date(sunSetTime[1]));
        
        long dob = SweDate.getDate(jd_ut).getTime();
        System.out.println("Date of Birth = " + new Date(dob));
        VDNode vdNode = SupportCalcs.getVDTree2(dob, moonInfo[0], profile);
        Calendar cal = Calendar.getInstance();
        cal.set(2001, Calendar.FEBRUARY, 4, 21, 35, 0);
        long event = cal.getTimeInMillis();
        List<VDNode> dbaList = vdNode.findDBA(event);
        System.out.println("Marriage DBA = " + dbaList);
        
        cal.set(2002, Calendar.FEBRUARY, 28, 14, 15, 0);
        event = cal.getTimeInMillis();
        dbaList = vdNode.findDBA(event);
        System.out.println("Child 1 DBA = " + dbaList);
        
        cal.set(2006, Calendar.OCTOBER, 17, 17, 15, 0);
        event = cal.getTimeInMillis();
        dbaList = vdNode.findDBA(event);
        System.out.println("Child 2 DBA = " + dbaList);
        
        System.out.println("End of Testing ephPlanetPositionsTest<<<<<<<<<<");
    }
    
    @Test
    @Ignore
    public void brtTest()
            throws JathakamuException
    {
        System.out.println("Testing brtTest>>>>>>>>>>>>");
        double jd_ut = SupportCalcs.getJulianDay(1973, 4, 4, 8, 55, 8, 65,
                "Asia/Calcutta");
//        double hrs = (8 + 55/60d + 8/3600d + 65/3600000d) - 5.5;
//        double jd_ut = SweDate.getJulDay(1973, 4, 4, hrs);
        Place place = (new Place.Builder()).name("Hyderabad")
                .longitude(78.5D)
                .latitude(17.333333333D)
                .build();
        ProfileSettings profileSettings = new ProfileSettings();
        profileSettings.setUseGeocentric(false);
        
        profileSettings.setPushyaPakshaAyanamsa();
        Profile profile = new Profile(jd_ut, place, profileSettings);
        EphemerisCalcs ephCalc = new EphemerisCalcs(profile);
        int pindex = Planet.MOON.getIndex();
        double[] planetInfo = ephCalc.getPlanetDetails(pindex);
        double lagna = ephCalc.getCuspDetails()[0];
        double calcMoonPos = SupportCalcs.getMoonPositionGivenLagna(lagna);
        
        System.out.println("Calculated moon position = " + calcMoonPos +
                ", Ephemeris moon position : " + planetInfo[0]);
         
        System.out.println("End of Testing brtTest<<<<<<<<<<");
    }
    
    private void setPlanetDetails(List<List<Object>> planetDetails)
    {
        planetDetails.parallelStream().forEach((planetInfo) ->
        {
            double planetLongitude = ((Degree) planetInfo.get(1)).getDegree();
            Raasi raasi = Raasi.getRaasi(planetLongitude);
            Planet signLord = Planet.getSignLord(raasi);
            planetInfo.add(signLord);

            List<LordNode> lords = SupportCalcs.getLords(planetLongitude);
            lords.stream().limit(3).forEach(l -> planetInfo.add(l.getPlanet()));
            
            for (int i = 0; i < 3; i++)
            {
                planetInfo.add("None");
            }
        });
    }
    
//    private void showDetails(String name, List<List<Object>> planetDetails)
//    {
//        StringBuilder sb = new StringBuilder(name);
//        double planetLongitude = planetInfo[0];
//        sb.append(" => ").append(new Degree(planetLongitude));
//        sb.append((planetInfo[1] < 0) ? "(R)" : "");
//        
//        Raasi raasi = Raasi.getRaasi(planetLongitude);
//        Planet signLord = Planet.getSignLord(raasi);
//        sb.append(" - ").append(signLord);
//        
//        List<LordNode> lords = SupportCalcs.getLords(planetLongitude);
//        lords.stream().limit(3).forEach(l -> sb.append("-").append(l.getPlanet()));
//        
//        System.out.println(sb);
//    }
}
