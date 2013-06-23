/*
 * Managed bean to store all the information about the Natal or horary chart.
 * 
 * 1) Planet position
 * 2) Cusp or Bhava position
 */
package mpp.jatakamu.mbeans;

import javax.inject.Named;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;

import mpp.jatakamu.JatakamuLogger;

import static mpp.jatakamu.Constants.PLANET_NAMES_LIST;

/**
 *
 * @author Phani Pramod M
 */
@Named(value = "chartInfo")
@SessionScoped
public class ChartInfo
    implements Serializable
{

    private double[] cusps = new double[12];
    private double[] planets = new double[9];

    /**
     * Creates a new instance of ChartInfo
     */
    public ChartInfo()
    {
        super();
        JatakamuLogger.log("creating ChartInfo instance..." + hashCode());
        initialize();
    }

    private void initialize()
    {
        cusps[0] = 1.0;
        for (int i = 1; i < 12; i++)
        {
            cusps[i] = cusps[i - 1] + 30.0;
        }

        planets[0] = 5.0;
        for (int i = 1; i < 9; i++)
        {
            planets[i] = planets[i - 1] + 35.0;
        }
    }

    public double getFirstCusp()
    {
        return cusps[0];
    }

    public void setFirstCusp(double firstCusp)
    {
        cusps[0] = firstCusp;
    }

    public double getSecondCusp()
    {
        return cusps[1];
    }

    public void setSecondCusp(double secondCusp)
    {
        cusps[1] = secondCusp;
    }

    public double getThirdCusp()
    {
        return cusps[2];
    }

    public void setThirdCusp(double thirdCusp)
    {
        cusps[2] = thirdCusp;
    }

    public double getFourthCusp()
    {
        return cusps[3];
    }

    public void setFourthCusp(double fourthCusp)
    {
        cusps[3] = fourthCusp;
    }

    public double getFifthCusp()
    {
        return cusps[4];
    }

    public void setFifthCusp(double fifthCusp)
    {
        cusps[4] = fifthCusp;
    }

    public double getSixthCusp()
    {
        return cusps[5];
    }

    public void setSixthCusp(double sixthCusp)
    {
        cusps[5] = sixthCusp;
    }

    public double getSeventhCusp()
    {
        return cusps[6];
    }

    public void setSeventhCusp(double seventhCusp)
    {
        cusps[6] = seventhCusp;
    }

    public double getEighthCusp()
    {
        return cusps[7];
    }

    public void setEighthCusp(double eighthCusp)
    {
        cusps[7] = eighthCusp;
    }

    public double getNinthCusp()
    {
        return cusps[8];
    }

    public void setNinthCusp(double ninthCusp)
    {
        cusps[8] = ninthCusp;
    }

    public double getTenthCusp()
    {
        return cusps[9];
    }

    public void setTenthCusp(double tenthCusp)
    {
        cusps[9] = tenthCusp;
    }

    public double getEleventhCusp()
    {
        return cusps[10];
    }

    public void setEleventhCusp(double eleventhCusp)
    {
        cusps[10] = eleventhCusp;
    }

    public double getTwelthCusp()
    {
        return cusps[11];
    }

    public void setTwelthCusp(double twelthCusp)
    {
        cusps[11] = twelthCusp;
    }

    public double getKetu()
    {
        return planets[0];
    }

    public void setKetu(double ketu)
    {
        planets[0] = ketu;
    }

    public double getVenus()
    {
        return planets[1];
    }

    public void setVenus(double venus)
    {
        planets[1] = venus;
    }

    public double getSun()
    {
        return planets[2];
    }

    public void setSun(double sun)
    {
        planets[2] = sun;
    }

    public double getMoon()
    {
        return planets[3];
    }

    public void setMoon(double moon)
    {
        planets[3] = moon;
    }

    public double getMars()
    {
        return planets[4];
    }

    public void setMars(double mars)
    {
        planets[4] = mars;
    }

    public double getRahu()
    {
        return planets[5];
    }

    public void setRahu(double rahu)
    {
        planets[5] = rahu;
    }

    public double getJupiter()
    {
        return planets[6];
    }

    public void setJupiter(double jupiter)
    {
        planets[6] = jupiter;
    }

    public double getSaturn()
    {
        return planets[7];
    }

    public void setSaturn(double saturn)
    {
        planets[7] = saturn;
    }

    public double getMercury()
    {
        return planets[8];
    }

    public void setMercury(double mercury)
    {
        planets[8] = mercury;
    }

    public double getCuspPosition(int i)
    {
        if (i < 0 || i > 12)
        {
            return -1;
        }

        return cusps[i];
    }

    public double getPlanetPosition(int i)
    {
        if (i < 0 || i > 9)
        {
            return -1;
        }
        return planets[i];
    }

    public String getPlanetName(int i)
    {
        if (i < 0 || i > 9)
        {
            return "???";
        }

        return PLANET_NAMES_LIST.get(i);
    }
}
