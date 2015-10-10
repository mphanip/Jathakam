/*
 * Copyright (c) 2015, phani
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package mpp.jathakam.services;

/**
 * This is an immutable class
 * 
 * @author phani
 */
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import mpp.jathakam.services.types.CUSP_AND_PLANETS;
import mpp.jathakam.services.types.Raasi;
import mpp.jathakam.services.util.Utils;

/**
 * This is an immutable class
 * 
 * @author Phani Pramod M
 */
public class ZodiacItem
    implements Serializable, Comparable<ZodiacItem>, Comparator<ZodiacItem>
{
    private CUSP_AND_PLANETS cuspOrPlanet = CUSP_AND_PLANETS.NONE;
    private double position;
    private final StringBuilder info = new StringBuilder();
    
    // Read only
    private int occupiedHouse;
    
    public ZodiacItem(CUSP_AND_PLANETS value, double position)
    {
        this(value, position, null);
    }
    
    public ZodiacItem(CUSP_AND_PLANETS value, double position, String information)
    {
        this.cuspOrPlanet = value;
        this.position = position;
        
        if (Utils.isNotEmpty(info.toString()))
        {
            info.delete(0, info.length());
            this.info.append(information);
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
    
//    public String getPositionInDegree()
//    {
//        return ViewUtils.toStringDegreeForRaasi(position);
//    }
    
    public boolean isPlanet()
    {
        return cuspOrPlanet.isPlanet();
    }
    
    public boolean isCusp() {
        return cuspOrPlanet.isCusp();
    }

    public int getOccupiedHouse()
    {
        return occupiedHouse;
    }
    
    public String getInfo()
    {
        return info.toString();
    }

//    public String getPlanetSign()
//    {
//        int ord = cuspOrPlanet.ordinal();
//        String rtnStr;
//        if (cuspOrPlanet.isPlanet())
//        {
//            rtnStr = Constants.PLANET_SIGN[cuspOrPlanet.ordinal()-12];
//        }
//        else {
//            rtnStr = cuspOrPlanet.toString();
//        }
//
//        return rtnStr;
//    }
//    
//    public String getPlanetName()
//    {
//        int ord = cuspOrPlanet.ordinal();
//        String rtnStr;
//        if (ord > 11)
//        {
//            rtnStr = Constants.PLANET_2_LETTER_NAMES[cuspOrPlanet.ordinal()-12];
//        }
//        else {
//            rtnStr = cuspOrPlanet.toString();
//        }
//
//        return rtnStr;
//    }
//    public String toStringWithSign()
//    {
//        String planetSign = getPlanetSign();
//        
//        return planetSign + " " + position;
//    }
    
    @Override
    public String toString()
    {
        return cuspOrPlanet + " " + position;
    }

    @Override
    public int compareTo(ZodiacItem o)
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
        final ZodiacItem other = (ZodiacItem) obj;
        if (this.cuspOrPlanet != other.cuspOrPlanet)
        {
            return false;
        }
        return Double.doubleToLongBits(this.position) == Double.doubleToLongBits(other.position);
    }
    
    @Override
    public int compare(ZodiacItem o1, ZodiacItem o2)
    {
        return o1.compareTo(o2);
    }

    private void initialize()
    {
        // Get Raasi for the cusp/planet
        // subtracting -1 because the first Raasi in Enum is None.
        occupiedHouse = Raasi.getRaasi(position).ordinal()-1;
        
        info.append(cuspOrPlanet).append("\n").append(info);
    }

}
