package mpp.jathakamu.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mpp.jathakamu.Constants.SIGNIFICATOR_LEVELS;

/**
 * Planet details having all the information. There will be one instance for
 * each planet
 * 
 * @author pmulakal
 *
 */
public class PlanetHoroDetails
    implements Serializable
{

    private static final long serialVersionUID = 8132975601611390413L;
    
    /*
     * planet, longitude, retrograde and posited raasi are all populated when
     * instantiated object is created.
     */
    private Planet planet;
    private double longitude = 0D;
    private boolean retrograde = false;
    private Raasi positedRaasi = Raasi.NONE;
    
    /*
     * planet in whose cusp it is posited. Valid values are 1 to 12 (both
     * inclusive) 
     */
    private int cusp = 0;
    
    /*
     * Following variables are instantiated when Horoscope.analyze is called.
     */
    private List<Integer> houseLord = new ArrayList<Integer>();
    private List<Planet> lords = new ArrayList<Planet>();
    private List<Planet> goodPlanetAspects = new ArrayList<Planet>();
    private List<Planet> badPlanetAspects = new ArrayList<Planet>();
    private List<Integer> goodCuspAspects = new ArrayList<Integer>();
    private List<Integer> badCuspAspects = new ArrayList<Integer>();
    
    /*
     * When populating significators make sure to use SIGNIFICATOR_LEVELS
     */
    private List<List<Integer>> cuspSignificators = new ArrayList<List<Integer>>();

    /*
     * specialPlanetAspects and specialCuspAspects are for Jupiter, Saturn, and
     * Mars
     */
    private List<Planet> specialPlanetAspects = new ArrayList<Planet>();
    private List<Integer> specialCuspAspects = new ArrayList<Integer>();
    private List<Planet> seventhAspect = new ArrayList<Planet>();
    private List<Planet> conjuctionPlanets = new ArrayList<Planet>();
    
    /*
     * The list contains all the planets having this planet as Nakshatra lord.
     * This list will be used to return "No planet in its star" and
     * "planet in its own star"
     */
    private List<Planet> starLordForPlanets = new ArrayList<Planet>();
    
    /*
     * whether the planet is benefic or melefic based on the Horoscope
     * 
     * Planet is melefic when it is lord of 6/8/12  otherwise benefic
     */
    private boolean benefic = false;
    private boolean melefic = false;
    
    public PlanetHoroDetails(Planet planet, double longitude, boolean retrograde)
    {
        super();
        this.planet = planet;
        this.longitude = longitude;
        this.retrograde = retrograde;
    }

    public Planet getPlanet()
    {
        return planet;
    }

    public void setPlanet(Planet planet)
    {
        this.planet = planet;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public boolean isRetrograde()
    {
        return retrograde;
    }

    public void setRetrograde(boolean retrograde)
    {
        this.retrograde = retrograde;
    }

    public Raasi getPositedRaasi()
    {
        return positedRaasi;
    }

    public void setPositedRaasi(Raasi positedRaasi)
    {
        this.positedRaasi = positedRaasi;
    }

    public int getCusp()
    {
        return cusp;
    }

    public void setCusp(int cusp)
    {
        this.cusp = cusp;
    }

    public List<Integer> getHouseLord()
    {
        return houseLord;
    }

    public void setHouseLord(List<Integer> houseLord)
    {
        this.houseLord = houseLord;
    }
    
    public void addHouseLord(Integer houseLord)
    {
        this.houseLord.add(houseLord);
    }

    public List<Planet> getLords()
    {
        return lords;
    }

    public void setLords(List<Planet> lords)
    {
        this.lords = lords;
    }
    
    public void addLord(Planet planet)
    {
        lords.add(planet);
    }
    
    public Planet getSignLord()
    {
        Planet signLord = lords.get(0);
        
        return signLord;
    }
    
    public Planet getNakshatraLord()
    {
        Planet starLord = lords.get(1);
        
        return starLord;
    }
    
    /**
     * 
     * @param level should greater than 0 (i.e. level start from 1) and maximum
     * value is 3
     * 
     * @return sub lord planet
     */
    public Planet getSubLord(int level)
    {
        if (level < 1)
        {
            return null;
        }
        
        level++;
        
        if (level > lords.size())
        {
            return null;
        }
        
        Planet subLord = lords.get(level);
        
        return subLord;
    }

    public List<Planet> getGoodPlanetAspects()
    {
        return goodPlanetAspects;
    }

    public void setGoodPlanetAspects(List<Planet> goodPlanetAspects)
    {
        this.goodPlanetAspects = goodPlanetAspects;
    }
    
    public void addGoodPlanetAspects(Planet goodPlanet)
    {
        goodPlanetAspects.add(goodPlanet);
    }

    public List<Planet> getBadPlanetAspects()
    {
        return badPlanetAspects;
    }

    public void setBadPlanetAspects(List<Planet> badPlanetAspects)
    {
        this.badPlanetAspects = badPlanetAspects;
    }
    
    public void addBadPlanetAspects(Planet badPlanet)
    {
        badPlanetAspects.add(badPlanet);
    }

    public List<Integer> getGoodCuspAspects()
    {
        return goodCuspAspects;
    }

    public void setGoodCuspAspects(List<Integer> goodCuspAspects)
    {
        this.goodCuspAspects = goodCuspAspects;
    }
    
    public void addGoodCuspAspects(Integer goodCusp)
    {
        goodCuspAspects.add(goodCusp);
    }

    public List<Integer> getBadCuspAspects()
    {
        return badCuspAspects;
    }

    public void setBadCuspAspects(List<Integer> badCuspAspects)
    {
        this.badCuspAspects = badCuspAspects;
    }
    
    public void addBadCuspAspects(Integer badCusp)
    {
        this.badCuspAspects.add(badCusp);
    }

    public List<Planet> getSpecialPlanetAspects()
    {
        return specialPlanetAspects;
    }

    public void setSpecialPlanetAspects(List<Planet> specialPlanetAspects)
    {
        this.specialPlanetAspects = specialPlanetAspects;
    }
    
    public void addSpecialPlanetAspects(Planet planet)
    {
        specialPlanetAspects.add(planet);
    }

    public List<Integer> getSpecialCuspAspects()
    {
        return specialCuspAspects;
    }

    public void setSpecialCuspAspects(List<Integer> specialCuspAspects)
    {
        this.specialCuspAspects = specialCuspAspects;
    }
    
    public void addSpecialCuspAspects(Integer cusp)
    {
        specialCuspAspects.add(cusp);
    }

    public List<List<Integer>> getCuspSignificators()
    {
        return cuspSignificators;
    }
    
    public List<Integer> getCuspSignificators(SIGNIFICATOR_LEVELS level)
    {
        return cuspSignificators.get(level.ordinal());
    }

    public void setCuspSignificators(List<List<Integer>> cuspSignificators)
    {
        this.cuspSignificators = cuspSignificators;
    }
    
    public void addCuspSignificator(SIGNIFICATOR_LEVELS level, Integer cusp)
    {
        if (cuspSignificators.size() == 0)
        {
            initializeCuspSignificators();
        }

        List<Integer> list = cuspSignificators.get(level.ordinal());
        
        list.add(cusp);
    }

    public boolean isBenefic()
    {
        return benefic;
    }

    public void setBenefic(boolean benfic)
    {
        this.benefic = benfic;
    }

    public boolean isMelefic()
    {
        return melefic;
    }

    public void setMelefic(boolean melefic)
    {
        this.melefic = melefic;
    }

    public boolean hasNoPlanetInItsStar()
    {
        boolean flag = (starLordForPlanets.size() == 0);
        return flag;
    }
    
    public List<Planet> getStarLordForPlanets()
    {
        return starLordForPlanets;
    }

    public void setStarLordForPlanets(List<Planet> starLordForPlanets)
    {
        this.starLordForPlanets = starLordForPlanets;
    }
    
    public void addStarLordForPlanet(Planet starLordForPlanet)
    {
        if (starLordForPlanet.isPlanetAfterSaturn())
            return;

        starLordForPlanets.add(starLordForPlanet);
    }

    public boolean isPlanetInItsOwnStar()
    {
        boolean flag = starLordForPlanets.contains(planet);
        return flag;
    }

    public List<Planet> getSeventhAspect()
    {
        return seventhAspect;
    }

    public void setSeventhAspect(List<Planet> seventhAspect)
    {
        this.seventhAspect = seventhAspect;
    }
    
    public void addSeventhAspect(Planet planet)
    {
        seventhAspect.add(planet);
    }
    
    private void initializeCuspSignificators()
    {
        for (int i = 0; i < 6; i++)
        {
            cuspSignificators.add(i, new ArrayList<Integer>());
        }
    }

    public List<Planet> getConjuctionPlanets()
    {
        return conjuctionPlanets;
    }

    public void setConjuctionPlanets(List<Planet> conjuctionPlanets)
    {
        this.conjuctionPlanets = conjuctionPlanets;
    }
    
    public void addConjuctionPlanet(Planet conjuctionPlanet)
    {
        conjuctionPlanets.add(conjuctionPlanet);
    }
}
