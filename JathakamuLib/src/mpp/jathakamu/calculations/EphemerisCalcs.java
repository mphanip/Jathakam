/* 
 * Yet to decide on the license
 */
package mpp.jathakamu.calculations;

import java.io.File;
import java.util.logging.Level;
import static mpp.jathakamu.JathakamLogger.LOGGER;

import mpp.jathakamu.GlobalSettings;
import mpp.jathakamu.JathakamuException;
import mpp.jathakamu.Profile;
import mpp.jathakamu.ProfileSettings;
import mpp.jathakamu.types.Degree;
import swisseph.DblObj;
import swisseph.SweConst;
import swisseph.SweDate;
import swisseph.SwissEph;

/**
 * All the calculations related to Swiss EphemerisCalcs (or java package)goes
 * here
 *
 * @author Phani
 */
public class EphemerisCalcs
{

    private final Profile profile;
    private final SwissEph swissEph;
    private final StringBuffer serr = new StringBuffer();
    private double ayanamsa;
    
    /* ascmc array is populated when calculating house/cusps */
    private final double []ascmc = new double[10];

    public EphemerisCalcs(Profile profile)
    {
        this.profile = profile;
        File ephFile = new File(GlobalSettings.EPHEMERIS_FILES_PATH);
        LOGGER.log(Level.INFO, "Using Swiss Ephmeris Files from location ''{0}''", ephFile.getAbsoluteFile().toString());
        swissEph = new SwissEph(GlobalSettings.EPHEMERIS_FILES_PATH);

        initializeEphemeris();
    }

    private void initializeEphemeris()
    {
        ProfileSettings settings = profile.getProfileSetting();
        String ver = swissEph.swe_version();

        LOGGER.log(Level.INFO, "Swiss Ephmeris Version is {0}", ver);
        
        setAyanamsa();
        
        double tjd_ut = profile.getJulianDayUT();
        
        ayanamsa = swissEph.swe_get_ayanamsa_ut(tjd_ut);
        LOGGER.log(Level.INFO, "Ayanamsa: {0}", new Degree(ayanamsa));
    }
    
    public String getPlanetName(int planet)
    {
        return swissEph.swe_get_planet_name(planet);
    }
    
    public double[] getPlanetDetails(int planet)
            throws JathakamuException
    {
        ProfileSettings settings = profile.getProfileSetting();
        String planetName = swissEph.swe_get_planet_name(planet);
        double tjd_ut = profile.getJulianDayUT();
        int ephFlags = settings.getEphemerisFlags();
        double[] xx = new double[6];
        
        setAyanamsa();

        LOGGER.log(Level.INFO, "Getting planet position for {0}", planetName);

        serr.setLength(0);
        printEphemerisFlags(ephFlags);
        LOGGER.log(Level.INFO, "Calling swe_calc_ut");
        int rtnFlag = swissEph.swe_calc_ut(tjd_ut, planet, ephFlags, xx,
                serr);

        if (rtnFlag == SweConst.ERR)
        {
            throw new JathakamuException(
                    "Error Occured while getting planet details for "
                    + planetName + "\nReturn status is " + rtnFlag
                    + "\n Error is " + serr);
        }
        
        double planetLongitude = xx[0];
        
        if (settings.deductAyanamsa())
        {
            planetLongitude = planetLongitude - ayanamsa;
        }
        planetLongitude = SupportCalcs.degnorm(planetLongitude);
        
        LOGGER.log(Level.INFO,
                "Planet longitude is {0} and is Retrograde? {1}",
                new Object[]
                {
                    planetLongitude, (xx[3] < 0)
                });

        return new double[]
        {
            planetLongitude, xx[3]
        };
    }

    /**
     * Calculate cusp or bhava position based
     * 
     * @return  returns 12 cusp positions.
     * @throws JathakamuException 
     */
    public double[] getCuspDetails()
            throws JathakamuException
    {
        ProfileSettings settings = profile.getProfileSetting();
        double tjd_ut = profile.getJulianDayUT();
        int ephFlags = settings.getEphemerisFlags();
        double geolat = profile.getLatitude();
        double geolong = profile.getLongitude();
        int hsys = settings.getHouseSystem();
        double[] cusp = new double[13];
        
        setAyanamsa();

        printEphemerisFlags(ephFlags);
        LOGGER.log(Level.INFO, "Calling swe_houses");
        int rtnFlag = swissEph.swe_houses(tjd_ut, ephFlags, geolat, geolong,
                hsys, cusp, ascmc);
        LOGGER.log(Level.INFO, "swe_houses returned with status {0}", rtnFlag);

        if (rtnFlag == SweConst.ERR)
        {
            throw new JathakamuException(
                    "Error Occured while getting cusp details. Return status is "
                    + rtnFlag + "\n Error is " + serr);
        }
        
        double []rtnCusp = new double[12];
        System.arraycopy(cusp, 1, rtnCusp, 0, 12);
        
        if (settings.deductAyanamsa())
        {
            for (int i = 0; i < 12; i++)
            {
                double cuspLongitude = rtnCusp[i];

                cuspLongitude = cuspLongitude - ayanamsa;
                rtnCusp[i] = SupportCalcs.degnorm(cuspLongitude);
            }
        }
        
        return rtnCusp;
    }

