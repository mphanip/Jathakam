/* 
 * Yet to decide on the license
 */

package mpp.jathakamu.utils;

/**
 * Class to have all core utilities which can be used by any package
 * 
 * @author Phani
 */
public class Core
{
    public static void sleepInHours(long hrs)
    {
        sleepInMinutes(hrs * 60);
    }
    
    public static void sleepInMinutes(long mins)
    {
        sleepInSecs(mins * 60);
    }
    
    public static void sleepInSecs(long secs)
    {
        sleepInMillis(secs * 1000L);
    }
    
    public static void sleepInMillis(long millis)
    {
        try
        {
            Object monitor = new Object() {};
            synchronized (monitor)
            {
                monitor.wait(millis);
            }
        }
        catch (java.lang.InterruptedException e)
        {
            // Ignore this exception
        }
    }
    
    public static double normalize(double value)
    {
        double rtnValue = value;
        double roundedValue = Double.valueOf(value).longValue();
        double diff = value - roundedValue;
        
        // Check for values like 200.0000000001
        if (diff < 0.0000000001)
        {
            rtnValue = roundedValue;
        }
        
        // Check for values like 199.99999999999
        if ((1-diff) < 0.0000000001)
        {
            rtnValue = Math.ceil(value);
        }
        
        return rtnValue;
    }
}
