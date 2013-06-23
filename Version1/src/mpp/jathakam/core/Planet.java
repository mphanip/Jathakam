package mpp.jathakam.core;

/**
 * @author phani
 */
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import swisseph.SweConst;

import mpp.jathakam.core.Nakshatram;
import mpp.jathakam.core.Planet;
import mpp.jathakam.core.Raasi;

public enum Planet
{
    KETU(11, EnumSet.of(Raasi.NONE), 7, EnumSet.of(Nakshatram.ASWINI, Nakshatram.MAKHA, Nakshatram.MOOLA)),
    VENUS(3, EnumSet.of(Raasi.LIBRA, Raasi.TARUS), 20, EnumSet.of(Nakshatram.BHARANI, Nakshatram.PURVA_PHALGUNI, Nakshatram.PURVA_ASHADA)),
    SUN(0, EnumSet.of(Raasi.LEO), 6, EnumSet.of(Nakshatram.KRITIKA, Nakshatram.UTTARA_PHALGUNI, Nakshatram.UTTARA_ASHADA)),
    MOON(1, EnumSet.of(Raasi.CANCER), 10, EnumSet.of(Nakshatram.ROHINI, Nakshatram.HASTA, Nakshatram.SHRAVANA)),
    MARS(4, EnumSet.of(Raasi.SCORPIO, Raasi.ARIES), 7, EnumSet.of(Nakshatram.MRIGHASIRA, Nakshatram.CHITRA, Nakshatram.DHANISHTA)),
    RAHU(10, EnumSet.of(Raasi.NONE), 18, EnumSet.of(Nakshatram.ARUDRA, Nakshatram.SWATHI, Nakshatram.SHATABHISHA)),
    JUPITER(5, EnumSet.of(Raasi.SAGITTARIUS, Raasi.PISCES), 16, EnumSet.of(Nakshatram.PUNARVASU, Nakshatram.VISAKHA, Nakshatram.PURVA_BHADRA)),
    SATURN(6, EnumSet.of(Raasi.CAPRICORN, Raasi.AQUARIUS), 19, EnumSet.of(Nakshatram.PUSHYAMI, Nakshatram.ANURADHA, Nakshatram.UTTARA_BHADRA)),
    MERCURY(2, EnumSet.of(Raasi.VIRGO, Raasi.GEMINI), 17, EnumSet.of(Nakshatram.ASLESHA, Nakshatram.JYESHTHA, Nakshatram.REVATHI));

    private int index = 0;
    private Set<Raasi> raasi = EnumSet.of(Raasi.LEO);
    private int dasaDuration = 6;
    private Set<Nakshatram> stars = EnumSet.of(Nakshatram.KRITIKA, Nakshatram.UTTARA_PHALGUNI, Nakshatram.UTTARA_ASHADA);

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
    
//    public static Planet[] dasaPlanetValues()
//    {
//    	Planet[] planet = new Planet[9];
//    	int index = 0;
//    	for (Planet p : values())
//    	{
//    		if (index > 8)
//    		{
//    			break;
//    		}
//    		planet[index++] = p;
//    	}
//    	
//    	return planet;
//    }

    public static Planet getPlanetStar(Nakshatram star)
    {
        for (Planet p : values())
        {
            Set<Nakshatram> stars = p.stars;

            if (stars.contains(star))
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
        return JathakamConstants.PLANET_NAMES[index];
    }
    
    public String getShortName()
    {
        return JathakamConstants.PLANET_SHORT_NAMES[index];
    }
    
    public String get2LetterName()
    {
        return JathakamConstants.PLANET_2_LETTER_NAMES[index];
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
    
    public static List<Planet> getPlanetList()
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
    
    public boolean isPlanetAfterSaturn()
    {
        if (index >= SweConst.SE_URANUS && index <= SweConst.SE_PLUTO)
        {
            return true;
        }
        
        return true;
    }

    @Override
    public String toString()
    {
        return getName();
    }
}
