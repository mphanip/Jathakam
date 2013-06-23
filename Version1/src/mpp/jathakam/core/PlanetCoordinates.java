/*
 *
 *
 */
package mpp.jathakam.core;

/**
 * @author phani
 */
import java.io.Serializable;
import mpp.jathakam.utils.JatakamUtilities;

public class PlanetCoordinates
    implements Serializable, Cloneable
{

    private static final long serialVersionUID = 1L;
    private double longitude;
    private double latitude;
    private double distance;
    private double speedInLongitude;
    private double speedInLatitude;
    private double speedInDistance;
    private int index;

    public PlanetCoordinates(int index, double longitude, double latitude, double distance, double speedInLongitude, double speedInLatitude, double speedInDistance)
    {
        this.longitude = longitude;
        this.latitude = latitude;
        this.distance = distance;
        this.speedInLongitude = speedInLongitude;
        this.speedInLatitude = speedInLatitude;
        this.speedInDistance = speedInDistance;
        this.index = index;
    }

    public double getDistance()
    {
        return this.distance;
    }

    public int getIndex()
    {
        return this.index;
    }

    public double getLatitude()
    {
        return this.latitude;
    }

    public double getLongitude()
    {
        return this.longitude;
    }

    public String getPlanetName()
    {
        if ((this.index < 0) || (this.index > 14))
        {
            return "";
        }

        String planetName = JathakamConstants.PLANET_NAMES[this.index];

        return planetName;
    }

    public double getSpeedInDistance()
    {
        return this.speedInDistance;
    }

    public double getSpeedInLatitude()
    {
        return this.speedInLatitude;
    }

    public double getSpeedInLongitude()
    {
        return this.speedInLongitude;
    }

    public boolean isRetrograde()
    {
        return (speedInLongitude < 0);
    }

    @Override
    protected Object clone()
        throws CloneNotSupportedException
    {
        super.clone();
        PlanetCoordinates pInfo = new PlanetCoordinates(this.index, this.longitude, this.latitude, this.distance,
            this.speedInLongitude, this.speedInLatitude,
            this.speedInDistance);
        return pInfo;
    }

    public String getRaasiName()
    {
        int raasi = getRaasi();
        return Raasi.getRaasi(raasi).name();
    }

    public int getRaasi()
    {
        return Double.valueOf(Math.ceil(longitude / 30D)).intValue();
    }

    @Override
    public String toString()
    {
        return "[" + getRaasiName() + " : " + getPlanetName()
            + " : " + JatakamUtilities.toStringDegree(this.longitude)
            + ((speedInLongitude < 0) ? "(R)" : "")
            + "]";
    }
}
