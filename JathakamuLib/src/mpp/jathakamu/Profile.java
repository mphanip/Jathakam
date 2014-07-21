/* 
 * Yet to decide on the license
 */
package mpp.jathakamu;

import java.io.Serializable;
import java.util.TimeZone;
import java.util.logging.Level;
import mpp.jathakamu.calculations.SupportCalcs;
import mpp.jathakamu.types.Degree;
import mpp.jathakamu.types.Place;

/**
 * This class is used to maintain all the input details needed for Ephemeris and
 * other calculations. There should be one and only one instance per analysis
 * (i.e. for Horary or Horoscope analysis)
 * 
 * This is designed to be immutable class, hence thread safe.
 *
 * @author Phani
 */
public class Profile
    implements Serializable
{
    /**
     * Julian date and time in UT. Needed for all Ephemeris calculations.
     */
    private double tjd_ut = 0.0;
    
    private Place place = null;
    
    private final ProfileSettings profileSetting;
    
    public Profile(double tjd_ut, Place place, ProfileSettings settings)
    {
        this.tjd_ut = tjd_ut;
        this.place = place;
        profileSetting = settings;
    }
    
    String getID()
    {
        return profileSetting.getID();
    }

    public double getJulianDayUT()
    {
        return tjd_ut;
    }

    public TimeZone getTimeZone()
    {
        return place.getTimeZone();
    }
    
    /**
     * The function will return latitude based on the settings value, use
     * useGeocentric
     *
     * @return latitude
     */
    public double getLatitude()
    {
        double latitude = place.getLatitude().getDegree();

        if (profileSetting.isUseGeocentric())
        {
            latitude = SupportCalcs.getGeocentricLatitude(latitude);
            JathakamLogger.LOGGER.log(Level.INFO, "Geocentric Latitude is : {0}",
                    new Degree(latitude));
        }

        return latitude;
    }

    public double getLongitude()
    {
        return place.getLongitude().getDegree();
    }

    public ProfileSettings getProfileSetting()
    {
        return profileSetting;
    }   

    public Place getPlace()
    {
        return place;
    }
}
