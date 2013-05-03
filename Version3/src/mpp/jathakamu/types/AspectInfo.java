package mpp.jathakamu.types;

import mpp.jathakamu.Constants.ASPECT_EFFECT;

public class AspectInfo
{
    private double longitude;
    private ASPECT_EFFECT aspect = ASPECT_EFFECT.NONE;

    /*
     * whether this aspect is major or not
     */
    private boolean major;

    /*
     * aspect name
     */
    private String name;

    /*
     * In case of planet planet index is store and for cusp it will be always
     * int
     */
    private int from;
    private int to;

    public AspectInfo(double longitude, int from, int to)
    {
        super();
        this.longitude = longitude;
        this.from = from;
        this.to = to;
    }

    public AspectInfo(double longitude, int from, int to, ASPECT_EFFECT aspect)
    {
        super();
        this.longitude = longitude;
        this.aspect = aspect;
        this.from = from;
        this.to = to;
    }

    public AspectInfo(double longitude, int from, int to, ASPECT_EFFECT aspect,
            String name, boolean major)
    {
        super();
        this.longitude = longitude;
        this.aspect = aspect;
        this.major = major;
        this.name = name;
        this.from = from;
        this.to = to;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public void setAspect(ASPECT_EFFECT aspect)
    {
        this.aspect = aspect;
    }

    public void setMajor(boolean major)
    {
        this.major = major;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setFrom(int from)
    {
        this.from = from;
    }

    public void setTo(int to)
    {
        this.to = to;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public ASPECT_EFFECT getAspect()
    {
        return aspect;
    }

    public int getFrom()
    {
        return from;
    }

    public int getTo()
    {
        return to;
    }

    public boolean isMajor()
    {
        return major;
    }

    public String getName()
    {
        return name;
    }
    
    public Planet getFromPlanet()
    {
        return Planet.getPlanet(from);
    }
    
    public Planet getToPlanet()
    {
        return Planet.getPlanet(to);
    }
    
    public int getToCusp()
    {
        return to;
    }
}
