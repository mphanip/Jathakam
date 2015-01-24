/* 
 * Yet to decide on the license
 */
package mpp.jathakamu.types;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import mpp.jathakamu.Constants;

/**
 *
 * @author Phani
 */
@XmlRootElement(name = "place")
public class Place
        implements Serializable
{

    /**
     * Name of the place
     */
    @XmlAttribute(name = "n")
    private String name;

    /**
     * Geographic Longitude of the place
     */
    @XmlAttribute(name = "lg", required = true)
    private double longitude;

    /**
     * Geographic Latitude of the place
     */
    @XmlAttribute(name = "la", required = true)
    private double latitude;

    @XmlAttribute(name = "tz", required = true)
    private String timeZone;
    
    @XmlAttribute(name = "wt")
    private boolean warTime = false;
    
    @XmlAttribute(name = "gc")
    private boolean geocentric = false;
    
    @XmlAttribute(name = "dst")
    private boolean applyDST = false; // Daylight Saving Time
    
    /**
     * Do not use this constructor, instead use Place.PlaceBuilder.
     */
    public Place()
    {
        super();
        name = "No Name";
        longitude = 0;
        latitude = 0;
        timeZone = Constants.DEFAULT_TIME_ZONE;
    }

    private Place(Place.PlaceBuilder builder)
    {
        this.name = builder.name;
        this.longitude = builder.longitude;
        this.latitude = builder.latitude;
        this.timeZone = builder.timeZone;
        this.warTime = builder.warTime;
        this.geocentric = builder.geocentric;
        this.applyDST = builder.applyDST;
    }

    public String getName()
    {
        return name;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public String getTimeZone()
    {
        return timeZone;
    }

    public boolean isWarTime()
    {
        return warTime;
    }

    public boolean isGeocentic()
    {
        return geocentric;
    }

    public boolean isApplyDST()
    {
        return applyDST;
    }

    public static class PlaceBuilder
    {
        private String name = "No Name";
        private double longitude;
        private double latitude;
        private String timeZone = Constants.DEFAULT_TIME_ZONE;
        private boolean warTime = false;
        private boolean geocentric = false;
        private boolean applyDST = false; // Daylight Saving Time

        public Place.PlaceBuilder values(String name, double longitude,
                double latitude, String timeZone,
                boolean isGeocentric, boolean applyDST, boolean isWarTime)
        {
            this.name = name;
            this.longitude = longitude;
            this.latitude = latitude;
            this.timeZone = timeZone;
            this.warTime = isWarTime;
            this.geocentric = isGeocentric;
            this.applyDST = applyDST;

            return this;
        }

        public Place.PlaceBuilder values(String name, double longitude,
                double latitude, String timeZone,
                boolean isGeocentric)
        {
            this.name = name;
            this.longitude = longitude;
            this.latitude = latitude;
            this.timeZone = timeZone;
            this.geocentric = isGeocentric;

            return this;
        }

        public Place.PlaceBuilder values(String name, double longitude,
                double latitude, String timeZone)
        {
            this.name = name;
            this.longitude = longitude;
            this.latitude = latitude;
            this.timeZone = timeZone;

            return this;
        }

        public Place.PlaceBuilder name(String name)
        {
            this.name = name;
            return this;
        }

        public Place.PlaceBuilder longitude(double longitude)
        {
            this.longitude = longitude;
            return this;
        }

        public Place.PlaceBuilder timeZone(String timeZone)
        {
            this.timeZone = timeZone;
            return this;
        }
        
        public Place.PlaceBuilder latitude(double latitude)
        {
            this.latitude = latitude;
            return this;
        }
        
        public Place.PlaceBuilder warTime(boolean warTime)
        {
            this.warTime = warTime;
            return this;
        }
        
        public Place.PlaceBuilder geocentric(boolean geocentric)
        {
            this.geocentric = geocentric;
            return this;
        }
        
        public Place.PlaceBuilder applyDST(boolean applyDST)
        {
            this.applyDST = applyDST;
            return this;
        }

        public Place build()
        {
            return new Place(this);
        }
    }
}
