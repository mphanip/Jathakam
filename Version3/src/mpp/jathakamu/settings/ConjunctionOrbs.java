package mpp.jathakamu.settings;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="orbs", namespace="mpp")
@XmlType(name = "orbs", propOrder = { "planetList" })
public class ConjunctionOrbs
{
    @XmlElement(name = "planet", required = true)
    protected List<ConjunctionPlanet> planetList;
}
