/* 
 * Yet to decide on the license
 */
package mpp.jathakamu;


import static mpp.jathakamu.JathakamLogger.LOGGER;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import mpp.jathakamu.types.Place;


/**
 * Contains Global setting like Ephemeris path.
 *
 * @author Phani
 */
public final class GlobalSettings
{

    public final static String HOME = System.getProperty("JATHAKAMU_HOME", ".");
    public final static String CONFIG_DIR = HOME + File.separator + "config";
    public final static String CONFIG_DIR_WITH_SEPARATOR = CONFIG_DIR + File.separator;
    private final static String SETTINGS_FILE_NAME = CONFIG_DIR_WITH_SEPARATOR + "GlobalSettings.xml";
    private final static Properties settingProps = new Properties();

    static
    {
        loadProperties();
    }

    public final static String DEFAULT_TIMEZONE = "Asia/Calcutta";

    public final static String EPHEMERIS_FILES_PATH = System.getProperty(
            "EPHEMERIS_PATH", HOME + File.separator + "ephemeris");
    
    public static Place astrologerPlace = new Place.PlaceBuilder().values("Bengaluru", 77.5833D, 12.9833D, Constants.DEFAULT_TIME_ZONE).build();
    
    private static void loadProperties()
    {
        try
        {
            File file = new File(SETTINGS_FILE_NAME);
            LOGGER.log(Level.INFO, "Global Setting File Name: {0}",
                    file.getAbsolutePath());

            if (!file.exists())
            {
                File dirName = file.getParentFile();
                if (!dirName.exists())
                {
                    boolean mkdirFlag = dirName.mkdirs();
                    if (!mkdirFlag)
                    {
                        LOGGER.log(Level.INFO, "Global setting mkdirs {0} failed",
                                dirName.getAbsolutePath());
                    }
                }
                initializeDefaults();
                saveProperties();
                return;
            }

            InputStream in = new FileInputStream(SETTINGS_FILE_NAME);
            settingProps.loadFromXML(in);
        }
        catch (IOException e)
        {
            LOGGER.log(Level.WARNING,
                    "Exception occured while reading Settings.xml, with the following stack trace",
                    e);
        }
    }

    private static void initializeDefaults()
    {
        settingProps.setProperty("DEFAULT_TIME_ZONE", "IST");
        settingProps.setProperty("EPHEMERIS_FILES_PATH", ".");
    }

    /**
     * Call this method to persist settings.
     */
    public static void saveProperties()
    {
        try
        {
            OutputStream os = new FileOutputStream(SETTINGS_FILE_NAME);
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
}
