package mpp.jathakamu.test;

import static mpp.jathakamu.JathakamLogger.LOGGER;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;

import swisseph.SweConst;
import swisseph.SwissEph;

import mpp.jathakamu.Constants.HORARY_NUMBER_SET;
import mpp.jathakamu.Horoscope;
import mpp.jathakamu.Jyothishyam;
import mpp.jathakamu.Constants.ASPECT_EFFECT;
import mpp.jathakamu.settings.AspectOrbsSettings;
import mpp.jathakamu.settings.Settings;
import mpp.jathakamu.types.AspectInfo;
import mpp.jathakamu.types.CuspHoroDetails;
import mpp.jathakamu.types.HoroChartData;
import mpp.jathakamu.types.Planet;
import mpp.jathakamu.types.PlanetHoroDetails;
import mpp.jathakamu.types.Raasi;
import mpp.jathakamu.utils.CalcUtils;
import mpp.jathakamu.utils.CircularList;
import mpp.jathakamu.view.ViewUtils;

public class Test1
{
    static
    {
//        System.setProperty("EPHEMERIS_PATH", "D:\\devel\\mine\\prj\\astro\\SwissEphemeris\\EphimerisFiles");
        System.setProperty("LOG_LEVEL", "ALL");
        System.setProperty("JATHAKAMU_HOME", "D:/devel/mine/prj\\astro\\workspace\\Jathakam3");
    }
    
    public static void test1()
    {
        HoroChartData dt = new HoroChartData(1973, Calendar.APRIL, 4, 8, 59, 16D, "IST", 78.5, 17D+20D/60D);
//        DateTimeLocation dt = new DateTimeLocation(1978, Calendar.DECEMBER, 7, 12, 4, 33D, "IST", 82D+1D/60D, 16D+35D/60D);
        Horoscope horo = Jyothishyam.getNatalHoroscope();
        
        List<PlanetHoroDetails> pl = horo.getPlanetHoroDetails(dt);
        List<CuspHoroDetails> cl = horo.getCuspHoroDetails(dt);
        double  ascLogitude = cl.get(0).getLongitude(),
                moonLogitude = pl.get(Planet.MOON.getIndex()).getLongitude(),
                sunLogitude = pl.get(Planet.SUN.getIndex()).getLongitude();
        PlanetHoroDetails fortuna = horo.getFortuna(ascLogitude, moonLogitude,
                sunLogitude);
        double ayanamsa = horo.getAyanamsa();
        double siderealTime = horo.getSiderealTime();
        
        horo.analyze(pl, cl);

        
        System.out.println("Ayanamsa = " + ViewUtils.toStringDegree(ayanamsa));
        System.out.println("Sidereal Time = " + ViewUtils.toStringDegree(siderealTime));
        System.out.println();

        Planet planet;
        double longitude;
        Raasi raasi;
        String viewLongitude;
        
        for (PlanetHoroDetails phd : pl)
        {
            planet = phd.getPlanet();
            longitude = phd.getLongitude();
            raasi = phd.getPositedRaasi();
            viewLongitude = ViewUtils.toStringDegreeForRaasi(longitude);
            
            System.out.print(planet.getName() + " : [" + raasi.name() + "] " + viewLongitude);
            List<Planet> lordsList = phd.getLords();
            for (Planet pln : lordsList)
            {
                System.out.print(" - " + pln);
            }
            System.out.println();
        }
        
        longitude = fortuna.getLongitude();
        raasi = fortuna.getPositedRaasi();
        viewLongitude = ViewUtils.toStringDegreeForRaasi(longitude);
        
        System.out.println("Fortuna : [" + raasi.name() + "] " + viewLongitude);
        
        System.out.println("-------------------------------------------------");
        
        for (CuspHoroDetails chd : cl)
        {
            int cusp = chd.getCusp();
            longitude = chd.getLongitude();
            raasi = chd.getPositedRaasi();
            viewLongitude = ViewUtils.toStringDegreeForRaasi(longitude);
            
            System.out.print(cusp + " : [" + raasi.name() + "] " + viewLongitude);
            
            List<Planet> lordsList = chd.getLords();
            for (Planet pln : lordsList)
            {
                System.out.print(" - " + pln);
            }
            System.out.println();
        }

//        System.out.println("=============================================");
//        for (PlanetHoroDetails phd : pl)
//        {
//            boolean flag = phd.hasNoPlanetInItsStar();
//            
//            System.out.println("Planet " + phd.getPlanet() + " Has no planets in its star? " + flag);
//            System.out.println("Planet " + phd.getPlanet() + " Conjunction :  " + phd.getConjuctionPlanets());
//            System.out.println("Planet " + phd.getPlanet() + " Good Planet Aspects :  " + phd.getGoodPlanetAspects());
//            System.out.println("Planet " + phd.getPlanet() + " Bad Planet Aspects :  " + phd.getBadPlanetAspects());
//            System.out.println("Planet " + phd.getPlanet() + " Good Cusp Aspects :  " + phd.getGoodCuspAspects());
//            System.out.println("Planet " + phd.getPlanet() + " Bad Cusp Aspects :  " + phd.getBadCuspAspects());
//            System.out.println("=============================================");
//        }
//        Planet p = Planet.MARS;
//        PlanetHoroDetails phdSaturn = pl.get(p.getIndex());
//        List<Planet> splPlanets = phdSaturn.getSpecialPlanetAspects();
//        for (Planet splPl : splPlanets)
//        {
//            System.out.println("Special Aspect for " +  p + " : " + splPl);
//        }
//        
//        List<Integer> splCusps = phdSaturn.getSpecialCuspAspects();
//        for (Integer cusp : splCusps)
//        {
//            System.out.println("Special Cusp for planet " + p + " : " + cusp);
//        }
//        
//        p = Planet.RAHU;
//        PlanetHoroDetails phdRahu = pl.get(p.getIndex());
//        List<Planet> seventhAspectPlanets = phdRahu.getSeventhAspect();
//        
//        System.out.println("7th Aspect for Rahu : " + seventhAspectPlanets);
    }
    
