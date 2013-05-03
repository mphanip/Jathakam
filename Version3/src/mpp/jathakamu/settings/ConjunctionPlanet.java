package mpp.jathakamu.settings;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import mpp.jathakamu.types.Planet;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "planet")
public class ConjunctionPlanet
{
    @XmlAttribute(name="name")
    protected Planet planet;
    
    @XmlAttribute(name="separating")
    protected double separating;
    
    @XmlAttribute(name="applying")
    protected double applying;
}
