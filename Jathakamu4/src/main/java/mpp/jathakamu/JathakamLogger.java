/* 
 * Yet to decide on the license
 */
package mpp.jathakamu;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Phani
 */
public final class JathakamLogger
{

    public static final Logger LOGGER = Logger.getLogger("Jathakamu");

    static
    {
        String sLogLevel = System.getProperty("LOG_LEVEL", "INFO");
        Level logLevel = getLevel(sLogLevel);
        setLoggerSettings(LOGGER, "Jathakamu.log", logLevel);
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
                return;
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(JathakamLogger.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        catch (SecurityException ex)
        {
            Logger.getLogger(JathakamLogger.class.getName()).log(Level.SEVERE,
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
