/*
 *
 *
 */
package mpp.jathakamu.types;

import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author phani
 */
public enum Raasi
{
    NONE(0, TRIAD.None),
    ARIES(0, TRIAD.Fiery), /* Mesha */
    TARUS(ARIES.end, TRIAD.Earthy), /* Vrishaba */
    GEMINI(TARUS.end, TRIAD.Airy), /* Mithuna */
    CANCER(GEMINI.end, TRIAD.Watery), /* Karkataka or Kataka */
    LEO(CANCER.end, TRIAD.Fiery), /* Simha */
    VIRGO(LEO.end, TRIAD.Earthy), /* Kanya */
    LIBRA(VIRGO.end, TRIAD.Airy), /* Thula */
    SCORPIO(LIBRA.end, TRIAD.Watery), /* Vrischika */
    SAGITTARIUS(SCORPIO.end, TRIAD.Fiery), /* Dhanus */
    CAPRICORN(SAGITTARIUS.end, TRIAD.Earthy), /* Makara */
    AQUARIUS(CAPRICORN.end, TRIAD.Airy), /* Kumbha */
    PISCES(AQUARIUS.end, TRIAD.Watery); /* Meena */

    private static NavigableMap<Range, Raasi> extents = null;

    private int start = 0;
    private int end = 0;
    private enum TRIAD {None, Fiery, Earthy, Airy, Watery};
    private TRIAD triad;

    private Raasi(int begin, TRIAD t)
    {
        start = begin;
        end = start + 30;
        triad = t;
    }

    public Range getExtent()
    {
        return new Range(start, end);
    }

    public static Raasi getRaasi(int ordinal)
    {
        for (Raasi r : values())
        {
            if (r.ordinal() == ordinal)
            {
                return r;
            }
        }

        return Raasi.NONE;
    }

    public synchronized static Raasi getRaasi(double longitude)
    {
        if (extents == null)
        {
            extents = new ConcurrentSkipListMap<Range, Raasi>();

            for (Raasi sign : Raasi.values())
            {
                extents.put(sign.getExtent(), sign);
            }
        }
        
        Map.Entry<Range, Raasi> entry = extents.ceilingEntry(new Range(0, longitude));
        
        if (entry == null)
        {
            return null;
        }
        
        Raasi rtnValue = entry.getValue();
        
        if (entry.getKey().end() == longitude)
        {
            // set it to next raasi
            int nextRaasi = entry.getValue().ordinal()+1;
            rtnValue = Raasi.getRaasi(nextRaasi);
        }
        
        return rtnValue;
    }

    public boolean isFiery()
    {
        return triad.equals(TRIAD.Fiery);
    }

    public boolean isEarthy()
    {
        return triad.equals(TRIAD.Earthy);
    }

    public boolean isAiry()
    {
        return triad.equals(TRIAD.Airy);
    }

    public boolean isWatery()
    {
        return triad.equals(TRIAD.Watery);
    }

    public boolean isMovable()
    {
        return (ordinal() % 3 == 1);
    }

    public boolean isFixed()
    {
        return (ordinal() % 3 == 2);
    }

    public boolean isCommon()
    {
        return (ordinal() % 3 == 0);
    }

    public boolean isPositive()
    {
        return (ordinal() % 2 == 1);
    }

    public boolean isNegative()
    {
        return (ordinal() % 2 == 0);
    }

    public boolean isMasculine()
    {
        return (ordinal() % 2 == 1);
    }

    public boolean isFemine()
    {
        return (ordinal() % 2 == 0);
    }

    public boolean isBarren()
    {
        int ord = this.ordinal();

        if (ord == 1
            || ord == 3
            || ord == 5
            || ord == 6
            || ord == 11)
        {
            return true;
        }

        return false;
    }

    public boolean isSemiFruitful()
    {
        int ord = this.ordinal();

        if (ord == 2
            || ord == 7
            || ord == 9
            || ord == 10)
        {
            return true;
        }

        return false;
    }

    public boolean isFruitful()
    {
        int ord = this.ordinal();

        if (ord == 4
            || ord == 8
            || ord == 12)
        {
            return true;
        }

        return false;
    }
    
    /*
     * Used for calculating houses given ascendant and latitude for Horary charts.
     */
    public boolean isRAMCPositive()
    {
        int ord = this.ordinal();
        if (ord == 1 || ord == 2 || ord == 3 || ord == 4 || ord == 11 || ord == 12)
        {
            return false;
        }
        
        return true;
    }
}
