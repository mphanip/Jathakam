/*
 *
 *
 */
package mpp.jathakam.core;

import static mpp.jathakam.core.JathakamConstants.SEFLG_SIDEREAL;
import static mpp.jathakam.core.JathakamConstants.SEFLG_SPEED;
import static mpp.jathakam.utils.JatakamUtilities.NUM_FORMAT;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.tree.TreeNode;

import mpp.jathakam.JatakamException;
import mpp.jathakam.core.vd.VimshottariDasa;
import mpp.jathakam.utils.JatakamUtilities;
import swisseph.SweConst;
import swisseph.SweDate;
import swisseph.SwissEph;
import swisseph.SwissLib;

/**
 *
 * @author Phani
 */
public class JathakamMain
{
    public static final Logger LOGGER = Logger.getLogger(JathakamMain.class.getName());

    static
    {
        String logFolder = System.getProperty("java.io.tmpdir", ".");
        if (!logFolder.endsWith(File.separator))
        {
        	logFolder = logFolder + File.separator;
        }
        String logFileName = logFolder + "Jatakam.log";
        System.out.println("Jathakam Log Filename: " + logFileName);
        JatakamUtilities.setLoggerSettings(LOGGER, logFileName, null);
    }

    private int ayanamsa = JathakamConstants.AYANAMSA.SE_SIDM_KRISHNAMURTI.ordinal();
    private double ayan_t0 = 0.0D;
    private double ayan_initial_value = 0.0D;
    private int houseSystem = 80;
    private int iflag = SEFLG_SPEED;
    private StringBuffer serr = new StringBuffer();
    private BirthDateTime birthDateTime = null;
    private PlaceInfo placeInfo = null;
    private SwissLib swephLib;
    private List<Double> cuspPositions = new ArrayList<Double>();
    private Map<Raasi, List<Integer>> raasiToCuspMap = new ConcurrentHashMap<Raasi, List<Integer>>(12);
    private Map<Integer, Raasi> cuspToRaasiMap = new ConcurrentHashMap<Integer, Raasi>(12);
    private Map<Integer, List<Integer>> cuspToPlanetMap = null;
    private TreeNode vimshottariTree = null;

    public final String getErrString()
    {
        return serr.toString();
    }

    public JathakamMain()
    {
    	swephLib = new SwissLib();
    }

    public JathakamMain(String path)
    {
        swephLib = new SwissLib();
    }

    public JathakamMain(String ephemerisPath, int ayan)
    {
        this(ephemerisPath);
        ayanamsa = ayan;
    }

    public JathakamMain(int ayan)
    {
        this();
        ayanamsa = ayan;
    }

    public JathakamMain(String ephemerisPath, int ayanamsa, int hsys)
    {
        this(ephemerisPath, ayanamsa);
        houseSystem = hsys;
    }

    public JathakamMain(int ayanamsa, int hsys)
    {
        this(ayanamsa);
        houseSystem = hsys;
    }

    public JathakamMain(String ephemerisPath, int ayanamsa, int houseSystem, int flag)
    {
        this(ephemerisPath, ayanamsa, houseSystem);
        iflag = flag;
    }

    public JathakamMain(int ayanamsa, int houseSystem, int flag)
    {
        this(ayanamsa, houseSystem);
        iflag = flag;
    }

    public JathakamMain(BirthDateTime bdt, int ayanamsa, int houseSystem, int flag)
    {
        this(ayanamsa, houseSystem, flag);
        birthDateTime = bdt;
    }

    public JathakamMain(BirthDateTime bdt)
    {
        birthDateTime = bdt;
    }

    public JathakamMain(PlaceInfo pi, BirthDateTime bdt, int ayanamsa, int houseSystem, int flag)
    {
        this(ayanamsa, houseSystem, flag);
        birthDateTime = bdt;
    }

    public JathakamMain(BirthDateTime bdt, PlaceInfo pi)
    {
        birthDateTime = bdt;
        placeInfo = pi;
    }

    public void setBirthDateTime(BirthDateTime bdt)
    {
        birthDateTime = bdt;
    }

    public void setPlaceInfo(PlaceInfo pi)
    {
        placeInfo = pi;
    }

    public void setHouseSystem(int hsys)
    {
        houseSystem = hsys;
    }