    public double getAyanamsa()
    {
        return ayanamsa;
    }
    
    public double getLagna()
    {
        return ascmc[SweConst.SE_ASC];
    }
    
    public double getMC()
    {
        return ascmc[SweConst.SE_MC];
    }
    
    public double getSidrealTime()
    {
        return ascmc[SweConst.SE_ARMC];
    }
    
    public double getVertex()
    {
        return ascmc[SweConst.SE_VERTEX];
    }
    
    public double getEquatorialAsc()
    {
        return ascmc[SweConst.SE_EQUASC];
    }
    
    public long[] getSunRiseTime()
            throws JathakamuException
    {
        return getSunRiseOrSet(SweConst.SE_CALC_RISE);
    }
    
    public long[] getSunSetTime()
            throws JathakamuException
    {
        return getSunRiseOrSet(SweConst.SE_CALC_SET);
    }
        
    /*
     * PRIVATE METHODS
     */
    
    private long[] getSunRiseOrSet(int flag)
            throws JathakamuException
    {
        DblObj rise = new DblObj();
        DblObj nextRise = new DblObj();
        int rtnFlag = swissEph.swe_rise_trans(profile.getJulianDayUT()-1, SweConst.SE_SUN, null,
                SweConst.SEFLG_SWIEPH, flag,
                new double[] {profile.getLongitude(), profile.getLatitude(), 10},
                0, 25, rise, serr);
        
        if (rtnFlag == SweConst.ERR)
        {
            throw new JathakamuException(
                    "Error Occured while calculating sun rise time. Return status is "
                    + rtnFlag + "\n Error is " + serr);
        }
        rtnFlag = swissEph.swe_rise_trans(profile.getJulianDayUT(), SweConst.SE_SUN, null,
                SweConst.SEFLG_SWIEPH, flag,
                new double[] {profile.getLongitude(), profile.getLatitude(), 10},
                0, 25, nextRise, serr);
        
        if (rtnFlag == SweConst.ERR)
        {
            throw new JathakamuException(
                    "Error Occured while calculating next day sun rise time. Return status is "
                    + rtnFlag + "\n Error is " + serr);
        }
        
        long []riseTimes = {SweDate.getDate(rise.val).getTime(), SweDate.getDate(nextRise.val).getTime()};
        
        return riseTimes;
    }
    
    private void setAyanamsa()
    {
        double[] ayan = profile.getProfileSetting().getAynamsa();

        swissEph.swe_set_sid_mode(Double.valueOf(ayan[0]).intValue(), ayan[1],
                ayan[2]);
    }

    /*
     * DEBUG METHODS
     */
    // Routine to print debug information about the Ephemeris flags used
    private void printEphemerisFlags(int flags)
    {
        LOGGER.log(Level.INFO,
                  ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"
                + "Following are the Ephemeris Flags used (true means the flag is set, otherwise false)\n"
                + "Speed = {0}\n"
                + "True Position = {1}\n"
                + "Topocentric = {2}\n"
                + "Sidreal = {3}\n"
                + "Heliocentric = {4}\n"
                + "No Abberation = {5}\n"
                + "No nutation = {6}\n"
                + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n",
                new Object[]
                {
                    ((flags & SweConst.SEFLG_SPEED) == SweConst.SEFLG_SPEED),
                    ((flags & SweConst.SEFLG_TRUEPOS) == SweConst.SEFLG_TRUEPOS),
                    ((flags & SweConst.SEFLG_TOPOCTR) == SweConst.SEFLG_TOPOCTR),
                    ((flags & SweConst.SEFLG_SIDEREAL) == SweConst.SEFLG_SIDEREAL),
                    ((flags & SweConst.SEFLG_HELCTR) == SweConst.SEFLG_HELCTR),
                    ((flags & SweConst.SEFLG_NOABERR) == SweConst.SEFLG_NOABERR),
                    ((flags & SweConst.SEFLG_NONUT) == SweConst.SEFLG_NONUT),
                });
    }
    
}
