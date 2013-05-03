package mpp.jathakamu;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import mpp.jathakamu.utils.CalcUtils;

public final class JathakamLogger
{

    public static final Logger LOGGER = Logger.getLogger("Jathakamu");

    static
    {
        String sLogLevel = System.getProperty("LOG_LEVEL", "INFO");
        Level logLevel = getLevel(sLogLevel);
//        String logFileName = System.getProperty("java.io.tmpdir", "/tmp");
//        File logFile = new File(logFileName);
//        if (!logFile.exists())
//        {
//            logFile.mkdirs();
//        }
        setLoggerSettings(LOGGER, "Jathakamu.log", logLevel);
    }

    public static void setLoggerSettings(Logger logger, String logFileName, Level logLevel)
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
            Logger.getLogger(CalcUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SecurityException ex)
        {
        	Logger.getLogger(CalcUtils.class.getName()).log(Level.SEVERE, null, ex);
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