    public void setCustomAyanamsa(double t0, double ayan_value_t0)
    {
        ayanamsa = JathakamConstants.AYANAMSA.SE_SIDM_USER.getOrdinal();
        ayan_t0 = t0;
        ayan_initial_value = ayan_value_t0;
    }

    public void setCalcFlags(int flag)
    {
        iflag = flag;
        LOGGER.log(Level.FINE, "Setting calculation flags to {0}", iflag);
    }

    public void addCalcFlags(int flag)
    {
        iflag |= flag;
        LOGGER.log(Level.FINE, "Setting calculation flags to {0}", iflag);
    }

    public void setKPOldAynamsa()
    {
        ayanamsa = JathakamConstants.AYANAMSA.SE_SIDM_USER.getOrdinal();
        ayan_t0 = SweDate.getJulDay(1900, 1, 1, 0.0D);
        ayan_initial_value = 22.362416666666665D;
    }

    public void setKPNewAynamsa()
    {
        ayanamsa = JathakamConstants.AYANAMSA.SE_SIDM_USER.getOrdinal();
        ayan_t0 = SweDate.getJulDay(1900, 1, 1, 0.0D);
        ayan_initial_value = 22.371027777777776D;
    }

    public List<PlanetCoordinates> getPlanetInfo()
        throws JatakamException
    {
        if (birthDateTime == null)
        {
            throw new JatakamException("Birth date and time information not available");
        }

        if (placeInfo == null)
        {
            throw new JatakamException("Longitude and latitude information not available");
        }

        return getPlanetInfo(birthDateTime, placeInfo);
    }
    
    public List<PlanetCoordinates> getPlanetInfo(BirthDateTime bdt, PlaceInfo pi)
            throws JatakamException
    {
    	return getPlanetInfo(bdt, pi, true);
    }
    
    public List<PlanetCoordinates> getPlanetInfo(BirthDateTime bdt, PlaceInfo pi, boolean trueNode)
        throws JatakamException
    {
        List<PlanetCoordinates> planetsInfo = new ArrayList<PlanetCoordinates>();
        LOGGER.log(Level.FINE, "Given birth date time : {0}", bdt);
        LOGGER.log(Level.FINE, "Given Place : {0}", pi);
        
        SwissEph sweph = getSwissEphemeris();

        double tjd_ut = JatakamUtilities.getJulianDay_UT(bdt, pi, true);
        LOGGER.log(Level.FINE, "Julian day is {0}", NUM_FORMAT.format(tjd_ut));

        double ayan = sweph.swe_get_ayanamsa_ut(tjd_ut);
        LOGGER.log(Level.FINE, "Ayanamsa : {0}", JatakamUtilities.toStringDegree(ayan));

        boolean deductAyanamsa = ((iflag & SEFLG_SIDEREAL) == 0);
        LOGGER.log(Level.FINE, "Will Ayanamsa be deducted from ephemeris values? {0}", deductAyanamsa);

        for (int i = SweConst.SE_SUN; i <= SweConst.SE_PLUTO; i++)
        {
        	serr.delete(0, serr.length());
            double[] xx = new double[6];
            int rtnFlag = sweph.swe_calc_ut(tjd_ut, i, iflag, xx, serr);

            LOGGER.log(Level.FINER, "Returned Flag is {0}, and iFlag was {1}", new Object[] {rtnFlag, iflag});
            logError();

            if (getErrString().trim().length() > 0)
            {
                LOGGER.log(Level.WARNING, "Error Occured {0}", getErrString());
            }

            PlanetCoordinates graha = getPlanetCoordinates(i, deductAyanamsa, ayan, xx);

            planetsInfo.add(graha);
        }
        
        serr.delete(0, serr.length());
        double[] xx = new double[6];
        int rtnFlag;
        if (trueNode)
        {
        	rtnFlag = sweph.swe_calc_ut(tjd_ut, SweConst.SE_TRUE_NODE, iflag, xx, serr);
        }
        else
        {
        	rtnFlag = sweph.swe_calc_ut(tjd_ut, SweConst.SE_MEAN_NODE, iflag, xx, serr);
        }
        LOGGER.log(Level.FINER, "Returned Flag is {0}, and iFlag was {1}", new Object[] {rtnFlag, iflag});
        logError();

        PlanetCoordinates graha = getPlanetCoordinates(Planet.RAHU.getIndex(), deductAyanamsa, ayan, xx);
        planetsInfo.add(graha);
        
        double ketuLon = graha.getLongitude() + 180.0D;
        ketuLon = swephLib.swe_degnorm(ketuLon);
        graha = new PlanetCoordinates(Planet.KETU.getIndex(), ketuLon, 0D, 0D, -1D, 0D, 0D);
        planetsInfo.add(graha);

        sweph.swe_close();
        
        double moonLongitude = planetsInfo.get(SweConst.SE_MOON).getLongitude();
        VimshottariDasa vd = new VimshottariDasa(moonLongitude);
        vimshottariTree = vd.getVDTree(bdt.getTimeInMillis());

        return planetsInfo;
    }

