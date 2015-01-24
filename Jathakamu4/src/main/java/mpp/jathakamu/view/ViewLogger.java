/* 
 * Yet to decide on the license
 */
package mpp.jathakamu.view;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author phani
 */
public class ViewLogger
{
    public static final Logger LOGGER = Logger.getLogger("Jathakamu");

    static
    {
        String sLogLevel = System.getProperty("LOG_LEVEL", "WARNING");
        Level logLevel = getLevel(sLogLevel);
        setLoggerSettings(LOGGER, "JathakamuView.log", logLevel);
    }
    
    public static void setLoggerSettings(Logger logger, String logFileName,
            Level logLevel)
    {
        try
        {
            FileHandler hand = new FileHandler(logFileName);
            SimpleFormatter formatter = new SimpleFormatter();
            hand.setFormatter(formatter);

            logger.addHandler(hand);

            if (logLevel != null)
            {
                logger.setLevel(logLevel);
            }
        }
        catch (IOException | SecurityException ex)
        {
            Logger.getLogger(ViewLogger.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    private static Level getLevel(String logLevel)
    {
        if (logLevel.equalsIgnoreCase("OFF"))
        {
            return Level.OFF;
        }
        else if (logLevel.equalsIgnoreCase("SEVERE"))
        {
            return Level.SEVERE;
        }
        else if (logLevel.equalsIgnoreCase("WARNING"))
        {
            return Level.WARNING;
        }
        else if (logLevel.equalsIgnoreCase("INFO"))
        {
            return Level.FINE;
        }
        else if (logLevel.equalsIgnoreCase("FINE"))
        {
            return Level.FINE;
        }
        else if (logLevel.equalsIgnoreCase("FINER"))
        {
            return Level.FINER;
        }
        else if (logLevel.equalsIgnoreCase("FINEST"))
        {
            return Level.FINEST;
        }
        else if (logLevel.equalsIgnoreCase("ALL"))
        {
            return Level.ALL;
        }

        return Level.INFO;
    }
}
