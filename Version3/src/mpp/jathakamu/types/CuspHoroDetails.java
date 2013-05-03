package mpp.jathakamu.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mpp.jathakamu.Constants.SIGNIFICATOR_LEVELS;

public class CuspHoroDetails
    implements Serializable
{

    private static final long serialVersionUID = 1250181816959038763L;

    /*
     * Cusp/House I, II, .... XII
     */
    private int cusp = 1;
    private Raasi positedRaasi;
    private double longitude = 0D;
    /*
     * Planet in the cusp
     */
    private List<Planet> planets = new ArrayList<Planet>();
    
    /*
     * sign lord, star lord, sub lords list will be stored in lords
     */
    private List<Planet> lords = new ArrayList<Planet>();
    
    private List<List<Planet>> planetSignificators = new ArrayList<List<Planet>>();

    public CuspHoroDetails()
    {
        super();
    }
    
    public CuspHoroDetails(int cusp, double longitude)
    {
        super();
        this.cusp = cusp;
        
        this.longitude = longitude;
    }

    public CuspHoroDetails(int cusp, double longitude, List<Planet> planets)
    {
        super();
        this.cusp = cusp;
        
        this.longitude = longitude;
        this.planets = planets;
    }

    public int getCusp()
    {
        return cusp;
    }

    public void setCusp(int cusp)
    {
        this.cusp = cusp;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public Raasi getPositedRaasi()
    {
        return positedRaasi;
    }

    public void setPositedRaasi(Raasi positedRaasi)
    {
        this.positedRaasi = positedRaasi;
    }

    public List<Planet> getPlanets()
    {
        return planets;
    }

    public void setPlanets(List<Planet> planets)
    {
        this.planets = planets;
    }
    
    public void addPlanets(Planet p)
    {
        planets.add(p);
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
    
    public List<List<Planet>> getPlanetSignificators()
    {
        return planetSignificators;
    }
    
    public List<Planet> getPlanetSignificators(SIGNIFICATOR_LEVELS level)
    {
        return planetSignificators.get(level.ordinal());
    }
    
    public void setPlanetSignificators(List<List<Planet>> planetSignificators)
    {
        this.planetSignificators = planetSignificators;
    }
    
    public void addPlanetSignificator(SIGNIFICATOR_LEVELS level, Planet planet)
    {
        if (planet.isPlanetAfterSaturn())
            return;
        
        if (planetSignificators.size() == 0)
        {
            initializeCuspSignificators();
        }
        
        List<Planet> list = planetSignificators.get(level.ordinal());
        
        list.add(planet);
    }
    
    private void initializeCuspSignificators()
    {
        for (SIGNIFICATOR_LEVELS level : SIGNIFICATOR_LEVELS.values())
        {
            planetSignificators.add(level.ordinal(), new ArrayList<Planet>());
        }
    }
}
