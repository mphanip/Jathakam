package mpp.jathakamu.settings;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import mpp.jathakamu.Constants.ASPECT_EFFECT;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "aspect", propOrder={"planet"})
public class Aspect
{
    @XmlElement(required = false)
    private List<AspectPlanet> planet;

    @XmlAttribute(name = "name")
    private String name;
    
    @XmlAttribute(name = "angle")
    private double angle;
    
    @XmlAttribute(name = "defaultOrbs")
    private double defaultOrbs;
    
    @XmlAttribute(name = "effect")
    private ASPECT_EFFECT effect;
    
    @XmlAttribute(name = "major", required = false)
    private boolean major;

    public Aspect()
    {
        super();
    }

    public Aspect(String name, double angle, double defaultOrbs,
            ASPECT_EFFECT effect)
    {
        super();
        this.name = name;
        this.angle = angle;
        this.defaultOrbs = defaultOrbs;
        this.effect = effect;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getAngle()
    {
        return angle;
    }

    public void setAngle(double angle)
    {
        this.angle = angle;
    }

    public double getDefaultOrbs()
    {
        return defaultOrbs;
    }

    public void setDefaultOrbs(double defaultOrbs)
    {
        this.defaultOrbs = defaultOrbs;
    }

    public ASPECT_EFFECT getEffect()
    {
        return effect;
    }

    public void setEffect(ASPECT_EFFECT effect)
    {
        this.effect = effect;
    }

    public List<AspectPlanet> getPlanet()
    {
        return planet;
    }

    public void setPlanet(List<AspectPlanet> planetList)
    {
        this.planet = planetList;
    }
    
    public void add(AspectPlanet planet)
    {
        this.planet.add(planet);
    }
}
