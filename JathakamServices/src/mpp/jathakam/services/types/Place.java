/*
 * Copyright (c) 2015, phani
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package mpp.jathakam.services.types;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import mpp.jathakam.services.Defaults;

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
     * Do not use this constructor, instead use PlaceBuilder.
     */
    public Place()
    {
        super();
        name = "Untitled";
        longitude = 0;
        latitude = 0;
        timeZone = Defaults.DEFAULT_TIME_ZONE;
    }

    private Place(PlaceBuilder builder)
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
        private String timeZone = Defaults.DEFAULT_TIME_ZONE;
        private boolean warTime = false;
        private boolean geocentric = false;
        private boolean applyDST = false; // Daylight Saving Time

        public PlaceBuilder values(String name, double longitude,
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

        public PlaceBuilder values(String name, double longitude,
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

        public PlaceBuilder values(String name, double longitude,
                double latitude, String timeZone)
        {
            this.name = name;
            this.longitude = longitude;
            this.latitude = latitude;
            this.timeZone = timeZone;

            return this;
        }
        
        public PlaceBuilder values(String name, double longitude,
                double latitude)
        {
            this.name = name;
            this.longitude = longitude;
            this.latitude = latitude;
            this.timeZone = Defaults.DEFAULT_TIME_ZONE;

            return this;
        }

        public PlaceBuilder name(String name)
        {
            this.name = name;
            return this;
        }

        public PlaceBuilder longitude(double longitude)
        {
            this.longitude = longitude;
            return this;
        }

        public PlaceBuilder timeZone(String timeZone)
        {
            this.timeZone = timeZone;
            return this;
        }
        
        public PlaceBuilder latitude(double latitude)
        {
            this.latitude = latitude;
            return this;
        }
        
        public PlaceBuilder warTime(boolean warTime)
        {
            this.warTime = warTime;
            return this;
        }
        
        public PlaceBuilder geocentric(boolean geocentric)
        {
            this.geocentric = geocentric;
            return this;
        }
        
        public PlaceBuilder applyDST(boolean applyDST)
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
