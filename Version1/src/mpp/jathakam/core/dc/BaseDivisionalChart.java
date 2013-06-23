/*
 *
 *
 */
package mpp.jathakam.core.dc;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;

import mpp.jathakam.core.Range;

/**
 * Base class for all divisional charts.
 *
 * @author phani
 */
public abstract class BaseDivisionalChart
    implements DivisionalChart
{
    protected String name;
    protected short divisions;
    protected NavigableSet<Range> divisionsRange;
    protected double longitude;

    public BaseDivisionalChart(String name, short divisions)
    {
        this.name = name;
        this.divisions = divisions;
        calculateDivisions();
    }

    @Override
    public String getName()
    {
        return name;
    }
    
    @Override
    public double getLongitude()
	{
		return longitude;
	}

    @Override
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}

	private void calculateDivisions()
    {
        divisionsRange = new ConcurrentSkipListSet<Range>();

        double start = 0;
        double end = 0;

        for (int i = 0; i < divisions; i++)
        {
            end = (30D * (double) (i+1)) / (double) divisions;
            Range r = new Range(start, end);
            divisionsRange.add(r);
            start = end;
        }
    }

    @Override
    public List<Range> getDivisions()
    {
        return new ArrayList<Range>(divisionsRange);
    }

    @Override
    public short getDivision()
    {
        return divisions;
    }

    @Override
    public double getDCLongitude()
    {
        // Remove the signs completed from the logitude
        while (longitude > 30)
        {
            longitude -= 30.0;
        }
        double rtnLongitude = longitude * (double) divisions;

        while (rtnLongitude > 30)
        {
            rtnLongitude -= 30;
        }

        return rtnLongitude;
    }

    protected int getDivisionIndex()
    {
        double position = longitude;

        // Get planet position without Raasi Position
        while (position - 30D > 0)
        {
            position = position - 30D;
        }

        int rtnPosition = -1;

        for (Range r : divisionsRange)
        {
            if (r.start() < position )
            {
                rtnPosition++;
            }
            else
            {
                break;
            }
        }

        return rtnPosition;
    }
}
