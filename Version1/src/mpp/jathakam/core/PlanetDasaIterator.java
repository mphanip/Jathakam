package mpp.jathakam.core;

import java.util.Iterator;

import mpp.jathakam.core.Planet;

public class PlanetDasaIterator
    implements Iterable<Planet>, Iterator<Planet>
{
    private final static Planet[] allPlanets = Planet.values();

    private Planet start = null;
    private Planet next = null;
    private int count = 0;

    public PlanetDasaIterator(Planet start)
    {
        this.start = start;
        next = start;
    }

    @Override
    public boolean hasNext()
    {
        return (count < 9);
    }

    @Override
    public Planet next()
    {
        if (count == 0)
        {
            count++;
            return next;
        }

        int index = next.ordinal() + 1;
        index = index % 9;
        next = allPlanets[index];
        count++;

        return next;
    }

    @Override
    public void remove()
    {
        // Do not implement, nothing to remove, kind of immutable class
    }

    @Override
    public Iterator<Planet> iterator()
    {
        next = start;
        count = 0;
        return this;
    }

}
