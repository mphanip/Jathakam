package mpp.jathakamu.utils;

import mpp.jathakamu.Constants.ASPECT_EFFECT;

public final class Utils
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
            monitor = null;
        }
        catch (java.lang.InterruptedException e)
        {
            // Ignore this exception
        }
    }
    
    public static ASPECT_EFFECT getAspectLevel(String str)
    {
        
        str = str.toUpperCase();
        int len = str.length();
        
        if (str.endsWith("GOOD"))
        {
            if (len == 4)
                return ASPECT_EFFECT.GOOD;
            if (len == "MODERATELY_GOOD".length())
                return ASPECT_EFFECT.MODERATELY_GOOD;
            if (len == "SLIGHTLY_GOOD".length())
                return ASPECT_EFFECT.SLIGHTLY_GOOD;
        }
        
        if (str.endsWith("BAD"))
        {
            if (len == 3)
                return ASPECT_EFFECT.BAD;
            if (len == "MODERATELY_BAD".length())
                return ASPECT_EFFECT.MODERATELY_BAD;
            if (len == "SLIGHTLY_BAD".length())
                return ASPECT_EFFECT.SLIGHTLY_BAD;
        }
        
        if (str.equals("CONJUNCTION"))
        {
            return ASPECT_EFFECT.CONJUNCTION;
        }
        
        return ASPECT_EFFECT.NONE;
    }
    
    /**
     * 
     * @param from
     *            starting value
     * @param to
     *            ending value
     * @param value
     *            value to be checked between from value and in value both
     *            inclusive.
     * @return returns true if valus is in between from and to values, both
     *         inclusive.
     */
    public static boolean valueIn(double from, double to, double value)
    {
        from = CalcUtils.degnorm(from);
        to = CalcUtils.degnorm(to);
        
        if (from > to)
        {
            if (value >= from && value <= 360.0)
                return true;
            
            if (value >= 0.0 && value <= to)
                return true;
            
            return false;
        }
        
        boolean flag = (value >= from && value <= to);

        return flag;
    }
}
