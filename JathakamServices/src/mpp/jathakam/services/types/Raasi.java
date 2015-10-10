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
    PISCES(AQUARIUS.end, TRIAD.Watery); /* Meena *//* Meena */

    private static Map<Range, Raasi> extents = null;
    private static Set<Range> extentsKeySet = null;

    private int start = 0;
    private int end = 0;
    private enum TRIAD {None, Fiery, Earthy, Airy, Watery};
    private final TRIAD triad;

    private Raasi(int begin, TRIAD t)
    {
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
}
