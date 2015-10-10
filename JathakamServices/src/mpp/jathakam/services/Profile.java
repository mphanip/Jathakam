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
package mpp.jathakam.services;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import mpp.jathakam.services.calculations.SupportCalcs;
import mpp.jathakam.services.types.Place;

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
    
    @XmlElement(name = "hn")
    private final int horaryNumber;
    
    Profile(double tjd_ut, Place place, ProfileSettings settings)
    {
        this.tjd_ut = tjd_ut;
        this.place = place;
        profileSettings = settings;
        horaryNumber = -1; // not set
    }

    Profile(ProfileBuilder aThis)
    {
        tjd_ut = aThis.tjd_ut;
        place = aThis.place;
        horaryNumber = aThis.horaryNumber;
        profileSettings = new ProfileSettings();
        profileSettings.setAynamsa(aThis.ayanamsa);
        profileSettings.setEphemerisFlags(aThis.ephemerisFlags);
        profileSettings.setHoraryNumberGroup(aThis.horaryNumberGroup);
        profileSettings.setHouseSystem(aThis.houseSystem);
        profileSettings.setOneYear(aThis.oneYear);
        profileSettings.setTrueNode(aThis.trueNode);
        profileSettings.setUseGeocentric(aThis.useGeocentric);
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
//            JathakamLogger.LOGGER.log(Level.INFO, "Geocentric Latitude is : {0}", ViewUtils.toStringDegree(latitude));
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

    public int getHoraryNumber()
    {
        return horaryNumber;
    }

}
