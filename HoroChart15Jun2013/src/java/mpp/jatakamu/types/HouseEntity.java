package mpp.jatakamu.types;

import java.io.Serializable;
import java.util.Objects;

import static mpp.jatakamu.Constants.CUSP_AND_PLANTS;
import static mpp.jatakamu.Constants.PLANET_SIGN;
import static mpp.jatakamu.Constants.ENTITY_TYPE;

import mpp.jatakamu.JatakamuLogger;
import mpp.jatakamu.utils.CalcUtils;
import mpp.jatakamu.utils.ViewUtils;

/**
 * This is an immutable class
 * 
 * @author Phani Pramod M
 */
public class HouseEntity
    implements Serializable, Comparable<HouseEntity>
{
    private CUSP_AND_PLANTS cuspOrPlanet = null;
    private double position;
    private int occupiedHouse;
    private ENTITY_TYPE type;
    private String info = "No Information is available currently";

    public HouseEntity(CUSP_AND_PLANTS value, double position)
    {
        this.cuspOrPlanet = value;
        this.position = position;

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
    
    public CUSP_AND_PLANTS getCuspOrPlanet()
    {
        return cuspOrPlanet;
    }
    
    public double getPosition()
    {
        return position;
    }
    
    public String getPositionInDegree()
    {
        return ViewUtils.toStringDegree3(position);
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
        String rtnStr = "";
        if (ord > 11)
        {
            rtnStr = PLANET_SIGN[cuspOrPlanet.ordinal()-12];
        }
        else
            rtnStr = cuspOrPlanet.toString();

        return rtnStr;
    }

    @Override
    public String toString()
    {
        return cuspOrPlanet + " " + position;
    }
    
    public String toStringWithSign()
    {
        String planetSign = getPlanetSign();
        
        return planetSign + " " + position;
    }

    @Override
    public int compareTo(HouseEntity o)
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
        final HouseEntity other = (HouseEntity) obj;
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
    
    private void initialize()
    {
        // Get Raasi for the cusp/planet
        occupiedHouse = CalcUtils.getZodiacSign(position);
        
        info = cuspOrPlanet + "\n" + info;
    }    
}
