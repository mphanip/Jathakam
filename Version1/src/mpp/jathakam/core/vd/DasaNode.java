/*
 *
 *
 */
package mpp.jathakam.core.vd;

import mpp.jathakam.core.Planet;
import mpp.jathakam.core.Range;

/**
 *
 * @author phani
 */
public class DasaNode
{
    private Planet planet;
    private Range value;
    private double timeFractionInYears;

    public DasaNode(Planet planet, Range value, double timeFraction)
    {
        this.planet = planet;
        this.value = value;
        timeFractionInYears = timeFraction;
    }
    
    public Planet getPlanet()
    {
        return planet;
    }

    public Range getValue()
    {
        return value;
    }

    public double getTimeFractionInYears()
	{
		return timeFractionInYears;
	}
    
    public double getDurationInYears(double longitude)
    {
    	double endValue = value.end();
		double startValue = value.start();
		double diffLongitude = (endValue - longitude)/(endValue - startValue);
		double durationInYears = (diffLongitude * timeFractionInYears);
		
		return durationInYears;
    }
    
    public double getRemainingDurationForLongitude(double longitude)
    {
    	double endValue = value.end();
		double startValue = value.start();
		double diffLongitude = (endValue - longitude)/(endValue - startValue);
		
		return diffLongitude;
    }

	@Override
    public String toString()
    {
        return "[" + planet + ", " + value + ", " + timeFractionInYears +']';
    }
}
