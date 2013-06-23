/*
 *
 *
 */
package mpp.jathakam;

import static mpp.jathakam.core.JathakamConstants.FORTUNA;
import static mpp.jathakam.core.JathakamConstants.SE_HSYS_PLACIDUS;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;

import mpp.jathakam.core.BirthDateTime;
import mpp.jathakam.core.HouseInfo;
import mpp.jathakam.core.JathakamConstants;
import mpp.jathakam.core.JathakamMain;
import mpp.jathakam.core.Lords;
import mpp.jathakam.core.NakshatraLordMap;
import mpp.jathakam.core.PlaceInfo;
import mpp.jathakam.core.Planet;
import mpp.jathakam.core.PlanetCoordinates;
import mpp.jathakam.core.Raasi;
import mpp.jathakam.core.TimeZoneInfo;
import mpp.jathakam.core.dc.D12;
import mpp.jathakam.core.dc.D9;
import mpp.jathakam.core.vd.VimshottariDasa;
import mpp.jathakam.utils.CircularList;
import mpp.jathakam.utils.JatakamUtilities;
import swisseph.SweConst;
import swisseph.SweDate;
import swisseph.SwissEph;
import swisseph.SwissLib;

/**
 *
 * @author phani
 */
public class Main
{
    private static StringBuffer serr = new StringBuffer();
//    private static int flag = JathakamConstants.SEFLG_SIDEREAL | JathakamConstants.SEFLG_TRUEPOS;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
//    	Runtime rt = Runtime.getRuntime();
//    	long fm1 = rt.freeMemory();
		
//		 test1();
		// test2();
		// test3();
		// test4();
		// test5();
		// test6();
		// test7();
		// test8();
		// test9();
//    	testVD();
//    	test10();
    	
    	Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("IST"));
    	cal.set(1973, 3, 4, 8, 59, 16);
    	
    	System.out.println("IST Cal = " + cal.get(Calendar.YEAR) + "/"
                + (cal.get(Calendar.MONTH) + 1) + "/"
                + cal.get(Calendar.DAY_OF_MONTH) + "/"
                + " "
                + cal.get(Calendar.HOUR_OF_DAY) + ":" 
                + cal.get(Calendar.MINUTE)+ ":"
                + cal.get(Calendar.SECOND)
                + "[ " + cal.getTime() + " ]");
    	
    	
    	cal.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
    	System.out.println("GMT Cal = " + cal.get(Calendar.YEAR) + "/"
    	                            + (cal.get(Calendar.MONTH) + 1) + "/"
    	                            + cal.get(Calendar.DAY_OF_MONTH) + "/"
    	                            + " "
    	                            + cal.get(Calendar.HOUR_OF_DAY) + ":" 
    	                            + cal.get(Calendar.MINUTE)+ ":"
    	                            + cal.get(Calendar.SECOND)
    	                            + "[ " + cal.getTime() + " ]");
    	
