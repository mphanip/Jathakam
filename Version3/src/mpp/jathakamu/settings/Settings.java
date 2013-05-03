package mpp.jathakamu.settings;

import static swisseph.SweConst.SEFLG_SIDEREAL;
import static swisseph.SweConst.SEFLG_SPEED;
import static swisseph.SweConst.SE_SIDM_KRISHNAMURTI;
import static swisseph.SweConst.SE_SIDM_USER;
import static mpp.jathakamu.JathakamLogger.LOGGER;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;

import mpp.jathakamu.Constants;
import mpp.jathakamu.Constants.HORARY_NUMBER_SET;

import swisseph.SweDate;

/**
 * Contains Global setting like default time zone if time zone is not provided.
 * Orb aspects between planet to planet and planet to cusp/house.
 * 
 * @author Phani
 */
public final class Settings
{
    public final static String HOME = System.getProperty("JATHAKAMU_HOME", ".");
    public final static String CONFIG_DIR = HOME + File.separator + "config";
    public final static String CONFIG_DIR_WITH_SEPARATOR = CONFIG_DIR + File.separator;
    private final static String SETTINGS_FILE_NAME = CONFIG_DIR_WITH_SEPARATOR + "Settings.xml";
    
    private static Properties settingProps = new Properties();

    static
    {
        loadProperties();
    }
    
    public final static String DEFAULT_TIMEZONE = "IST";

    public final static String EPHEMERIS_FILES_PATH = System.getProperty(
            "EPHEMERIS_PATH", HOME + File.separator + "ephemeris");

    public static boolean USE_GEOCENTRIC_LATITUDE = true;

    /**
     * Should be one of the constant SE_SIDM_*. Default is SE_SIDM_KRISHNAMURTI
     */
    public static int AYANAMSA = SE_SIDM_KRISHNAMURTI;

    /**
     * When AYANAMSA is set to SE_SIDM_USER, ayan_t0 and ayan_initial_value
     * should be set
     */
    public static double ayan_t0 = 0.0D;
    public static double ayan_initial_value = 0.0D;

    public static int EPH_FLAGS = SEFLG_SPEED;

    public static int HOUSE_SYSTEM = Constants.HSYS_PLACIDUS;
    
    public static boolean TRUE_NODE = true;
    
    public static HORARY_NUMBER_SET HORARY_NUMBER_GROUP = HORARY_NUMBER_SET.HORARY_249;
    
    
    private static void loadProperties()
    {
        try
        {
            File file = new File(SETTINGS_FILE_NAME);
            
            if (!file.exists())
            {
                initializeDefaults();
                saveProperties();
                return;
            }
            
            InputStream in = new FileInputStream(SETTINGS_FILE_NAME);
            settingProps.loadFromXML(in);
        }
        catch (Exception e)
        {
            LOGGER.log(Level.WARNING, "Exception occured while reading Settings.xml, with the following stack trace", e);
        }
    }
    
    private static void initializeDefaults()
    {
        settingProps.setProperty("DEFAULT_TIME_ZINE", "IST");
        settingProps.setProperty("EPHEMERIS_FILES_PATH", ".");
        settingProps.setProperty("USE_GEOCENTRIC_LATITUDE", Boolean.toString(true));
        settingProps.setProperty("AYANAMSA", Integer.toString(SE_SIDM_KRISHNAMURTI));
        settingProps.setProperty("ayan_t0", Double.toString(0.0));
        settingProps.setProperty("ayan_initial_value", Double.toString(0.0));
        settingProps.setProperty("EPH_FLAGS", Integer.toString(SEFLG_SPEED));
        settingProps.setProperty("HOUSE_SYSTEM", Integer.toString(Constants.HSYS_PLACIDUS));
        settingProps.setProperty("TRUE_NODE", Boolean.toString(true));
        settingProps.setProperty("HORARY_NUMBER_SET", HORARY_NUMBER_SET.HORARY_249.toString());
    }

    private static void saveProperties()
    {
        try
        {
            OutputStream os = new FileOutputStream(SETTINGS_FILE_NAME);
            String comment = "File updated on " + (new Date());
            settingProps.storeToXML(os, comment, "UTF-8");
        }
        catch (Exception e)
        {
            LOGGER.log(Level.WARNING, "Exception occured while reading Settings.xml, with the following stack trace", e);
        }
    }

    public static boolean deductAyanamsa()
    {
        return ((EPH_FLAGS & SEFLG_SIDEREAL) == 0);
    }

    public static void setKPOldAynamsa()
    {
        setAynamsa(SweDate.getJulDay(1900, 1, 1, 0.0D), 22.362416666666665D);
    }

    public static void setKPNewAynamsa()
    {
        setAynamsa(SweDate.getJulDay(1900, 1, 1, 0.0D), 22.371027777777776D);
    }

    public static void setAynamsa(double t0, double initialValue)
    {
        AYANAMSA = SE_SIDM_USER;
        ayan_t0 = t0;
        ayan_initial_value = initialValue;
    }
}
