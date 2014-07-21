/* 
 * Yet to decide on the license
 */
package mpp.jathakamu.types;

import java.io.Serializable;
import java.util.TimeZone;
import mpp.jathakamu.Constants;

/**
 *
 * @author Phani
 */
public class Place
        implements Serializable
{

    /**
     * Name of the place
     */
    private final String name;

    /**
     * Geographic Longitude of the place
     */
    private final Degree longitude;

    /**
     * Geographic Latitude of the place
     */
    private final Degree latitude;

    private final TimeZone timeZone;
    private boolean warTime = false;
    private boolean geocentric = false;
    private boolean applyDST = false; // Daylight Saving Time
    
    /**
     * Do not use this constructor, instead use Place.Builder.
     */
    public Place()
    {
        super();
        name = "No Name";
        longitude = new Degree(0);
        latitude = new Degree(0);
        timeZone = TimeZone.getTimeZone(Constants.DEFAULT_TIME_ZONE);
    }

    private Place(Place.Builder builder)
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

    public Degree getLongitude()
    {
        return longitude;
    }

    public Degree getLatitude()
    {
        return latitude;
    }

    public TimeZone getTimeZone()
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

    public static class Builder
    {
        private String name = "No Name";
        private Degree longitude;
        private Degree latitude;
        private TimeZone timeZone = TimeZone.getTimeZone(Constants.DEFAULT_TIME_ZONE);
        private boolean warTime = false;
        private boolean geocentric = false;
        private boolean applyDST = false; // Daylight Saving Time

        public Place.Builder values(String name, double longitude,
                double latitude, String timeZone,
                boolean isGeocentric, boolean applyDST, boolean isWarTime)
        {
            this.name = name;
            this.longitude = new Degree(longitude);
            this.latitude = new Degree(latitude);
            this.timeZone = TimeZone.getTimeZone(timeZone);
            this.warTime = isWarTime;
            this.geocentric = isGeocentric;
            this.applyDST = applyDST;

            return this;
        }

        public Place.Builder values(String name, double longitude,
                double latitude, String timeZone,
                boolean isGeocentric)
        {
            this.name = name;
            this.longitude = new Degree(longitude);
            this.latitude = new Degree(latitude);
            this.timeZone = TimeZone.getTimeZone(timeZone);
            this.geocentric = isGeocentric;

            return this;
        }

        public Place.Builder values(String name, double longitude,
                double latitude, String timeZone)
        {
            this.name = name;
            this.longitude = new Degree(longitude);
            this.latitude = new Degree(latitude);
            this.timeZone = TimeZone.getTimeZone(timeZone);

            return this;
        }

        public Place.Builder name(String name)
        {
            this.name = name;
            return this;
        }

        public Place.Builder longitude(double longitude)
        {
            this.longitude = new Degree(longitude);
            return this;
        }

        public Place.Builder timeZone(String timeZone)
        {
            this.timeZone = TimeZone.getTimeZone(timeZone);
            return this;
        }
        
        public Place.Builder latitude(double latitude)
        {
            this.latitude = new Degree(latitude);
            return this;
        }
        
        public Place.Builder warTime(boolean warTime)
        {
            this.warTime = warTime;
            return this;
        }
        
        public Place.Builder geocentric(boolean geocentric)
        {
            this.geocentric = geocentric;
            return this;
        }
        
        public Place.Builder applyDST(boolean applyDST)
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
