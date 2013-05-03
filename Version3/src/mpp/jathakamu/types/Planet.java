package mpp.jathakamu.types;

/**
 * @author phani
 */

import static swisseph.SweConst.SE_SUN;
import static swisseph.SweConst.SE_MOON;
import static swisseph.SweConst.SE_MERCURY;
import static swisseph.SweConst.SE_MARS;
import static swisseph.SweConst.SE_VENUS;
import static swisseph.SweConst.SE_JUPITER;
import static swisseph.SweConst.SE_SATURN;
import static swisseph.SweConst.SE_URANUS;
import static swisseph.SweConst.SE_NEPTUNE;
import static swisseph.SweConst.SE_PLUTO;


import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import mpp.jathakamu.Constants;

public enum Planet
{
    KETU(11, EnumSet.of(Raasi.NONE), 7, EnumSet.of(Nakshatram.ASWINI, Nakshatram.MAKHA, Nakshatram.MOOLA)),
    VENUS(SE_VENUS, EnumSet.of(Raasi.LIBRA, Raasi.TARUS), 20, EnumSet.of(Nakshatram.BHARANI, Nakshatram.PURVA_PHALGUNI, Nakshatram.PURVA_ASHADA)),
    SUN(SE_SUN, EnumSet.of(Raasi.LEO), 6, EnumSet.of(Nakshatram.KRITIKA, Nakshatram.UTTARA_PHALGUNI, Nakshatram.UTTARA_ASHADA)),
    MOON(SE_MOON, EnumSet.of(Raasi.CANCER), 10, EnumSet.of(Nakshatram.ROHINI, Nakshatram.HASTA, Nakshatram.SHRAVANA)),
    MARS(SE_MARS, EnumSet.of(Raasi.SCORPIO, Raasi.ARIES), 7, EnumSet.of(Nakshatram.MRIGHASIRA, Nakshatram.CHITRA, Nakshatram.DHANISHTA)),
    RAHU(10, EnumSet.of(Raasi.NONE), 18, EnumSet.of(Nakshatram.ARUDRA, Nakshatram.SWATHI, Nakshatram.SHATABHISHA)),
    JUPITER(SE_JUPITER, EnumSet.of(Raasi.SAGITTARIUS, Raasi.PISCES), 16, EnumSet.of(Nakshatram.PUNARVASU, Nakshatram.VISAKHA, Nakshatram.PURVA_BHADRA)),
    SATURN(SE_SATURN, EnumSet.of(Raasi.CAPRICORN, Raasi.AQUARIUS), 19, EnumSet.of(Nakshatram.PUSHYAMI, Nakshatram.ANURADHA, Nakshatram.UTTARA_BHADRA)),
    MERCURY(SE_MERCURY, EnumSet.of(Raasi.VIRGO, Raasi.GEMINI), 17, EnumSet.of(Nakshatram.ASLESHA, Nakshatram.JYESHTHA, Nakshatram.REVATHI)),
    URANUS(SE_URANUS, Collections.<Raasi> emptySet(), 0, Collections.<Nakshatram> emptySet()),
    NEPTUNE(SE_NEPTUNE, Collections.<Raasi> emptySet(), 0, Collections.<Nakshatram> emptySet()),
    PLUTO(SE_PLUTO, Collections.<Raasi> emptySet(), 0, Collections.<Nakshatram> emptySet());

    private int index;
    private Set<Raasi> raasi;
    private int dasaDuration;
    private Set<Nakshatram> stars;

    private Planet(int planetIndex, Set<Raasi> sign, int dasaDuration, Set<Nakshatram> stars)
    {
        this.index = planetIndex;
        this.raasi = sign;
        this.dasaDuration = dasaDuration;
        this.stars = stars;
    }

    public int getIndex()
    {
        return this.index;
    }

    public Set<Raasi> getOwnerRaasiList()
    {
        return raasi;
    }

    public int getDasaDuration()
    {
        return dasaDuration;
    }

    public Set<Nakshatram> getStars()
    {
        return stars;
    }

    public static Planet getNakshatraLord(Nakshatram star)
    {
        for (Planet p : values())
        {
            Set<Nakshatram> stars = p.stars;

            if (stars.contains(star))
                return p;
        }

        return null;
    }

    public static Planet getSignLord(Raasi sign)
    {
        for (Planet p : values())
        {
            Set<Raasi> signs = p.raasi;

            if (signs.contains(sign))
                return p;
        }

        return null;
    }
    
    public static Planet getPlanetSign(Raasi sign)
    {
        for (Planet p : values())
        {
            Set<Raasi> signs = p.raasi;

            if (signs.contains(sign))
                return p;
        }

        return null;
    }

    public String getName()
    {
        return Constants.PLANET_NAMES[index];
    }
    
    public String getShortName()
    {
        return Constants.PLANET_SHORT_NAMES[index];
    }
    
    public String get2LetterName()
    {
        return Constants.PLANET_2_LETTER_NAMES[index];
    }

    public static Planet getPlanet(int index)
    {
        for (Planet p : values())
        {
            if (p.index == index)
            {
                return p;
            }
        }

        return null;
    }
    
    public static Planet getPlanetByOrdinal(int ordinalIndex)
    {
    	for (Planet p : values())
        {
            if (p.ordinal() == ordinalIndex)
            {
                return p;
            }
        }

        return null;
    }
    
    public static List<Planet> getVimshottariDasaPlanets()
    {
    	List<Planet> pList = new ArrayList<Planet>();
		pList.add(Planet.KETU);
		pList.add(Planet.VENUS);
		pList.add(Planet.SUN);
		pList.add(Planet.MOON);
		pList.add(Planet.MARS);
		pList.add(Planet.RAHU);
		pList.add(Planet.JUPITER);
		pList.add(Planet.SATURN);
		pList.add(Planet.MERCURY);
		
		return pList;
    }
    
    public static List<Planet> getPlanetsAsList()
    {
        List<Planet> pList = new ArrayList<Planet>();
        pList.add(Planet.SUN);
        pList.add(Planet.MOON);
        pList.add(Planet.MERCURY);
        pList.add(Planet.VENUS);
        pList.add(Planet.MARS);
        pList.add(Planet.JUPITER);
        pList.add(Planet.SATURN);
        pList.add(Planet.URANUS);
        pList.add(Planet.NEPTUNE);
        pList.add(Planet.PLUTO);
        pList.add(Planet.RAHU);
        pList.add(Planet.KETU);
        
        return pList;
    }
    
    public boolean isPlanetAfterSaturn()
    {
        boolean flag = (index >= SE_URANUS && index <= SE_PLUTO);
        
        return flag;
    }

    @Override
    public String toString()
    {
        return getName();
    }
}
