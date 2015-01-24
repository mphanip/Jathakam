/* 
 * Yet to decide on the license
 */

package mpp.jathakamu.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import static mpp.jathakamu.Constants.CUSP_AND_PLANETS;
import mpp.jathakamu.types.HouseEntity;
import mpp.jathakamu.types.HouseEntityComparator;
import mpp.jathakamu.types.Planet;

/**
 *
 * @author phani
 */
public class DefaultHouseInfo
    implements HouseInfo
{
    protected final List<List<HouseEntity>> houseEntities = new ArrayList<>();
    protected final List<String> information = new ArrayList<>();

    @Override
    public HouseEntity[] getHouseInfo(int housePosition)
    {
        List<HouseEntity> rtnValue = houseEntities.get(housePosition);
        HouseEntity[] hes = new HouseEntity[rtnValue.size()];

        rtnValue.toArray(hes);
        return hes;
    }

    protected void initializeHouseEntity()
    {
        houseEntities.clear();

        for (int i = 0; i < 12; i++)
        {
            houseEntities.add(new ArrayList());
        }
    }

    protected void setHouseEntity(CUSP_AND_PLANETS obj, double position,
            String info)
    {
        HouseEntity he = new HouseEntity(obj, position, info);
        int index = he.getOccupiedHouse();

        houseEntities.get(index).add(he);
    }

    protected void sortHouseEntities()
    {
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

    protected String getLordsString(List<Planet> lordsList)
    {
        String value = lordsList.stream()
                .limit(4)
                .map(planet -> planet.get2LetterName())
                .collect(Collectors.joining("-"))
                .toString();
        return "LORDS: " + value;
    }

    @Override
    public String[] getInformation()
    {
        String[] str = new String[information.size()];

        return information.toArray(str);
    }

    @Override
    public void clearInformation()
    {
        information.clear();
    }

    @Override
    public void addInformation(String info)
    {
        information.add(info);
    }
    
}
