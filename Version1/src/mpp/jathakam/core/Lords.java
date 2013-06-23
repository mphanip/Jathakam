/*
 *
 *
 */
package mpp.jathakam.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import mpp.jathakam.core.Planet;
import mpp.jathakam.core.PlanetDasaIterator;
import mpp.jathakam.core.Raasi;
import mpp.jathakam.core.Range;

/**
 * Given a longitude, calculates Sign lord, star/Nakshatra lord, Sub-Lord,
 * Sub-Sub-Lord, etc
 *
 * @author phani
 */
public class Lords
{
    private final static NavigableMap<Range, List<Planet>> lordsTable
        = new ConcurrentSkipListMap<Range, List<Planet>>();

    static
    {
        double start = 0;
        double end = 0;

        for (int i = 0; i < 3; i++)
        {
            for (Planet mahaDasaPlanet : Planet.values())
            {
                PlanetDasaIterator bukthi = new PlanetDasaIterator(mahaDasaPlanet);

                for (Planet bukthiPlanet : bukthi)
                {
                    PlanetDasaIterator pratyantara = new PlanetDasaIterator(bukthiPlanet);
                    for (Planet pratyantaraPlanet : pratyantara)
                    {
                        double dasaduration = 800D;
                        dasaduration *= bukthiPlanet.getDasaDuration();
                        dasaduration *= pratyantaraPlanet.getDasaDuration();

                        end += dasaduration / (120D * 120D * 60D);

                        List<Planet> planetList = new ArrayList<Planet>();
                        Raasi planetSign = Raasi.getRaasi(end);
                        Planet signStar = Planet.getPlanetSign(planetSign);
                        planetList.add(signStar);
                        planetList.add(mahaDasaPlanet);
                        planetList.add(bukthiPlanet);
                        planetList.add(pratyantaraPlanet);

                        if (isSignChanged(start, end))
                        {
                            double newEnd1 = Math.floor(end);
                            lordsTable.put(new Range(start, newEnd1), planetList);
                            lordsTable.put(new Range(newEnd1, end), new ArrayList<Planet>(planetList));
                        }
                        else
                        {
                            lordsTable.put(new Range(start, end), planetList);
                        }

                        start = end;
                    }
                }
            }
        }
    }

    private static boolean isSignChanged(double start, double end)
    {
        for (int i = 1; i <= 12; i++)
        {
            double t = 30.0 * i;
            int j = 30 * i;

            if (j == 120 || j == 240 || j == 360 )
                continue;

            if (start < t && end > t)
                return true;
        }

        return false;
    }

    public static void dumpLordsTable()
    {

        /* Just for debugging purpose printing the table */
        for (Map.Entry<Range,List<Planet>> entry : lordsTable.entrySet())
        {
            Range key = entry.getKey();
            List<Planet> value = entry.getValue();

            System.out.println(key + "=" + value);
        }
    }

    public static List<Planet> getPlanetLords(double planetLongitude)
    {
        Map.Entry<Range, List<Planet>> entry = lordsTable.ceilingEntry(new Range(0D, planetLongitude));
        List<Planet> rtnList = entry.getValue();

        return rtnList;
    }
}
