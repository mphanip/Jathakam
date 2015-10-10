package mpp.jathakam.services.types;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import mpp.jathakam.services.Constants;

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
    URANUS(SE_URANUS, Collections.<Raasi>emptySet(), 0, Collections.<Nakshatram>emptySet()),
    NEPTUNE(SE_NEPTUNE, Collections.<Raasi>emptySet(), 0, Collections.<Nakshatram>emptySet()),
    PLUTO(SE_PLUTO, Collections.<Raasi>emptySet(), 0, Collections.<Nakshatram>emptySet());

    private final int index;
    private final Set<Raasi> raasi;
    private final int dasaDuration;
    private final Set<Nakshatram> stars;
    private final static List<Planet> VDPLANETS = Arrays.asList(KETU,VENUS,SUN,MOON,MARS,RAHU,JUPITER,SATURN,MERCURY);
    private final static List<Planet> ALL_PLANETS = Arrays.asList(Planet.SUN, Planet.MOON, Planet.MERCURY,
                            Planet.VENUS, Planet.MARS, Planet.JUPITER, Planet.SATURN,
                            Planet.URANUS, Planet.NEPTUNE, Planet.PLUTO, Planet.RAHU,
                            Planet.KETU);

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
            {
                return p;
            }
        }

        return null;
    }

    public static Planet getSignLord(Raasi sign)
    {
        for (Planet p : values())
        {
            Set<Raasi> signs = p.raasi;

            if (signs.contains(sign))
            {
                return p;
            }
        }

        return null;
    }

    public static Planet getPlanetSign(Raasi sign)
    {
        for (Planet p : values())
        {
            Set<Raasi> signs = p.raasi;

            if (signs.contains(sign))
            {
                return p;
            }
        }

        return null;
    }

    public String getName()
    {
        return Constants.PLANET_NAMES.get(index);
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
        return VDPLANETS;
    }

    public static List<Planet> getPlanetsAsList()
    {
        return ALL_PLANETS;
    }

    /**
     * Same as getVDPlanetStream(KETU)
     *
     * @return Stream of Planets
     * @see Planet.getVDPlanetStream(Planet)
     */
    public static Stream<Planet> getVDPlanetStream()
    {
        return getVDPlanetStream(KETU);
    }

    /**
     * Get stream of Vimshottari Dasa sequence of planets starting with
     * startingPlanet
     *
     * @param startingPlanet
     * @return Stream<Planet> with Vimshottari Dasa sequence of planets, so that
     * calling method can iterate (for each) thru the stream.
     */
    public static Stream<Planet> getVDPlanetStream(Planet startingPlanet)
    {
        int startIndex = startingPlanet.ordinal();
        int endIndex = MERCURY.ordinal();
        int firstPlanetIndex = KETU.ordinal();
        Stream.Builder<Planet> planets = Stream.builder();

        for (int index = startIndex; index <= endIndex; index++)
        {
            planets.add(Planet.getPlanetByOrdinal(index));
        }

        for (int index = firstPlanetIndex; index < startIndex; index++)
        {
            planets.add(Planet.getPlanetByOrdinal(index));
        }

        Stream<Planet> planetsStream = planets.build();
        return planetsStream;
    }

    /**
     * Get stream of Vimshottari Dasa sequence of planets starting with
     * startingPlanet
     *
     * @param dasaStartingPlanet
     * @param currentDasaPlanet
     * @return Stream<Planet> with Vimshottari Dasa sequence of planets, so that
     * calling method can iterate (for each) thru the stream.
     */
    public static Stream<Planet> getVDPlanetStream(Planet dasaStartingPlanet,
            Planet currentDasaPlanet)
    {
        List<Planet> rtnPlanet = getVDPlanets(dasaStartingPlanet, currentDasaPlanet);

        return rtnPlanet.stream();
    }

    /**
     * Get stream of Vimshottari Dasa sequence of planets starting with
     * startingPlanet
     *
     * @param dasaStartingPlanet
     * @param currentDasaPlanet
     * @return Stream<Planet> with Vimshottari Dasa sequence of planets, so that
     * calling method can iterate (for each) thru the stream.
     */
    public static List<Planet> getVDPlanets(Planet dasaStartingPlanet,
            Planet currentDasaPlanet)
    {
        List<Planet> vdPlanetList = getVimshottariDasaPlanets();
        int index = vdPlanetList.indexOf(dasaStartingPlanet);
        Collections.rotate(vdPlanetList, -index);
        int currentIndex = vdPlanetList.indexOf(currentDasaPlanet);
        List<Planet> rtnPlanet = vdPlanetList.subList(currentIndex, vdPlanetList.size());

        return rtnPlanet;
    }

    public static Planet[] getVDPlanetArray(Planet startingPlanet)
    {
        Planet[] rtnVDPlanets = new Planet[9];
        int startIndex = startingPlanet.ordinal();
        int endIndex = MERCURY.ordinal();
        int firstPlanetIndex = KETU.ordinal();
        int i = 0;

        for (int index = startIndex; index <= endIndex; index++)
        {
            rtnVDPlanets[i++] = Planet.getPlanetByOrdinal(index);
        }

        for (int index = firstPlanetIndex; index < startIndex; index++)
        {
            rtnVDPlanets[i++] = Planet.getPlanetByOrdinal(index);
        }

        return rtnVDPlanets;
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
