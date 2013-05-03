package mpp.jathakamu.settings;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "planet")
public class AspectPlanet
{
    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "orbs")
    private double orbs;

    public AspectPlanet()
    {
        super();
    }

    public AspectPlanet(String name, double orbs)
    {
        super();
        this.name = name;
        this.orbs = orbs;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getOrbs()
    {
        return orbs;
    }

    public void setOrbs(double orbs)
    {
        this.orbs = orbs;
    }
}
