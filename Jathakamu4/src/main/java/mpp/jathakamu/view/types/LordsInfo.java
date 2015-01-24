    /* 
 * Yet to decide on the license
 */

package mpp.jathakamu.view.types;

import java.io.Serializable;
import java.util.List;
import mpp.jathakamu.types.Planet;
import mpp.jathakamu.utils.ViewUtils;

/**
 *
 * @author phani
 */
public class LordsInfo
    implements Serializable
{
    private String name;
    private String longitude;
    private String signLord;
    private String starLord;
    private String subLord;
    private String sub2Lord;
    private String sub3Lord;
    private String sub4Lord;

    public LordsInfo(String longitude, String starLord, String subLord,
            String sub2Lord, String sub3Lord, String sub4Lord)
    {
        this.longitude = longitude;
        this.starLord = starLord;
        this.subLord = subLord;
        this.sub2Lord = sub2Lord;
        this.sub3Lord = sub3Lord;
        this.sub4Lord = sub4Lord;
    }
    
    public LordsInfo(String name, double longitude, List<Planet> lordsList)
    {
        this.name = name;
        this.longitude = " [" + ViewUtils.toStringDegree(longitude) + "]";
        signLord = lordsList.get(0).getShortName();
        starLord = lordsList.get(1).getShortName();
        subLord = lordsList.get(2).getShortName();
        sub2Lord = lordsList.get(3).getShortName();
        sub3Lord = lordsList.get(4).getShortName();
        sub4Lord = lordsList.get(5).getShortName();
    }
    
    
    public LordsInfo(int index, double longitude, List<Planet> lordsList)
    {
        this.name = String.valueOf(index);
        this.longitude = " [" + ViewUtils.toStringDegree(longitude) + "]";
        signLord = lordsList.get(0).getShortName();
        starLord = lordsList.get(1).getShortName();
        subLord = lordsList.get(2).getShortName();
        sub2Lord = lordsList.get(3).getShortName();
        sub3Lord = lordsList.get(4).getShortName();
        sub4Lord = lordsList.get(5).getShortName();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    public String getSignLord()
    {
        return signLord;
    }

    public void setSignLord(String signLord)
    {
        this.signLord = signLord;
    }

    public String getStarLord()
    {
        return starLord;
    }

    public void setStarLord(String starLord)
    {
        this.starLord = starLord;
    }

    public String getSubLord()
    {
        return subLord;
    }

    public void setSubLord(String subLord)
    {
        this.subLord = subLord;
    }

    public String getSub2Lord()
    {
        return sub2Lord;
    }

    public void setSub2Lord(String sub2Lord)
    {
        this.sub2Lord = sub2Lord;
    }

    public String getSub3Lord()
    {
        return sub3Lord;
    }

    public void setSub3Lord(String sub3Lord)
    {
        this.sub3Lord = sub3Lord;
    }

    public String getSub4Lord()
    {
        return sub4Lord;
    }

    public void setSub4Lord(String sub4Lord)
    {
        this.sub4Lord = sub4Lord;
    }
}
