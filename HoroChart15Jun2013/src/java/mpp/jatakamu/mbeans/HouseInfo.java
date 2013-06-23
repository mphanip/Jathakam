/*
 * House info contains each house having planets and cusps.
 */
package mpp.jatakamu.mbeans;

import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import static mpp.jatakamu.Constants.CUSP_AND_PLANTS;

import mpp.jatakamu.JatakamuLogger;
import mpp.jatakamu.types.HouseEntity;
import mpp.jatakamu.types.HouseEntityComparator;

/**
 *
 * @author pmulakal
 */
@Named(value = "houseInfo")
@SessionScoped
public class HouseInfo
    implements Serializable
{
    
    @Inject ChartInfo chartInfo;
    
    private List<List<HouseEntity>> houseEntities = new ArrayList();
    
    /**
     * Creates a new instance of HouseInfo
     */
    public HouseInfo()
    {
        JatakamuLogger.log("creating HouseInfo instance..." + hashCode());
        initialize();
    }
    
    public int getHouseInfoSize(int housePosition)
    {
        return houseEntities.get(housePosition).size();
    }
    
    /*
     * Temporary use
     */
    public String getInfo(int housePosition)
    {
//        Set<HouseEntity> hes = getHouseInfo(housePosition);
        HouseEntity[] hes = getHouseInfo(housePosition);
        StringBuilder sb = new StringBuilder();
        
//        for (Iterator<HouseEntity> it = hes.iterator(); it.hasNext();)
        for (HouseEntity h : hes)
        {
//            HouseEntity houseEntity = it.next();
//            sb.append(houseEntity).append(" ");
             sb.append(h).append(" ");
        }
        
        return sb.toString();
    }
    
    public HouseEntity[] getHouseInfo(int housePosition)
    {
        List<HouseEntity> rtnValue = houseEntities.get(housePosition);
        HouseEntity[] hes = new HouseEntity[rtnValue.size()];
        
//        if (housePosition > 5)
//        {
//            rtnValue = ((TreeSet)rtnValue).descendingSet();
//        }
        rtnValue.toArray(hes);
        
        return hes;
    }
    
    public boolean hasData(int position)
    {
        if (houseEntities == null)
        {
            return false;
        }
        List<HouseEntity> bd = houseEntities.get(position);
        
        boolean rtnValue = bd.isEmpty();
        
        return rtnValue;
    }
    
    private void initialize()
    {
        houseEntities.clear();
        
        for (int i =0; i < 12; i++)
        {
            houseEntities.add(new ArrayList());
        }
    }

    public void calculate()
    {
        initialize();
        JatakamuLogger.log("Calculating....");

        for (int i = 0; i < 12; i++)
        {
            JatakamuLogger.log("cusp " + (i+1));
            CUSP_AND_PLANTS cusp = CUSP_AND_PLANTS.values()[i];
            double val = chartInfo.getCuspPosition(i);
            HouseEntity he = new HouseEntity(cusp, val);
            int index = he.getOccupiedHouse();

            houseEntities.get(index).add(he);
        }
        JatakamuLogger.log("Calculating Cusps Done...");
        
        for (int i = 0; i < 9; i++)
        {
            CUSP_AND_PLANTS planet = CUSP_AND_PLANTS.values()[12+i];
            double val = chartInfo.getPlanetPosition(i);
            HouseEntity he = new HouseEntity(planet, val);
            int index = he.getOccupiedHouse();

            houseEntities.get(index).add(he);
        }

        JatakamuLogger.log("Planet Done....");
        
        // Sort the list
        for (int i = 0; i < 12; i++)
        {
            List<HouseEntity> list = houseEntities.get(i);
            if (i < 6)
            {
                Collections.sort(list, new HouseEntityComparator(true));
            }
            else
            {
                Collections.sort(list, new HouseEntityComparator(false));
            }
        }
        
    }
    /*
     * For debug
     */
    public void showInfo(String value)
    {
        JatakamuLogger.log("House Info: showInfo Value = " + value);
    }
}
