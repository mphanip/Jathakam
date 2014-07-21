/* 
 * Yet to decide on the license
 */
package mpp.jathakamu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import mpp.jathakamu.calculations.SupportCalcs;
import mpp.jathakamu.types.Degree;
import swisseph.SweConst;
import swisseph.SweDate;
import swisseph.SwissEph;

import static mpp.jathakamu.GlobalSettings.CONFIG_DIR_WITH_SEPARATOR;
import static mpp.jathakamu.GlobalSettings.saveProperties;
import static mpp.jathakamu.JathakamLogger.LOGGER;
import static swisseph.SweConst.SEFLG_SWIEPH;
import static swisseph.SweConst.SEFLG_SIDEREAL;
import static swisseph.SweConst.SEFLG_SPEED;
import static swisseph.SweConst.SE_HSYS_PLACIDUS;
import static swisseph.SweConst.SE_SIDM_KRISHNAMURTI;
import static swisseph.SweConst.SE_SIDM_USER;

/**
 * These setting are per instance (may user). These settings are intended to use
 * with each instance of SwissEph
 *
 * @author Phani
 */
public final class ProfileSettings
    implements Serializable
{
    /**
     * This settings are unique to the profile.
     */
    private final String ID = UUID.randomUUID().toString();

    private double oneYear = 365.2425D;

    private boolean useGeocentric = true;

    /**
     * Should be one of the constant swisseph.SweConst.SE_SIDM_*. Default is
     * SE_SIDM_KRISHNAMURTI
     */
    private int ayanamsa = SE_SIDM_KRISHNAMURTI;

    /**
     * When AYANAMSA is set to SE_SIDM_USER, ayan_t0 and ayan_initial_value
     * should be set
     */
    private double ayan_t0 = 0.0D;
    private double ayan_initial_value = 0.0D;

    /**
     * Ephemeris flags to use. Default is set to get retrograde planets, use 
     * swiss ephemeris and sidereal positions
     */
    private int ephemerisFlags = SEFLG_SWIEPH | SEFLG_SPEED | SEFLG_SIDEREAL;

    /**
     * Default is Placidus system. This will not change mostly, but just in case
     * if someone needs this value changed.
     */
    private int houseSystem = SE_HSYS_PLACIDUS;

    /**
     * Value of nodes (Rahu and Ketu) computation, required for Swiss Ephemeris.
     */
    private boolean trueNode = true;

    /**
     * Used for Horary charts. Default NONE means it is Natal chart.
     */
    private Constants.HORARY_NUMBER_SET horaryNumberGroup = Constants.HORARY_NUMBER_SET.HORARY_NONE;

    /**
     * Profile settings in Properties hash map.
     */
    private final Properties settingProps = new Properties();

    /**
     * File name to store user specific settings
     */
    private final String fileName = CONFIG_DIR_WITH_SEPARATOR + ID + "-Settings.xml";

    public ProfileSettings()
    {
        loadSettings();
    }

    private void loadSettings()
    {
        try
        {
            File file = new File(fileName);
            LOGGER.log(Level.INFO, "Profile Setting File Name: {0}",
                    file.getAbsolutePath());
            if (!file.exists())
            {
                File dirName = file.getParentFile();
                if (!dirName.exists())
                {
                    boolean mkdirFlag = dirName.mkdirs();
                    if (!mkdirFlag)
                    {
                        LOGGER.log(Level.INFO, "Profile Setting: mkdirs {0} failed",
                                dirName.getAbsolutePath());
                    }
                }
                initializeDefaults();
                saveProperties();
                return;
            }

            InputStream in = new FileInputStream(fileName);
            settingProps.loadFromXML(in);
        }
        catch (IOException e)
        {
            LOGGER.log(Level.WARNING,
                    "Exception occured while reading Settings.xml, with the following stack trace",
                    e);
        }
    }

    private void initializeDefaults()
    {
        settingProps.setProperty("USE_GEOCENTRIC_LATITUDE", Boolean.toString(
                true));
        settingProps.setProperty("AYANAMSA", Integer.toString(
                SE_SIDM_KRISHNAMURTI));
        settingProps.setProperty("ayan_t0", Double.toString(0.0));
        settingProps.setProperty("ayan_initial_value", Double.toString(0.0));
        settingProps.setProperty("EPH_FLAGS", Integer.toString(SEFLG_SWIEPH | SEFLG_SPEED | SEFLG_SIDEREAL));
        settingProps.setProperty("HOUSE_SYSTEM", Integer.toString(
                SE_HSYS_PLACIDUS));
        settingProps.setProperty("TRUE_NODE", Boolean.toString(true));
        settingProps.setProperty("HORARY_NUMBER_SET",
                Constants.HORARY_NUMBER_SET.HORARY_249.toString());
        settingProps.setProperty("ONE_YEAR",
                String.valueOf(oneYear));
    }

    /**
     * Call this method to persist settings.
     */
    public void saveProperties()
    {
        try
        {
            OutputStream os = new FileOutputStream(fileName);
            String comment = "File updated on " + (new Date());
            settingProps.storeToXML(os, comment, "UTF-8");
        }
        catch (IOException e)
        {
            LOGGER.log(Level.WARNING,
                    "Exception occured while reading Settings.xml, with the following stack trace",
                    e);
        }
    }

    public boolean deductAyanamsa()
    {
        return !((ephemerisFlags & SEFLG_SIDEREAL) == SEFLG_SIDEREAL);
    }

    /**
     * set KP Old Ayanamsa. Swiss Ephemeris does not have this option.
     */
    public void setKPOldAynamsa()
    {
        setAynamsa(SweDate.getJulDay(1900, 1, 1, 0.0D), 22.362416666666665D);
    }

    /**
     * Looks like there is some difference in swiss ephemeris calculation and
     * this one.
     */
    public void setKPNewAynamsa()
    {
        setAynamsa(SweDate.getJulDay(1900, 1, 1, 0.0D), 22.37045D /*22.371027777777776D */);
    }
    
    public void setPushyaPakshaAyanamsa()
            throws JathakamuException
    {
        SwissEph swissEph = new SwissEph(GlobalSettings.EPHEMERIS_FILES_PATH);
        double jd_ut = SweDate.getJulDay(2000, 1, 1, 0.5);
//        double jd_ut = SupportCalcs.getJulianDay(1973, 4, 4, 8, 59, 8, 0,
//                "Asia/Calcutta");
        double[] xx = new double[6];
        StringBuffer serr = new StringBuffer();
        int iFlags = SweConst.SEFLG_SWIEPH;
        int seFlag = swissEph.swe_fixstar_ut(new StringBuffer("Asellus Australis"), jd_ut, iFlags, xx, serr);

        if (seFlag == SweConst.ERR)
        {
            throw new JathakamuException("Failed to set Pushya Paksha Ayanamsa - Error: " + serr);
        }
        
        double starLongitude = xx[0];
        
        LOGGER.log(Level.INFO, "Delta Cancri star longitude = {0}", new Degree(starLongitude));
        
        double ppAyanamasa = SupportCalcs.degnorm(starLongitude - 106.0);
        
        LOGGER.log(Level.INFO, "Pushya Paksha Ayanamsa is {0}", new Degree(ppAyanamasa));
        
        setAynamsa(jd_ut, ppAyanamasa);
    }

    public void setaynamsa(int ayan)
    {
        this.ayanamsa = ayan;
        saveProperties();
    }

    public void setAynamsa(double t0, double initialValue)
    {
        ayanamsa = SE_SIDM_USER;
        ayan_t0 = t0;
        ayan_initial_value = initialValue;
        saveProperties();
    }
    
    public String getID()
    {
        return ID;
    }

    public double[] getAynamsa()
    {
        return new double[]
        {
            ayanamsa, ayan_t0, ayan_initial_value
        };
    }

    public double getOneYear()
    {
        return oneYear;
    }

    public void setOneYear(double oneYear)
    {
        this.oneYear = oneYear;
        saveProperties();
    }

    public boolean isUseGeocentric()
    {
        return useGeocentric;
    }

    public void setUseGeocentric(boolean useGeocentric)
    {
        this.useGeocentric = useGeocentric;
        saveProperties();
    }

    public int getEphemerisFlags()
    {
        return ephemerisFlags;
    }

    public void setEphemerisFlags(int ephemerisFlags)
    {
        this.ephemerisFlags = ephemerisFlags;
        saveProperties();
    }

    public int getHouseSystem()
    {
        return houseSystem;
    }

    public void setHouseSystem(int houseSystem)
    {
        this.houseSystem = houseSystem;
        saveProperties();
    }

    public boolean isTrueNode()
    {
        return trueNode;
    }

    public void setTrueNode(boolean trueNode)
    {
        this.trueNode = trueNode;
        saveProperties();
    }

    public Constants.HORARY_NUMBER_SET getHoraryNumberGroup()
    {
        return horaryNumberGroup;
    }

    public void setHoraryNumberGroup(
            Constants.HORARY_NUMBER_SET horaryNumberGroup)
    {
        this.horaryNumberGroup = horaryNumberGroup;
        saveProperties();
    }
}
