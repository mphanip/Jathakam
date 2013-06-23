/*
 *
 *
 */
package mpp.jathakam.core;

import mpp.jathakam.utils.JatakamUtilities;

/**
 *
 * @author phani
 */
public class PlaceInfo
{

    private String name = "Allahabad";
    private double geolon = 82.5D;
    private double geolat = 17.0D;
    private String timezoneDisplayName = "IST-Indian Standard Time";

    public PlaceInfo(String name, double longitude, double latitude, String timezoneDisplayName)
    {
        this.name = name;
        this.geolon = longitude;
        this.geolat = latitude;
        this.timezoneDisplayName = timezoneDisplayName;
    }

    public double getLatitude()
    {
        return this.geolat;
    }

    public double getLogitude()
    {
        return this.geolon;
    }

    public String getName()
    {
        return this.name;
    }

    public String getTimezoneDisplayName()
    {
        return this.timezoneDisplayName;
    }

    @Override
    public String toString()
    {
        return "PlaceInfo{" + "name=" + name
            + ", geolon=" + JatakamUtilities.toStringDegree(geolon)
            + ", geolat=" + JatakamUtilities.toStringDegree(geolat)
            + ", timeZone=" + timezoneDisplayName + '}';
    }
}