    public List<HouseInfo> getHouses()
        throws JatakamException
    {
        if (birthDateTime == null)
        {
            throw new JatakamException("Birth date and time information not available");
        }

        if (placeInfo == null)
        {
            throw new JatakamException("Longitude and latitude information not available");
        }

        return getHouses(birthDateTime, placeInfo, houseSystem);
    }

    public List<HouseInfo> getHouses(int hsys)
        throws JatakamException
    {
        if (birthDateTime == null)
        {
            throw new JatakamException("Birth date and time information not available");
        }

        if (placeInfo == null)
        {
            throw new JatakamException("Longitude and latitude information not available");
        }

        return getHouses(birthDateTime, placeInfo, hsys);
    }

    public List<HouseInfo> getHouses(int year, int month, int day, double time, PlaceInfo pi, int hsys)
    {
        BirthDateTime bdt = new BirthDateTime(year, month, day, time);
        return getHouses(bdt, pi, hsys);
    }

    public List<HouseInfo> getHouses(BirthDateTime bdt, PlaceInfo pi, int hsys)
    {
        List<HouseInfo> houseInfoList = new ArrayList<HouseInfo>(12);

        SwissEph sweph = getSwissEphemeris();
        
//        BirthDateTime bdt_lmt = JatakamUtilities.getLMT(bdt, pi);
//        LOGGER.log(Level.FINE, "Local mean time {0}", bdt_lmt);

        double tjd_ut = JatakamUtilities.getJulianDay_UT(bdt, pi, true);

        LOGGER.log(Level.FINE, "Julian day is {0}", NUM_FORMAT.format(tjd_ut));

        double ayan = sweph.swe_get_ayanamsa_ut(tjd_ut);
        LOGGER.log(Level.FINE, "Ayanamsa : {0}", JatakamUtilities.toStringDegree(ayan));

        double geolat = pi.getLatitude();
        double geoCenLat = JatakamUtilities.getGeocentricLatitude(geolat);
        double geolon = pi.getLogitude();
        double[] cusps = new double[37];
        double[] ascmc = new double[10];

        LOGGER.log(Level.FINE, "Geocentric Latitude : {0}", JatakamUtilities.toStringDegree(geoCenLat));

        int rtnFlag = sweph.swe_houses(tjd_ut, iflag,
                                                 geoCenLat, geolon, hsys,
                                                 cusps, ascmc);

        LOGGER.log(Level.FINE, "Sidereal Time : {0}", JatakamUtilities.toStringDegree(ascmc[2]/15.0D));

        if (rtnFlag != 0)
        {
            LOGGER.log(Level.WARNING, "Error occured!!! Error code is {0}", rtnFlag);
        }

        int i = 0;
        for (double cusp : cusps)
        {
            if (cusp == 0.0D)
            {
                continue;
            }
            i++;
            cusp -= ayan;
            cusp = swephLib.swe_degnorm(cusp);
            HouseInfo h = new HouseInfo("House " + i, cusp);
            houseInfoList.add(h);
            cuspPositions.add(cusp);
            
            Raasi cuspRaasi = Raasi.getRaasi(cusp);
            List<Integer> cuspList = raasiToCuspMap.get(cuspRaasi);
            if (cuspList == null)
            {
            	cuspList = new ArrayList<Integer>();
            	raasiToCuspMap.put(cuspRaasi, cuspList);
            }
            cuspToRaasiMap.put(i, cuspRaasi);
            cuspList.add(i);
        }

        sweph.swe_close();

        return houseInfoList;
    }
    
    public List<Double> getCuspPositions()
    {
    	return new ArrayList<Double>(cuspPositions);
    }
    
