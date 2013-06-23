/*
 *
 *
 */
package mpp.jathakam.core;

import mpp.jathakam.core.Range;
import mpp.jathakam.utils.JatakamUtilities;

/**
 *
 * @author phani
 */
public class Range
    implements Comparable<Range>
{
    public final static Range EMPTY = new Range(0, 0);

    private double start;
    private double end;

    public Range(double s, double e)
    {
        start = s;
        end = e;
    }

    @Override
    public int compareTo(Range o)
    {
        return (int)((end * 60D * 60D * 1000D) - (o.end * 60D * 60D * 1000D));
    }

    public double start()
    {
        return start;
    }

    public double end()
    {
        return end;
    }
    
    @Override
    public String toString()
    {
        return "[" + JatakamUtilities.toStringDegree(start) + ", " + JatakamUtilities.toStringDegree(end) + "]";
    }
}
