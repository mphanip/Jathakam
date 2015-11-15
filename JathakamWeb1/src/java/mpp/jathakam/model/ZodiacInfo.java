/*
 * This software is provided AS IS without any warranty for any kind of use. Author is not liable for any loss for using
 * this software.
 */
package mpp.jathakam.model;

/**
 *
 * @author phani
 */
public class ZodiacInfo
{
    private int objectType; // Planets or cusp
    private double position;

    public ZodiacInfo()
    {
    }

    public ZodiacInfo(int objectType, double position)
    {
        this.objectType = objectType;
        this.position = position;
    }

    public int getObjectType()
    {
        return objectType;
    }

    public void setObjectType(int objectType)
    {
        this.objectType = objectType;
    }

    public double getPosition()
    {
        return position;
    }

    public void setPosition(double position)
    {
        this.position = position;
    }
}
