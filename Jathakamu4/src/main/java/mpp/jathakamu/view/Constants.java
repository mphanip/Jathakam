/* 
 * Yet to decide on the license
 */

package mpp.jathakamu.view;

import java.time.ZoneId;
import java.util.Set;

/**
 *
 * @author phani
 */
public final class Constants
{
    static
    {
        
    }
    
    public final static Set<String> getTimeZones()
    {
        return ZoneId.getAvailableZoneIds();
    }
    
    public final static String []LONGITUDE_DIRECTIONS = {"East", "West"};
    
    public final static String []LATITUDE_DIRECTIONS = {"North", "South"};
    
    public final static String []GENDERS = {"Male", "Female", "Others"};
}
