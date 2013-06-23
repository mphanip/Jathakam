/*
 *
 *
 */
package mpp.jathakam.core;

import java.io.Serializable;

import mpp.jathakam.core.HouseInfo;
import mpp.jathakam.core.Raasi;
import mpp.jathakam.utils.JatakamUtilities;

/**
 *
 * @author phani
 */
public class HouseInfo
  implements Serializable, Cloneable
{
  private static final long serialVersionUID = -7229121350282242277L;
  private String name = "";
  private double position = 0.0D;

  public HouseInfo(String name, double pos)
  {
    this.name = name;
    this.position = pos;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public double getPosition()
  {
    return this.position;
  }

  public void setPosition(double pos)
  {
    this.position = pos;
  }


    public String getRaasiName()
    {
        int raasi = getRaasi();
        return Raasi.getRaasi(raasi).name();
    }

    public int getRaasi()
    {
        return Double.valueOf(Math.ceil(position / 30D)).intValue();
    }

  public String toString()
  {
      String raasi = getRaasiName();
    return "[" + name + " = " + raasi + " : " + JatakamUtilities.toStringDegree(this.position) + "]";
  }

  protected Object clone()
    throws CloneNotSupportedException
  {
    super.clone();
    HouseInfo hi = new HouseInfo(this.name, this.position);
    return hi;
  }
}
