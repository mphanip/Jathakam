/* 
 * Yet to decide on the license
 */
package mpp.jathakamu;

import java.util.logging.Level;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import mpp.jathakamu.calculations.SupportCalcs;
import mpp.jathakamu.types.Place;
import mpp.jathakamu.utils.ViewUtils;

/**
 * This class is used to maintain all the input details needed for Ephemeris and
 * other calculations. There should be one and only one instance per analysis
 * (i.e. for Horary or Horoscope analysis)
 * 
 * This is designed to be immutable class, hence thread safe.
 *
 * @author Phani
 */
@XmlRootElement(name = "profile")
public class Profile
{
    /**
     * Julian date and time in UT. Needed for all Ephemeris calculations.
     */
    @XmlAttribute(name = "jd")
    private double tjd_ut = 0.0;
    
    @XmlElement(name = "place")
    private Place place = null;
    
    @XmlElement(name = "settings")
    private final ProfileSettings profileSettings;
    
    @XmlElement(name = "h249")
    private int horaryNumber249 = 0;
    
    @XmlElement(name = "h2193")
    private int horaryNumber2193 = 0;
    
    public Profile(double tjd_ut, Place place, ProfileSettings settings)
    {
        this.tjd_ut = tjd_ut;
        this.place = place;
        profileSettings = settings;
    }
    
    String getID()
    {
        return profileSettings.getID();
    }

    public double getDateTime()
    {
        return tjd_ut;
    }

    public String getTimeZone()
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
        double latitude = place.getLatitude();

        if (profileSettings.isUseGeocentric())
        {
            latitude = SupportCalcs.getGeocentricLatitude(latitude);
            JathakamLogger.LOGGER.log(Level.INFO, "Geocentric Latitude is : {0}",
                    ViewUtils.toStringDegree(latitude));
        }

        return latitude;
    }

    public double getLongitude()
    {
        return place.getLongitude();
    }

    public ProfileSettings getProfileSetting()
    {
        return profileSettings;
    }   

    public Place getPlace()
    {
        return place;
    }

    public int getHoraryNumber249()
    {
        return horaryNumber249;
    }

    public int getHoraryNumber2193()
    {
        return horaryNumber2193;
    }
    
}
