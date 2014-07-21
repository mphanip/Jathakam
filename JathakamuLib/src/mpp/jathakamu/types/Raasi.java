/*
 * Yet to decide on the license
 *
 */
package mpp.jathakamu.types;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author phani
 */
public enum Raasi
{
    NONE("", "", 0, TRIAD.None),
    ARIES("Aries", "Ar", 0, TRIAD.Fiery), /* Mesha */
    TARUS("Tarus", "Ta", ARIES.end, TRIAD.Earthy), /* Vrishaba */
    GEMINI("Gemine", "Ge", TARUS.end, TRIAD.Airy), /* Mithuna */
    CANCER("Cancer", "Ca", GEMINI.end, TRIAD.Watery), /* Karkataka or Kataka */
    LEO("Leo", "Le", CANCER.end, TRIAD.Fiery), /* Simha */
    VIRGO("Virgo", "Vi", LEO.end, TRIAD.Earthy), /* Kanya */
    LIBRA("Libra", "Li", VIRGO.end, TRIAD.Airy), /* Thula */
    SCORPIO("Scorpio", "Sc", LIBRA.end, TRIAD.Watery), /* Vrischika */
    SAGITTARIUS("Sagittarius", "Sa", SCORPIO.end, TRIAD.Fiery), /* Dhanus */
    CAPRICORN("Capricorn", "Ca", SAGITTARIUS.end, TRIAD.Earthy), /* Makara */
    AQUARIUS("Aquarius", "Aq", CAPRICORN.end, TRIAD.Airy), /* Kumbha */
    PISCES("Pisces", "Pi", AQUARIUS.end, TRIAD.Watery); /* Meena */

    private static Map<Range, Raasi> extents = null;
    private static Set<Range> extentsKeySet = null;

    private int start = 0;
    private int end = 0;
    private enum TRIAD {None, Fiery, Earthy, Airy, Watery};
    private final TRIAD triad;
    private String name;
    private String twoLetterName;

    private Raasi(String pName, String pTwoLetterName, int begin, TRIAD t)
    {
        name = pName;
        twoLetterName = pTwoLetterName;
        start = begin;
        end = start + 30;
        triad = t;
    }

    public Range getExtent()
    {
        return new Range.Builder().values(start, end).build();
    }

    public static Raasi getRaasiByOrdinal(int ordinal)
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
            extents = new HashMap<>();

            for (Raasi sign : Raasi.values())
            {
                extents.put(sign.getExtent(), sign);
            }
            extentsKeySet = extents.keySet();
        }
        
        if (longitude == 0.0 || longitude == 360.0)
        {
            return ARIES;
        }
        
        Optional<Range> key = extentsKeySet.parallelStream()
                .filter(range -> range.inRange((longitude == 360.0) ? 0.0 : longitude))
                .findFirst();
        
        if (!key.isPresent())
        {
            return Raasi.NONE;
        }
        key.get();
        
        Raasi rtnValue = extents.get(key.get());
        
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
    
    public String toString2Letters()
    {
        return twoLetterName;
    }

    @Override
    public String toString()
    {
        return name;
    }
    
    
}
