package mpp.jatakamu.utils;

import swisseph.SwissLib;


/**
 *
 * @author phani
 */
public final class CalcUtils
{
    final static SwissLib swephLib = new SwissLib();
    
    public static int getZodiacSign(double position)
    {
        if (position > 360.0 || position < 0.0)
        {
            position = swephLib.swe_degnorm(position);
        }

        if (position >= 0.0 && position <= 30.0)
            return 0;
        
        if (position > 30.0 && position <= 60.0)
            return 1;
        
        if (position > 60.0 && position <= 90.0)
            return 2;
        
        if (position > 90.0 && position <= 120.0)
            return 3;
        
        if (position > 120.0 && position <= 150.0)
            return 4;
        
        if (position > 150.0 && position <= 180.0)
            return 5;
        
        if (position > 180.0 && position <= 210.0)
            return 6;
        
        if (position > 210.0 && position <= 240.0)
            return 7;
        
        if (position > 240.0 && position <= 270.0)
            return 8;
        
        if (position > 270.0 && position <= 300.0)
            return 9;
        
        if (position > 300.0 && position <= 330.0)
            return 10;
        
        if (position > 330.0 && position <= 360.0)
            return 11;
        
        return 0;
    }
}
