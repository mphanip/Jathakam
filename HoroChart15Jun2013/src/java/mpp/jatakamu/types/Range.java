/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mpp.jatakamu.types;

import mpp.jatakamu.utils.ViewUtils;

/**
 *
 * @author Phani Pramod M
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
        return (int) ((end * 60D * 60D * 1000D) - (o.end * 60D * 60D * 1000D));
    }

    public double start()
    {
        return start;
    }

    public double end()
    {
        return end;
    }
    
    public boolean contains(double longitude)
    {
        boolean flag = (longitude >= start && longitude < end);

        return flag;
    }

    @Override
    public String toString()
    {
        return "[" + ViewUtils.toStringDegree(start) + ", "
                + ViewUtils.toStringDegree(end) + "]";
    }

}
