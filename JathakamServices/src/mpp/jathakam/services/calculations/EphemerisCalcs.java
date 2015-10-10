/* 
 * Yet to decide on the license
 */
package mpp.jathakam.services.calculations;

import java.util.logging.Level;
import mpp.jathakam.ephimeris.EphemerisResources;
import mpp.jathakam.services.JathakamuException;
import mpp.jathakam.services.Profile;
import mpp.jathakam.services.ProfileSettings;
import mpp.jathakam.services.types.AYANAMSA;

import swisseph.SweConst;
import swisseph.SwissEph;

import static mpp.jathakam.services.JathakamLogger.LOGGER;

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
        swissEph = new SwissEph(EphemerisResources.getEphemerisPath());

        initializeEphemeris();
    }

    private void initializeEphemeris()
    {
        ProfileSettings settings = profile.getProfileSetting();
        String ver = swissEph.swe_version();

        LOGGER.log(Level.INFO, "Swiss Ephmeris Version is {0}", ver);

        AYANAMSA enumAyan = settings.getAyanamsa();

        if (enumAyan.isUserDefined()) {
            double[] ayan = settings.getUserDefinedAynamsa();
            swissEph.swe_set_sid_mode(Double.valueOf(ayan[0]).intValue(), ayan[1],
                    ayan[2]);
        } else {
            swissEph.swe_set_sid_mode(enumAyan.getValue());
        }

        double tjd_ut = profile.getDateTime();
        ayanamsa = swissEph.swe_get_ayanamsa_ut(tjd_ut);
        LOGGER.log(Level.INFO, "Ayanamsa: {0}", ayanamsa);
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
        double tjd_ut = profile.getDateTime();
        int ephFlags = settings.getEphemerisFlags();
        double[] xx = new double[6];

        LOGGER.log(Level.INFO, "Getting planet position for {0}", planetName);

        serr.setLength(0);
        printEphemerisFlags(ephFlags);
        LOGGER.log(Level.INFO, "Calling swe_calc_ut");
        int rtnFlag = swissEph.swe_calc_ut(tjd_ut, planet, ephFlags, xx,
                serr);

        LOGGER.log(Level.INFO,
                "Planet {2} longitude is {0} and is Retrograde? {1}",
                new Object[]
                {
                    xx[0], (xx[3] < 0), planetName
                });

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

        return new double[]
        {
            planetLongitude, xx[3]
        };
    }

    public double[] getCuspDetails()
            throws JathakamuException
    {
        ProfileSettings settings = profile.getProfileSetting();
        double tjd_ut = profile.getDateTime();
        int ephFlags = settings.getEphemerisFlags();
        double geolat = profile.getLatitude();
        double geolong = profile.getLongitude();
        int hsys = settings.getHouseSystem();
        double[] cusp = new double[13];

        printEphemerisFlags(ephFlags);
        LOGGER.log(Level.INFO, "Calling swe_houses");
        int rtnFlag = swissEph.swe_houses(tjd_ut, ephFlags, geolat, geolong,
                hsys, cusp, ascmc);

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
    
    /*
     * PRIVATE METHODS
     */
    
    /*
     * DEBUG METHODS
     */
    // Routine to print debug information about the Ephemeris flags used
    private void printEphemerisFlags(int flags)
    {
        LOGGER.log(Level.FINEST, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"
                + "Following are the Ephemeris Flags used (true means the flag is set, otherwise false)\n"
                + "Speed = {0}\n"
                + "True Position = {1}\n"
                + "Topocentric = {2}\n"
                + "Sidreal = {3}\n"
                + "Heliocentric = {4}\n"
                + "No Abberation = {5}\n"
                + "No nutation = {6}\n"
                + "########################################\n",
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