//    	String[] ids = TimeZone.getAvailableIDs();
//    	SortedSet<String> sortedIds = new TreeSet<String>();
//    	
//    	for (String s: ids)
//    	{
//    	    sortedIds.add(s);
//    	}
//    	
//    	for (String s: sortedIds)
//    	{
//    	    System.out.println("Time zone is '" + s + "'");
//    	}
    }

    public static void test1()
    {
        JathakamMain swe = new JathakamMain();
        swe.setKPOldAynamsa();

        BirthDateTime bdt = new BirthDateTime(1973, 4, 4, 8, 59, 16.017);
        Set<String> tzi = TimeZoneInfo.getAllDisplayNames();
        String tz = "";

        for (String name : tzi)
        {
            if (name.toUpperCase().startsWith("IST"))
            {
                tz = name;
                break;
            }
        }

        PlaceInfo pi = new PlaceInfo("Hyderabad", 78.5D, 17.0D + 20D/60D, tz);
//        PlaceInfo pi = new PlaceInfo("Bangalore", 77D + 38D/60D, 12.0D + 58D/60D, tz);
//        PlaceInfo pi = new PlaceInfo("Guntur", 80D + 27D/60D, 16D + 22D/60D, tz);
//        PlaceInfo pi = new PlaceInfo("Test", 82.5D, 17.0D, tz);
//        PlaceInfo pi = new PlaceInfo("London", 0D, 51D + 32D/60D, tz);

        System.out.println("Birth Info: " + bdt);
        System.out.println("Place: " + pi);
        
        List<PlanetCoordinates> planetPosList = null;
        NakshatraLordMap nlMap = null;
        try
        {
            planetPosList = swe.getPlanetInfo(bdt, pi, true);
        }
        catch (JatakamException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        List<HouseInfo> hiList = swe.getHouses(bdt, pi, SE_HSYS_PLACIDUS);
        
        // Calculate fortuna
        // Longitude of Fortuna = Longitude of Ascendant + Longitude of Moon - Longitude of Sun
        double ascLogitude = hiList.get(0).getPosition();
        double moonLogitude = planetPosList.get(SweConst.SE_MOON).getLongitude();
        double sunLogitude = planetPosList.get(SweConst.SE_SUN).getLongitude();
        double fortunaLongitude = ascLogitude + moonLogitude - sunLogitude;
        planetPosList.add(new PlanetCoordinates(FORTUNA, fortunaLongitude, 0D, 0D, 0D, 0D, 0D));
        nlMap = new NakshatraLordMap(planetPosList);

        System.out.println("********** PLANETS POSITION ***********");
        for (PlanetCoordinates planet : planetPosList)
        {
        	int planetIndex = planet.getIndex();
        	
            List<Planet> planetLords = nlMap.getLords(planetIndex);
            System.out.print(planet + " - " + planetLords);
            
            Planet p = Planet.getPlanet(planetIndex);
            
            if (nlMap.isSelfStar(p))
            {
            	System.out.print(" #");
            }
            
            if (nlMap.hasNoPlanetInItsStar(p))
            {
            	System.out.print(" *");
            }
            
            System.out.println();
        }

        System.out.println("********** HOUSE POSITION ***********");
        for (HouseInfo hi : hiList)
        {
            List<Planet> houseLords = Lords.getPlanetLords(hi.getPosition());
            System.out.println(hi + " - " + houseLords);
        }

        System.out.println("********** Planet Cusp *************");
        for (PlanetCoordinates planet : planetPosList)
        {
            int cusp = swe.getPlanetCusp(planet.getLongitude());
            System.out.println("Cusp for the planet " + JathakamConstants.PLANET_NAMES[planet.getIndex()] + " is  " + cusp);
        }
        
//        System.out.println("************* Planet Significators ***************");
//
//        for (PlanetCoordinates planet : planetPosList)
//        {
//        	int planetIndex = planet.getIndex();
//			if ((planetIndex >= SweConst.SE_URANUS && planetIndex <= SweConst.SE_PLUTO)
//					|| planetIndex == FORTUNA)
//			{
//				continue;
//			}
//        	Planet curPlanet = Planet.getPlanet(planet.getIndex());
//	        System.out.println("############\n" + curPlanet.getName() + " Significators\n##############");
//	        // Level 1: House Occupied by the Nakshatra Lord(NL) of the planet.
//	        double planetLongitude = planet.getLongitude();
//	        List<Planet> lordsList = Lords.getPlanetLords(planetLongitude);
//	        Planet nlPlanet = lordsList.get(1);
//	        double nlPosition = planetPosList.get(nlPlanet.getIndex()).getLongitude();
//	        int nlPlanetCusp = swe.getPlanetCusp(nlPosition);
//	        System.out.println("Level 1 : " + nlPlanetCusp);
//	        
//	        // Level 2: House occupied by the planet itself
//	        System.out.println("Level 2 : " + swe.getPlanetCusp(planetLongitude));
//	        
//	        // Level 3: Houses owned by the NL.
//	        Set<Raasi> raasiList = nlPlanet.getOwnerRaasiList();
//	        System.out.print("Level 3: ");
//	        for (Raasi raasi : raasiList)
//	        {
//	        	List<Integer> cuspsList = swe.getCuspsInRaasi(raasi);
//	        	System.out.print(cuspsList + ", ");
//	        }
//	        System.out.println();
//	        
//	        // Level 4: Houses owned by the planet itself
//	        raasiList = curPlanet.getOwnerRaasiList();
//	        System.out.print("Level 4: ");
//	        for (Raasi raasi : raasiList)
//	        {
//	        	List<Integer> cuspsList = swe.getCuspsInRaasi(raasi);
//	        	System.out.print(cuspsList + ", ");
//	        }
//	        System.out.println();
//        }

//        System.out.println("*********** Cusp Significators **************");
//        for (int i = 1; i <= 12; i++)
//        {
//        	System.out.println("############\nCusp " + i + " Significators\n##############");
//        	
//        	System.out.print("Level 1: ");
//        	List<Integer> planetList = swe.getPlanetsInCusp(i, planetPosList);
//        	for (int planetIndex : planetList)
//        	{
//        		Planet planet = Planet.getPlanet(planetIndex);
//        		if (planet == null)
//        		{
//        			continue;
//        		}
//        		
//        		List<Planet> level1Planets = nlMap.getPlanetsHavingNakshatraLord(planet);
//        		System.out.print(level1Planets);
//        	}
//        	System.out.println();
//        	
//        	System.out.print("Level 2: ");
//        	for (int level2planet : planetList)
//        	{
//        		System.out.print(JathakamConstants.PLANET_NAMES[level2planet]);
//        		System.out.print(", ");
//        	}
//        	System.out.println();
//        	
//        	Raasi cuspRaasi = swe.getCuspRaasi(i);
//        	Planet raasiOwner = Planet.getPlanetSign(cuspRaasi);
//        	List<Planet> level3Planets = nlMap.getPlanetsHavingNakshatraLord(raasiOwner);
//        	System.out.println("Level 3: " + level3Planets);
//
//        	System.out.println("Level 4: " + raasiOwner);
//        }

        System.out.println("********** Birth Time Rectification ************");
        double moonPosition = JatakamUtilities.getMoonPositionGivenAsc(hiList.get(0).getPosition());
        double calMoonPos = planetPosList.get(Planet.MOON.getIndex()).getLongitude();
        double diff = Math.abs(moonPosition - calMoonPos);
        
        String sMoonPos = JatakamUtilities.toStringDegree(moonPosition);
        String sCalMoonPos = JatakamUtilities.toStringDegree(calMoonPos);
        String sDiff = JatakamUtilities.toStringDegree(diff);
        
        System.out.println("BTR Moon Position = " + sMoonPos
        		+ "\nCalculated moon position : " + sCalMoonPos
        		+ "\nDifference : " + sDiff
        		);
    }

    public static void test2()
    {
    	SwissEph se = new SwissEph();
    	SwissLib sl = new SwissLib();
        int day = 15;
        int month = 3;
        int year = 2012;
        int hrs = 18;
        int mins = 55;
        int secs = 26;
        double longitude = 82D + 30D/60D;
        double latitude = 12D + 52D/60D + 58D/3600D;
        double[] lon_lat_rad = new double[6];
        double[] cusps = new double[37];
        double[] ascmc = new double[10];

        int[] iyear_out = new int[1];
        int[] imonth_out = new int[1];
        int[] iday_out = new int[1];
        int[] ihour_out = new int[1];
        int[] imin_out = new int[1];
        double[] dsec_out = new double[1];
        
        long timeOffset = (long) (5.5D * 1000 * 60 * 60);

        SweDate sweDate = new SweDate(year, month, day, hrs/1D + mins/60D + secs);
        Date utcDate = sweDate.getDate(timeOffset);
        Calendar utcCal = Calendar.getInstance();
        utcCal.setTime(utcDate);
        iyear_out[0] = utcCal.get(Calendar.YEAR);
        imonth_out[0] = utcCal.get(Calendar.MONTH)+1;
        iday_out[0] = utcCal.get(Calendar.DAY_OF_MONTH);
        ihour_out[0] = utcCal.get(Calendar.HOUR_OF_DAY);
        imin_out[0] = utcCal.get(Calendar.MINUTE);
        dsec_out[0] = utcCal.get(Calendar.SECOND) + utcCal.get(Calendar.MILLISECOND)/1000D;

        System.out.println("UT Time = " + iday_out[0] + "/" + imonth_out[0] + "/"
            + iyear_out[0] + " " + ihour_out[0] + ":" + imin_out[0] + ":"
            + dsec_out[0]);

        double hoursUT = ihour_out[0] + imin_out[0] + dsec_out[0];
        double jul_day_UT = SweDate.getJulDay(iyear_out[0], imonth_out[0], iday_out[0], hoursUT);
        double hours = hrs + mins + secs;
        double jul_day = SweDate.getJulDay(year, month, day, hours);

        se.swe_set_sid_mode(255,SweDate.getJulDay(1900, 1, 1, 0.0D), 22.362416666666665D);
        double ayan = se.swe_get_ayanamsa_ut(jul_day_UT);
        System.out.println("Ayanamsa = " + JatakamUtilities.toStringDegree(ayan));

        se.swe_calc_ut(jul_day_UT, 1, 0, lon_lat_rad, serr);
        System.out.println("Moon Position = " + JatakamUtilities.toStringDegree(lon_lat_rad[0]-ayan));

        se.swe_calc_ut(jul_day_UT, Planet.SATURN.getIndex(), 0, lon_lat_rad, serr);
        System.out.println("Saturn Position = " + JatakamUtilities.toStringDegree(lon_lat_rad[0]-ayan));

        se.swe_houses(jul_day, 0, latitude, longitude, 'P', cusps, ascmc);

        double sidTime = sl.swe_sidtime(jul_day_UT) + 5.5D;
        System.out.println("Sidereal Time: " + JatakamUtilities.toStringDegree(sidTime));

        System.out.println("Sidereal Time: [" + ascmc[2]/15D + "] : " + JatakamUtilities.toStringDegree(ascmc[2]/15D));
        System.out.println("Asc : " + JatakamUtilities.toStringDegree(sl.swe_degnorm(cusps[1]-ayan)));

        se.swe_close();
    }

    public static void test3()
    {
//    	SwissEph se = new SwissEph();
//    	SwissLib sl = new SwissLib();
//        double jul_day_UT = SweDate.getJulDay(2012, 3, 14, 17D+3D/60D+51D/3600D);
//        double []e = new double[1];
//        int []year = new int[1];
//        int []month = new int[1];
//        int []day = new int[1];
//        double [] hours = new double[1];
//
//        double jul_day_ET = jul_day_UT + (new SweDate(jul_day_UT)).getDeltaT();
//
//        System.out.println("Delta : " + SweDate.getDeltaT(jul_day_UT));
//
//        SweNativeLib.swe_time_equ(jul_day_ET, e, serr);
//
//        System.out.println("E = " +  JatakamUtilities.toStringDegree(e[0]));
//
////        jul_day_UT += e[0];
//        SweNativeLib.swe_revjul(jul_day_UT, flag, year, month, day, hours);
////        hours[0] -= 19.466666667D/60D;
//
//        System.out.println(day[0] + "/" + month[0] + "/" + year[0] + " " + JatakamUtilities.toStringDegree(hours[0]));
    }

    public static void test4()
    {
//        String value = JatakamUtilities.toStringDateTime(2012, 12, 1, 5 + 30D/60D + 32.5D/3600D);
//        System.out.println(value);

//        PlanetDasaIterator iter = new PlanetDasaIterator(Planet.KETU);
//
//        for (Planet p : iter)
//        {
//            System.out.println(p);
//        }

//        Calendar cal = Calendar.getInstance();
//        cal.setTimeInMillis(0);
//        Date dt = cal.getTime();
//        System.out.println("date = " + dt + "[" + cal.getTimeInMillis() + "]");

//        BirthDateTime bt = JatakamUtilities.getLMT(new BirthDateTime(1973, 4, 4, 8, 59, 16D), new PlaceInfo("Hyderabad", 78.5D, 17D + 20D/60D, "IST-Indian Standard Time"));
//        System.out.println("LMT : " + bt);
//        bt = JatakamUtilities.getLMT(new BirthDateTime(1973, 4, 4, 8, 59, 16D), new PlaceInfo("Bangalore", 77D + 38D/60D, 12.0D + 58D/60D, "IST-Indian Standard Time"));
//        System.out.println("LMT : " + bt);

//        List<Planet> planetList = Lords.getPlanetLords(120 + 17 + 32D/60D + 4D/3600D);
//        System.out.println(planetList);

//        double longitude = 120 + 17 + 32D/60D + 4D/3600D;
//        Map.Entry<Range, List<DasaNode>> node =  VimshottariDasa.getDasaEntry(longitude);
//        Nakshatram star = Nakshatram.getStar(longitude);
//
//        List<DasaNode> nodeList = node.getValue();
//        Planet mahaDasaPlanet = nodeList.get(1).getPlanet();
//
//        Range dasaRange = node.getKey();
//        double endExtent = dasaRange.end();
//
//        Range starRange = star.getExtent();
//        double starEndExtent = starRange.end();
//
//        double diff = Math.round((starEndExtent - longitude) * 60);
//
//        double dasaBal = (diff * mahaDasaPlanet.getDasaDuration()) / 800D;
//
//        System.out.println(star + ", Start Extent="
//            + JatakamUtilities.toStringDegree(starEndExtent) + ", "
//            + mahaDasaPlanet
//            + ", End extent=" + JatakamUtilities.toStringDegree(endExtent)
//            + "\ndiff=" + diff + ", Dasa Balance="
//            + JatakamUtilities.toStringDateTime(dasaBal));
    }

//    public static void test5()
//    {
//        D9 d9 = new D9();
//
//        double longitude = 270D + 19D + 14.0/60.0;
//        Raasi r = d9.getRaasi(longitude);
//        System.out.println("Mer Navamsa Raasi for longitude " + longitude + " is " + r);
//
//        longitude = 300D + 15D + 56.0/60.0;
//        r = d9.getRaasi(longitude);
//        System.out.println("Moon Navamsa Raasi for longitude " + longitude + " is " + r);
//
//        longitude = 300D + 7D + 9.0/60.0;
//        r = d9.getRaasi(longitude);
//        System.out.println("Ven Navamsa Raasi for longitude " + longitude + " is " + r);
//
//        longitude = 330D + 24D + 23.0/60.0;
//        r = d9.getRaasi(longitude);
//        System.out.println("Sat Navamsa Raasi for longitude " + longitude + " is " + r);
//
//        longitude = 0D + 2D + 49.0/60.0;
//        r = d9.getRaasi(longitude);
//        System.out.println("Mar Navamsa Raasi for longitude " + longitude + " is " + r);
//
//        longitude = 30D + 13D + 44.0/60.0;
//        r = d9.getRaasi(longitude);
//        System.out.println("Rah Navamsa Raasi for longitude " + longitude + " is " + r);
//
//        longitude = 150D + 21D + 36.0/60.0;
//        r = d9.getRaasi(longitude);
//        System.out.println("Jup Navamsa Raasi for longitude " + longitude + " is " + r);
//    }
//    
    public static void test6()
    {
    	SwissEph se = new SwissEph();
    	SwissLib sl = new SwissLib();
    	SweDate sdate = new SweDate(SweDate.JD0);
    	
    	System.out.println("SwissEph Version " + se.swe_version() );
//    	boolean flag = sdate.setDate(1973, 4, 4, 5.5);
//    	System.out.println("Is Date Correct: " + flag);
    	Date date = sdate.getDate(0);
    	long millis = (long)((sdate.getJulDay() - SweDate.JD0) * 24 * 3600 * 1000);
    	System.out.println("Millis = " + millis + ", " + (new Date(millis)));
    	System.out.println("Date : " + date);
    	double tjd_ut = sdate.getJulDay();
    	double sid = sl.swe_sidtime(tjd_ut);
    	System.out.println("Sidreal time : " + sid) ;
    }

//    public static void test7()
//    {
//    	Planet[] dasaPlanets = Planet.dasaPlanetValues();
//    	for (Planet planet : dasaPlanets)
//    	{
//    		System.out.println(planet);
//    	}
//    }
    
    public static void test8()
    {
    	String names[] = {
    			"Asc",
    			"Sun",
    			"Mon",
    			"Mer",
    			"Ven",
    			"Mar",
    			"Jup",
    			"Sat",
    			"Rah",
    			"Ket",
    	}; 
//    	double []longitude = {
//    			210D + 27D + 52D/60D + 35D/3600D,
//    			30D + 29D + 10D/60D + 5D/3600D,
//    			60D + 8D + 2D/60D + 16D/3600D,
//    			60D + 23D + 35D/60D + 32D/3600D,
//    			60D + 1D + 57D/60D + 7D/3600D,
//    			120D + 22D + 20D/60D + 1D/3600D,
//    			120D + 10D + 2D/60D + 23D/3600D,
//    			120D + 27D + 7D/60D + 15D/3600D,
//    			90D + 28D + 19D/60D + 59D/3600D,
//    			180D+90D + 28D + 19D/60D + 59D/3600D,
//    	};
    	
//    	double []longitude = {
//    			90D + 6D + 18D/60D + 5D/3600D,
//    			150D + 21D + 55D/60D + 25D/3600D,
//    			120D + 0D + 30D/60D + 42D/3600D,
//    			150D + 14D + 20D/60D + 44D/3600D,
//    			120D + 26D + 19D/60D + 11D/3600D,
//    			60D + 28D + 4D/60D + 16D/3600D,
//    			60D + 12D + 17D/60D + 26D/3600D,
//    			120D + 3D + 38D/60D + 34D/3600D,
//    			150D + 21D + 49D/60D + 10D/3600D,
//    			180D + 150D + 21D + 49D/60D + 10D/3600D
//    	};
    	
    	double []longitude = {
    			60D + 26D + 57D/60D,
    			180D + 11D + 46D/60D,
    			330D + 14D + 39D/60D,
    			150D + 23D + 19D/60D,
    			180D + 26D + 57D/60D,
    			150D + 16D + 51D/60D,
    			120D + 4D + 32D/60D,
    			180D + 28D + 20D/60D,
    			210D + 24D + 53D/60D,
    			30D + 24D + 53D/60D
    	};
    	
    	int i = 0;
    	for (double l : longitude)
    	{
	    	D12 d12 = new D12(l);
	    	
	        Raasi r = d12.getRaasi();
	        System.out.println("D12 Raasi for " + names[i++] + " is " + r
	        		+ " longitude is " + JatakamUtilities.toStringDegree(d12.getDCLongitude()));
    	}
    }
    
    public static void test9()
    {
    	List<Planet> lords = Lords.getPlanetLords(180D + 10D + 16D/60D + 0/3600D);
    	System.out.println("Lords = " + lords);
    	
    	D9 d9 = new D9(210D + 15D + 8D/60D + 0/3600D);
    	Raasi r = d9.getRaasi();
    	double l = d9.getDCLongitude();
    	System.out.println("Raasi is " +  r + ", longitude = " + l);
    }
    
    public static void testVD()
    {
//      VimshottariDasa.dumpDasaTable();
    	
		double longitude = 330D + 29D + 39D / 60D + 34D / 3600D;
//    	double longitude = 14D + 0D/60D + 17D/3600D;
		Calendar cal = Calendar.getInstance();
		cal.set(1973, 3, 4, 8, 59, 16);
//		cal.set(2012, 5, 15, 14, 00, 00);
		cal.set(Calendar.MILLISECOND, 0);
		long dob = cal.getTimeInMillis();
		
		VimshottariDasa vd = new VimshottariDasa(longitude);
		TreeNode treeNode = vd.getVDTree(dob);
		
		JTree vdTree = new JTree(treeNode);
		JFrame mainFrame = new JFrame("Vimshottari Tree");
		
		vdTree.setEditable(false);
		vdTree.setShowsRootHandles(false);
		
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.add(new JScrollPane(vdTree));
		mainFrame.pack();
		mainFrame.setVisible(true);
		
//		Planet []planets = new Planet[5];
//		long []endDate = new long[5];
//		String indent = "";
//		int i = 0;
//		 
//		for (DasaNode dasa : nodeList)
//		{
//			planets[i] = dasa.getPlanet();
//			double duration = dasa.getDurationInYears(longitude);
//			
//			endDate[i] = JatakamUtilities.addDate(dob, duration);
//			String sEndDate = JatakamUtilities.getDateString(endDate[i]);
//			
//			System.out.println(indent + dasa.getPlanet()
//					+ "[" + sEndDate + "]"
//					+ " {" + JatakamUtilities.toStringDateTime(duration) + "}");
//
//			indent += "\t";
//			i++;
//		}
//		
//		CircularList<Planet> vdList = new CircularList<Planet>(true);
//		vdList.addAll(Planet.getPlanetList());
//		
//		Iterator<Planet> mahaDasaIter = vdList.iterator(planets[0]);
//		Planet endPlanet;
//		Planet p;
//		
//		Iterator<Planet> buktiIter = vdList.iterator(planets[0]);
//		endPlanet =  Planet.getPlanetByOrdinal(planets[1].ordinal()-1);
//		p = buktiIter.next();
//		while (buktiIter.hasNext() && !p.equals(endPlanet))
//		{
//			p = buktiIter.next();
//		}
//		
//		Iterator<Planet> antaraIter = vdList.iterator(planets[1]);
//		endPlanet =  Planet.getPlanetByOrdinal(planets[2].ordinal()-1);
//		p = antaraIter.next();
//		while (antaraIter.hasNext() && !p.equals(endPlanet))
//		{
//			p = antaraIter.next();
//		}
//
//		Iterator<Planet> sookshmaIter = vdList.iterator(planets[2]);
//		endPlanet =  Planet.getPlanetByOrdinal(planets[3].ordinal()-1);
//		p = sookshmaIter.next();
//		while (sookshmaIter.hasNext() && !p.equals(endPlanet))
//		{
//			p = sookshmaIter.next();
//		}
//		
//		Iterator<Planet> pranaIter = vdList.iterator(planets[3]);
//		endPlanet =  Planet.getPlanetByOrdinal(planets[4].ordinal()-1);
//		p = pranaIter.next();
//		while (pranaIter.hasNext() && !p.equals(endPlanet))
//		{
//			p = pranaIter.next();
//		}
//		
//		while (mahaDasaIter.hasNext())
//		{
//			Planet mp = mahaDasaIter.next();
//		
//			if (buktiIter == null)
//			{
//				buktiIter = vdList.iterator(mp);
//			}
//			while (buktiIter.hasNext())
//			{
//				Planet bp = buktiIter.next();
//				
//				if (antaraIter == null)
//				{
//					antaraIter = vdList.iterator(bp);
//				}
//				while (antaraIter.hasNext())
//				{
//					Planet ap = antaraIter.next();
//					
//					if (sookshmaIter == null)
//					{
//						sookshmaIter = vdList.iterator(ap);
//					}
//					while (sookshmaIter.hasNext())
//					{
//						Planet sp = sookshmaIter.next();
//						
//						if (pranaIter == null)
//						{
//							pranaIter = vdList.iterator(sp);
//						}
//						while (pranaIter.hasNext())
//						{
//							Planet pp = pranaIter.next();
//
//							System.out.println(mp + "-" + bp + "-" + ap + "-" + sp + "-" + pp);
//						}
//						pranaIter = null;
//					}
//					sookshmaIter = null;
//				}
//				antaraIter = null;
//			}
//			buktiIter = null;
//		}

		System.out.println();
    }
    
    public static void test10()
    {
    	ArrayList<String> baseList = new ArrayList<String>(5);
    	baseList.add("A");
    	baseList.add("B");
    	baseList.add("C");
    	baseList.add("D");
    	baseList.add("E");
    	
    	CircularList<String> list1 = new CircularList<String>();
    	list1.addAll(baseList);
    	list1.setStartIndex(3);
    	
    	CircularList<String> list2 = new CircularList<String>(false);
    	list2.addAll(baseList);
    	list2.setStartIndex(3);
    	
    	CircularList<String> list3 = new CircularList<String>();
    	list3.addAll(baseList);
    	list3.setStartIndex(0);
    	
    	System.out.println("List1 items");
    	for(String s : list1)
    	{
    		System.out.println(s);
    	}
    	System.out.println("-----------------------\n");
    	System.out.println("List2 items");
    	for(String s : list2)
    	{
    		System.out.println(s);
    	}
    	
    	System.out.println("-----------------------\n");
    	System.out.println("List3 items");
    	int loop = 10;
    	int i = 0;
    	for (Iterator<String> iter = list3.infiniteIterator(); iter.hasNext(); i++)
    	{
    		if (i == loop)
    			break;
    		
    		System.out.println(iter.next());
    	}
    }
}