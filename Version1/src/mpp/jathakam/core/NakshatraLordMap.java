package mpp.jathakam.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import swisseph.SweConst;

public class NakshatraLordMap
{
	Map<Integer, List<Planet>> lordsList = new HashMap<Integer, List<Planet>>(12);
	Map<Planet, List<Planet>> nakshatraLordMap = new HashMap<Planet, List<Planet>>(12);
	
	public NakshatraLordMap(List<PlanetCoordinates> planetsCoords)
	{
		initialize();
		setValues(planetsCoords);
	}
	
	private void initialize()
	{
		for (int i = 0; i < 12; i++)
		{
			nakshatraLordMap.put(Planet.getPlanet(i), new ArrayList<Planet>());
		}
	}
	
	private void setValues(List<PlanetCoordinates> planetsCoords)
	{
		for (PlanetCoordinates pc : planetsCoords)
		{
			List<Planet> planetLords = Lords.getPlanetLords(pc.getLongitude());
			int planetIndex = pc.getIndex();
			lordsList.put(planetIndex, planetLords);
			
			Planet planet = Planet.getPlanet(planetIndex);
			if (planet == null)
			{
				continue;
			}
			
			Planet nlPlanet = planetLords.get(1);  // index 1 is Nakshatra lord
			List<Planet> planetsInNL = nakshatraLordMap.get(nlPlanet);
			planetsInNL.add(planet); 
		}
	}
	
	public List<Planet> getLords(int planetIndex)
	{
		List<Planet> lordPlanets = lordsList.get(planetIndex);
		return new ArrayList<Planet>(lordPlanets);
	}
	
	public List<Planet> getPlanetsHavingNakshatraLord(Planet planet)
	{
		List<Planet> nlPlanets = nakshatraLordMap.get(planet);

		return new ArrayList<Planet>(nlPlanets);
	}
	
	public boolean hasNoPlanetInItsStar(Planet planet)
	{
		if (planet == null)
			return false;
		
		int index = planet.getIndex();
		boolean flag = isValidPlanet(index);
		
		if (!flag)
			return false;
		
		List<Planet> planets = getPlanetsHavingNakshatraLord(planet);
		int size = planets.size();
		return (size == 0);
	}
	
	public boolean isSelfStar(Planet planet)
	{
		if (planet == null)
			return false;
		
		int index = planet.getIndex();
		boolean flag = isValidPlanet(index);
		
		if (!flag)
			return false;

		List<Planet> planets = getPlanetsHavingNakshatraLord(planet);
		flag = planets.contains(planet);
		return flag;
	}
	
	private boolean isValidPlanet(int planetIndex)
	{
		if (planetIndex >= SweConst.SE_URANUS && planetIndex <= SweConst.SE_PLUTO)
		{
			return false;
		}
		
		// Fortuna
		if (planetIndex == 12)
		{
			return false;
		}
		
		return true;
	}
}