    public int getPlanetCusp(double planetPosition)
    {
    	int n = cuspPositions.size();
    	int planetIndex = -1;
    	
    	for (int curIndex = 0; curIndex < n; curIndex++)
    	{
    		int nextIndex = curIndex+1;
    		if (curIndex == n-1)
    		{
    			nextIndex = 0;
    		}
    		double curCuspPos = cuspPositions.get(curIndex);
    		double nextCuspPos = cuspPositions.get(nextIndex);
    		
    		if (nextCuspPos < curCuspPos)
    		{
    			// which means that next cusp is in or after mesha(Aries) raasi
    			nextCuspPos += 360D;
    		}
    		
    		if (planetPosition >= curCuspPos && planetPosition <= nextCuspPos)
    		{
    			return (curIndex+1);
    		}
    	}

    	return planetIndex;
    }

	public List<Integer> getCuspsInRaasi(Raasi raasi)
    {
    	List<Integer> cuspsList = raasiToCuspMap.get(raasi);
    	if (cuspsList == null)
    	{
    		return Collections.emptyList();
    	}
    	
    	return cuspsList;
    }
    
    public Map<Planet, Integer> getAllPlanetsCuspPosition(List<PlanetCoordinates> planetsList)
    {
    	Map<Planet, Integer> planetsCupsPosition = new HashMap<Planet, Integer>(12);
    	
    	for (PlanetCoordinates planetCoord : planetsList)
    	{
    		Planet planet = Planet.getPlanet(planetCoord.getIndex());
    		if (planet == null)
    		{
    			continue;
    		}
    		Integer cusp = getPlanetCusp(planetCoord.getLongitude());
    		planetsCupsPosition.put(planet, cusp);
    	}
    	
    	return planetsCupsPosition;
    }
    
    public List<Integer> getPlanetsInCusp(int cusp, List<PlanetCoordinates> planetsCoordinates)
    {
    	if (cuspToPlanetMap == null)
    	{
    		cuspToPlanetMap = new ConcurrentHashMap<Integer, List<Integer>>(12);
    		
    		for (int i = 1; i <= 12; i++)
    		{
		    	List<Integer> planetsList = new ArrayList<Integer>();
		    	for (PlanetCoordinates planetCoord : planetsCoordinates)
		    	{
		    		int planetInCusp = getPlanetCusp(planetCoord.getLongitude());
		    		if (planetInCusp == i)
		    		{
		    			planetsList.add(planetCoord.getIndex());
		    		}
		    	}
		    	
		    	cuspToPlanetMap.put(i, planetsList);
    		}
    	}
    	
    	List<Integer> rtnList = cuspToPlanetMap.get(cusp);
    	
    	if (rtnList == null)
    	{
    		return Collections.emptyList();
    	}
    	
    	return rtnList;
    }
    
    public Raasi getCuspRaasi(int cusp)
    {
    	return cuspToRaasiMap.get(cusp);
    }
    
    public TreeNode getVDTree()
    {
    	return vimshottariTree;
    }
    
    public double getAyanamsa()
    {
        double tjd_ut = JatakamUtilities.getJulianDay_UT(birthDateTime, placeInfo, true);
        SwissEph se = getSwissEphemeris();
        double ayanamsa = se.swe_get_ayanamsa_ut(tjd_ut);
        return ayanamsa;
    }
    
    private SwissEph getSwissEphemeris()
    {
    	SwissEph sweph = new SwissEph(System.getProperty("EPHEMERIS_PATH", "."));
    	String ver = sweph.swe_version();
        LOGGER.log(Level.FINEST, "Swiss Ephmeris Version is {0}", ver);
        
        sweph.swe_set_sid_mode(ayanamsa, ayan_t0, ayan_initial_value);

        return sweph;
    }
    
    private PlanetCoordinates getPlanetCoordinates(int planetIndex, boolean deductAyanamsa, double ayan, double []xx)
    {
    	double geoLon = xx[0];

        if (deductAyanamsa)
        {
            geoLon = geoLon - ayan;
        }

        geoLon = swephLib.swe_degnorm(geoLon);
        
        PlanetCoordinates graha = new PlanetCoordinates(planetIndex, geoLon, xx[1],
            xx[2], xx[3], xx[4], xx[5]);
    	return graha;
    }
    
    private void logError()
    {
    	if (serr.length() > 0)
        {
        	LOGGER.log(Level.SEVERE, "Error occured while calulating the planet position {0}", serr.toString());
        }
    }
}
