package mpp.jathakamu.types;

import static swisseph.SweConst.SE_JUPITER;
import static swisseph.SweConst.SE_MARS;
import static swisseph.SweConst.SE_MERCURY;
import static swisseph.SweConst.SE_MOON;
import static swisseph.SweConst.SE_NEPTUNE;
import static swisseph.SweConst.SE_PLUTO;
import static swisseph.SweConst.SE_SATURN;
import static swisseph.SweConst.SE_SUN;
import static swisseph.SweConst.SE_URANUS;
import static swisseph.SweConst.SE_VENUS;

import java.util.List;

import mpp.jathakamu.Constants.ASPECT_EFFECT;
import mpp.jathakamu.settings.AspectOrbsSettings;
import mpp.jathakamu.settings.ConjunctionOrbsSettings;
import mpp.jathakamu.utils.Utils;


public class AspectsTable
{
    public final static int ASPECT_SUN_INDEX = SE_SUN;
    public final static int ASPECT_MOO_INDEX = SE_MOON;
    public final static int ASPECT_MER_INDEX = SE_MERCURY;
    public final static int ASPECT_VEN_INDEX = SE_VENUS;
    public final static int ASPECT_MAR_INDEX = SE_MARS;
    public final static int ASPECT_JUP_INDEX = SE_JUPITER;
    public final static int ASPECT_SAT_INDEX = SE_SATURN;
    public final static int ASPECT_URA_INDEX = SE_URANUS;
    public final static int ASPECT_NEP_INDEX = SE_NEPTUNE;
    public final static int ASPECT_PLU_INDEX = SE_PLUTO;
    public final static int ASPECT_RAH_INDEX = 11;
    public final static int ASPECT_KET_INDEX = 12;

    public final static int ASPECT_ASC_INDEX = 0;
    public final static int ASPECT_CUSP2_INDEX = 1;
    public final static int ASPECT_CUSP3_INDEX = 2;
    public final static int ASPECT_CUSP4_INDEX = 3;
    public final static int ASPECT_CUSP5_INDEX = 4;
    public final static int ASPECT_CUSP6_INDEX = 5;
    public final static int ASPECT_CUSP7_INDEX = 6;
    public final static int ASPECT_CUSP8_INDEX = 7;
    public final static int ASPECT_CUSP9_INDEX = 8;
    public final static int ASPECT_CUSP10_INDEX = 9;
    public final static int ASPECT_CUSP11_INDEX = 10;
    public final static int ASPECT_CUSP12_INDEX = 11;
    
    private AspectInfo [][]ASPECTS_PLANET_TO_PLANET= new AspectInfo[12][12];
    private AspectInfo [][]ASPECTS_PLANET_TO_CUSP= new AspectInfo[12][12];
    
    public AspectsTable()
    {
        super();
    }
    
    public AspectsTable(List<PlanetHoroDetails> phd, List<CuspHoroDetails> chd)
    {
        // index i will correspond to From planet
        for (int i = 0; i < 12; i++)
        {
            // index j will correspond to To planet
            PlanetHoroDetails fromPhd = phd.get(i);
            for (int j = 0; j < 12; j++)
            {
                PlanetHoroDetails toPhd = phd.get(j);
                
                double fromLongitude = fromPhd.getLongitude();
                double toLongitude = toPhd.getLongitude();
                double aspectLongitude = (toLongitude - fromLongitude);
                
                if (aspectLongitude < 0)
                {
                    aspectLongitude += 360;
                }
                
                ASPECTS_PLANET_TO_PLANET[i][j] = new AspectInfo(aspectLongitude, i, j, ASPECT_EFFECT.NONE);
                setAspectAttr(ASPECTS_PLANET_TO_PLANET[i][j]);
                
                if (chd == null)
                {
                    continue;
                }

                // To Cusp
                CuspHoroDetails toCusp = chd.get(j);
                double toCuspLongitude = toCusp.getLongitude();
                aspectLongitude = (toCuspLongitude - fromLongitude);
                
                if (aspectLongitude < 0)
                {
                    aspectLongitude += 360;
                }
                
                ASPECTS_PLANET_TO_CUSP[i][j] = new AspectInfo(aspectLongitude, i, toCusp.getCusp());
                setAspectAttr(ASPECTS_PLANET_TO_CUSP[i][j]);
            }
        }
    }


    public AspectInfo[][] getPlanetToPlanetAspects()
    {
        int len = ASPECTS_PLANET_TO_PLANET.length;
        AspectInfo [][]rtnAspects = new AspectInfo[len][len];
        
        System.arraycopy(ASPECTS_PLANET_TO_PLANET, 0, rtnAspects, 0, len);
        
        return rtnAspects;
    }

    public AspectInfo[][] getPlanetToCuspAspects()
    {
        int len = ASPECTS_PLANET_TO_CUSP.length;
        AspectInfo [][]rtnAspects = new AspectInfo[len][len];
        
        System.arraycopy(ASPECTS_PLANET_TO_CUSP, 0, rtnAspects, 0, len);
        
        return rtnAspects;
    }
    
    private void setAspectAttr(AspectInfo aspectInfo)
    {
        int fromPlanetIndex = aspectInfo.getFrom();
        Planet fromPlanet = Planet.getPlanet(fromPlanetIndex);
        double longitude = aspectInfo.getLongitude();
        double[] orb = ConjunctionOrbsSettings.getOrb(fromPlanet);

        if (orb != null)
        {
            boolean flag = Utils.valueIn(-orb[0], orb[1], longitude);

            if (flag)
            {
                aspectInfo.setAspect(ASPECT_EFFECT.CONJUNCTION);
                return;
            }
        }

        AspectOrbsSettings.setAspectAttr(aspectInfo);
    }
}
