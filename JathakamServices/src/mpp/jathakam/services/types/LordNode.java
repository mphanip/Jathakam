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
