/*
 * Yet to decide on the License, should either BSD or GPL
 *  Copyright(R) Phani Pramod
 */

package mpp.jathakamu.types;

import static mpp.jathakamu.Constants.DEG_SEP;

import java.io.Serializable;
import mpp.jathakamu.Constants;

/**
 * This is an immutable class
 * 
 * @author phani
 */
public class Degree
    implements Serializable
{
    private final double degrees;
    private int nDegree;
    private final int nDegreeInRaasi;
    private int nMinutes;
    private double dSeconds;
    boolean useRaasi = false;

    public Degree(double degrees)
    {
        this.degrees = degrees;
        
        nDegree = Double.valueOf(degrees).intValue();
        double dmin = (degrees - nDegree) * 60D;
        nMinutes = Double.valueOf(dmin).intValue();
        dSeconds = (dmin - nMinutes) * 60D;
        
        if (Constants.NUM_FORMAT.format(dSeconds).equals("60.00"))
        {
            nMinutes++;
            dSeconds = 0;
        }
        
        if (nMinutes == 60)
        {
            nDegree++;
            nMinutes = 0;
        }
        
        nDegreeInRaasi = nDegree % 30;
    }

    public Degree(int nDegree, int nMinutes, double dSeconds)
    {
        this.nDegree = nDegree;
        this.nMinutes = nMinutes;
        this.dSeconds = dSeconds;
        nDegreeInRaasi = nDegree % 30;
        
        degrees = nDegree + (nMinutes / 60D) + (dSeconds / 3600D);
    }
    
    public Degree(double degrees, boolean useRaasi)
    {
        this(degrees);
        this.useRaasi = useRaasi;
    }
    
    public double getDegree()
    {
        return degrees;
    }
    
    public double getDegreeRaasi()
    {
        return nDegreeInRaasi;
    }
    
    @Override
    public String toString()
    {
        return toString(DEG_SEP);
    }
    
    public String toString(String seperator)
    {
        return toString(seperator, useRaasi);
    }
    
    public String toStringRaasi()
    {
        return toString(DEG_SEP, true);
    }
    
    public String toStringRaasi(String seperator)
    {
        return toString(seperator, true);
    }
    
    private String toString(String seperator, boolean forRaasi)
    {
        int d = (forRaasi) ? nDegreeInRaasi : nDegree;
        String deg = Constants.DEG_NUM_FORMAT.format(d);
        StringBuilder sb = new StringBuilder();

        sb.append(deg);
        
        if (forRaasi)
        {
            sb.append(Raasi.getRaasi(degrees).toString2Letters());
        }
        else
        {
            sb.append( seperator);
        }
        
        sb.append(Constants.MIN_NUM_FORMAT.format(nMinutes)).append(seperator);
        sb.append(Constants.NUM_FORMAT.format(dSeconds));

        return sb.toString();
    }
}
