/*
 * Yet to decide on the license
 */

package mpp.jathakamu.types;

import java.io.Serializable;

/**
 * This will be used for Vimshottari dasa lords - Mahadasa, Bhukti, anthara,
 * Sookshma, Prana and for star lord, sub lord, sub... lords
 * 
 * @author Phani
 */
public class LordNode
    implements Serializable
{
    private final Planet planet;
    private final Range value;
    private final double timeFractionInYears;

    public LordNode(Planet planet, Range value, double timeFraction)
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
    	double endValue = value.getMax().doubleValue();
		double startValue = value.getMin().doubleValue();
		double diffLongitude = (endValue - longitude)/(endValue - startValue);
		double durationInYears = (diffLongitude * timeFractionInYears);
		
		return durationInYears;
    }
    
    public double getRemainingDurationForLongitude(double longitude)
    {
    	double endValue = value.getMax().doubleValue();
		double startValue = value.getMin().doubleValue();
		double diffLongitude = (endValue - longitude)/(endValue - startValue);
		
		return diffLongitude;
    }

	@Override
    public String toString()
    {
        return "[" + planet + ", " + value + ", " + timeFractionInYears +']';
    }
}
