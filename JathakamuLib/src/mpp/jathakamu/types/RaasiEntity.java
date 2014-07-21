/* 
 * Yet to decide on the license
 */

package mpp.jathakamu.types;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import mpp.jathakamu.Constants;
import mpp.jathakamu.Constants.ENTITY_TYPE;
import mpp.jathakamu.Constants.CUSP_AND_PLANETS;
import mpp.jathakamu.utils.ViewUtils;

/**
 * This is an immutable class
 * 
 * @author Phani Pramod M
 */
public class RaasiEntity
    implements Serializable, Comparable<RaasiEntity>, Comparator<RaasiEntity>
{
    private CUSP_AND_PLANETS cuspOrPlanet = null;
    private double position;
    private int occupiedHouse;
    private ENTITY_TYPE type;
    private String info = "No Information is available currently";
    private boolean ascending = true;
    
    public RaasiEntity(CUSP_AND_PLANETS value, double position)
    {
        this(value, position, null);
    }

    public RaasiEntity(CUSP_AND_PLANETS value, double position, String information)
    {
        this.cuspOrPlanet = value;
        this.position = position;
        
        if (info != null && info.trim().length() > 0)
        {
            this.info = information;
        }

        if (value.ordinal() > 11)
        {
            type = ENTITY_TYPE.PLANET;
        }
        else
        {
            type = ENTITY_TYPE.CUSP;
        }
        
        initialize();
    }
    
    public CUSP_AND_PLANETS getCuspOrPlanet()
    {
        return cuspOrPlanet;
    }
    
    public double getPosition()
    {
        return position;
    }
    
    public String getPositionInDegree()
    {
        return ViewUtils.toStringDegreeForRaasi(position);
    }
    
    public ENTITY_TYPE getType()
    {
        return type;
    }

    public int getOccupiedHouse()
    {
        return occupiedHouse;
    }
    
    public String getInfo()
    {
        return info;
    }

    public String getPlanetSign()
    {
        int ord = cuspOrPlanet.ordinal();
        String rtnStr;
        if (ord > 11)
        {
            rtnStr = Constants.PLANET_SIGN[cuspOrPlanet.ordinal()-12];
        }
        else
            rtnStr = cuspOrPlanet.toString();

        return rtnStr;
    }
    
    public String getPlanetName()
    {
        int ord = cuspOrPlanet.ordinal();
        String rtnStr;
        if (ord > 11)
        {
            rtnStr = Constants.PLANET_2_LETTER_NAMES[cuspOrPlanet.ordinal()-12];
        }
        else
            rtnStr = cuspOrPlanet.toString();

        return rtnStr;
    }

    public boolean isAscending()
    {
        return ascending;
    }

    public void setAscending(boolean ascending)
    {
        this.ascending = ascending;
    }
    
    public String toStringWithSign()
    {
        String planetSign = getPlanetSign();
        
        return planetSign + " " + position;
    }
    
    @Override
    public String toString()
    {
        return cuspOrPlanet + " " + position;
    }


    @Override
    public int compareTo(RaasiEntity o)
    {
        // compare depends on the position of the planet or cusp
        // Here we can either do Math.ceil or the following logic, to avoid
        // return the same int when the diff is less than 1. Revisit to check
        // if there is a better way to do it.
        return (int)(position*1000000 - o.position*1000000);
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.cuspOrPlanet);
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.position) ^ (Double.doubleToLongBits(this.position) >>> 32));
        hash = 67 * hash + Objects.hashCode(this.type);
        
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final RaasiEntity other = (RaasiEntity) obj;
        if (this.cuspOrPlanet != other.cuspOrPlanet)
        {
            return false;
        }
        if (Double.doubleToLongBits(this.position) != Double.doubleToLongBits(other.position))
        {
            return false;
        }
        if (this.type != other.type)
        {
            return false;
        }

        return true;
    }
    
    @Override
    public int compare(RaasiEntity o1, RaasiEntity o2)
    {
        if (this.ascending)
            return o1.compareTo(o2);
        else
            return o2.compareTo(o1);
    }

    private void initialize()
    {
        // Get Raasi for the cusp/planet
        // subtracting -1 because the first Raasi in Enum is None.
        occupiedHouse = Raasi.getRaasi(position).ordinal()-1;
        
        info = cuspOrPlanet + "\n" + info;
    }

}