    public static void test2()
    {
        CircularList<Planet> cl = new CircularList<Planet>(Planet.getVimshottariDasaPlanets(), true, 0);
        
        for (Planet dp : cl.iterable(0))
        {
            for (Planet bp : cl.iterable(dp.ordinal()))
            {
                System.out.println(dp.getName() + "-" + bp.getName());
            }
        }
    }
    
    public static void test3()
    {
        CalcUtils.initializeLordsTable();
    }
    
    public static void test4()
    {
        Jyothishyam.getNatalHoroscope();
        double longitude = 330D + 29D + 39D / 60D + 34D / 3600D;
        HoroChartData dt = new HoroChartData(1973, Calendar.APRIL, 4, 8, 59, 16D, "IST", 78.5, 17D+20D/60D);
        long dob = dt.getTimeInMillis();
        
        TreeNode treeNode = CalcUtils.getVDTree(dob, longitude);
        
        JTree vdTree = new JTree(treeNode);
        JFrame mainFrame = new JFrame("Vimshottari Tree");
        
        vdTree.setEditable(false);
        vdTree.setShowsRootHandles(false);
        
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.add(new JScrollPane(vdTree));
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
    
    public static void test5()
    {
        AspectInfo aspectInfo = new AspectInfo(120, Planet.SUN.getIndex(), Planet.MOON.getIndex());
        AspectOrbsSettings.setAspectAttr(aspectInfo);
        ASPECT_EFFECT effect = aspectInfo.getAspect();
//        AspectOrbsSettings.save();
        System.out.println("effect = " + effect);
    }

    public static void horaryTest()
    {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        cal.setLenient(false);
        cal.set(1994, Calendar.MARCH, 10, 11, 00, 00);
        cal.set(Calendar.MILLISECOND, 0);
        double longitude = 80D + 17D / 60D;
        double latitude = 13D + 04D / 60D;
        HoroChartData dt = new HoroChartData(cal, longitude, latitude, 150);
        Horoscope horo = Jyothishyam.getHoraryHoroscope();
        List<PlanetHoroDetails> pl = horo.getPlanetHoroDetails(dt);
        Planet planet;
        Raasi raasi;
        
        double planetLongitude;
        String viewLongitude;
        for (PlanetHoroDetails phd : pl)
        {
            planet = phd.getPlanet();
            planetLongitude = phd.getLongitude();
            raasi = phd.getPositedRaasi();
            viewLongitude = ViewUtils.toStringDegreeForRaasi(planetLongitude);
            
            System.out.printf("\n%s : [%s] %s", planet.getName(), raasi.name(), viewLongitude);
        }
        
        List<CuspHoroDetails> cl = horo.getCuspHoroDetails(dt);
        
        for (CuspHoroDetails chd : cl)
        {
            int cusp = chd.getCusp();
            longitude = chd.getLongitude();
            raasi = chd.getPositedRaasi();
            viewLongitude = ViewUtils.toStringDegreeForRaasi(longitude);
            
            System.out.println(cusp + " : [" + raasi.name() + "] " + viewLongitude);
        }

    }
    
    public static void testSunRise()
    {
        HoroChartData dt = new HoroChartData(1973, Calendar.APRIL, 4, 8, 59, 16D, "IST", 78.5, 17D+20D/60D);
        Horoscope horo = Jyothishyam.getNatalHoroscope();
        
        long sunRise = horo.getPlanetRiseTime(Planet.SUN, dt);
        System.out.println("Sun Rise : " + new Date(sunRise));
        
        long sunSet =  horo.getPlanetSetTime(Planet.SUN, dt);
        System.out.println("Sun set : " + new Date(sunSet));
    }
    
    public static void test7()
    {
        for (int i = 0; i <= 360; i++)
        {
            test6(i);
        }
    }
    
    public static void test6(double asc)
    {
        StringBuffer serr = new StringBuffer();
        HoroChartData dt = new HoroChartData(1973, Calendar.APRIL, 4, 8, 59, 0D, "IST", 82.5, 0D+0D/60D);
        double tjd_ut = dt.getJulianDay();
        SwissEph se = getSwissEphemeris(tjd_ut);
        double ayanamsa = se.swe_get_ayanamsa_ut(tjd_ut);
        double[] xx = new double[6];
        double eps;
        double[] cusps = new double[37];
        double[] ascmc = new double[10];
        double armc = 1D;
//        double asc = 330D + 57D/60D;
        double geocentriclat = 0D;
        Raasi positedRaasi = Raasi.getRaasi(asc);
        
        se.swe_calc_ut(tjd_ut, SweConst.SE_ECL_NUT, 0, xx, serr);
        eps = xx[0];
        
//        System.out.printf("\nEcliptic obliquity = %s [%s]", eps, ViewUtils.toStringDegree(eps));
        
        double e = StrictMath.toRadians(eps);
        double a = StrictMath.toRadians(asc);
        double v1 = StrictMath.cos(e);
        double v2 = StrictMath.tan(a);
        double v3 = StrictMath.sin(e);
        double v4 = StrictMath.sin(a);
        double v5 = v1 * v2;
        double v6 = v3 * v4;
        double ra = StrictMath.atan(v5);
        double d = StrictMath.asin(v6);
        double l = StrictMath.toRadians(geocentriclat);
        double oa = ra - StrictMath.asin(StrictMath.tan(d) * StrictMath.tan(l));
        double oaInDeg = StrictMath.toDegrees(oa);
        double ramc = StrictMath.abs(oaInDeg + 90);
        double sidrealTime = ramc / 15D;
        
        // 0 > Asc < 90 Add 270 to oa
        // 90 > Asc <= 270 Add 90 to oa
        // 270 > Asc < 360 Add 270 to oa
        if ((asc >= 0 && asc <= 90) || (asc > 270 && asc <= 360))
        {
            ramc += 180D;
        }
        
        System.out.printf("\nASC = %s ==> RAMC = %s [%s], Sidereal Time = %s [%s]",
                asc, ramc, ViewUtils.toStringDegree(ramc),
                sidrealTime,  ViewUtils.toStringDegree(sidrealTime));
        
//        se.swe_houses_armc(armc, 0D, eps, 'P', cusps, ascmc);
//        
//        System.out.printf("\nAscendent = %s [%s]", cusps[1], ViewUtils.toStringDegree(cusps[1]));
//        System.out.printf("\nII Cusp = %s [%s]", cusps[10], ViewUtils.toStringDegree(cusps[2]));
//        System.out.printf("\nMC = %s [%s]", cusps[10], ViewUtils.toStringDegree(cusps[10]));
    }
    
    public static SwissEph getSwissEphemeris(double tjd_ut)
    {
        SwissEph sweph = new SwissEph(Settings.EPHEMERIS_FILES_PATH);
        String ver = sweph.swe_version();
//        System.out.printf("Swiss Ephmeris Version is %s", ver);

        sweph.swe_set_sid_mode(Settings.AYANAMSA, Settings.ayan_t0,
                Settings.ayan_initial_value);
        double ayanamsa = sweph.swe_get_ayanamsa_ut(tjd_ut);
//        System.out.printf("\nAyanamsa = %s %s", ayanamsa, ViewUtils.toStringDegree(ayanamsa));
        
        return sweph;
    }
    
    public static void main(String[] args)
    {
//        testSunRise();
//        horaryTest();
        test1();
        
//        test2();
//        test3();
//        test4();
//        test5();
//        test7();
    }
    
}
