/*
 *
 *
 */
package mpp.jathakamu.types;

/**
 * This will be used for Vimshottari dasa lords - Mahadasa, Bhukti, anthara,
 * Sookshma, Prana and for star lord, sub lord, sub... lords
 * 
 * @author Phani
 */
public class LordNode
{
    private Planet planet;
    private Range value;
    private double timeFractionInYears;

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
