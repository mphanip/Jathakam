package mpp.jathakamu.settings;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(namespace="mpp")
@XmlType(name = "orbs", propOrder = { "aspectList" })
public class Orbs
{
    @XmlElement(name = "aspect", required = true)
    protected List<Aspect> aspectList;

//    public List<Aspect> getAspects()
//    {
//        return aspectList;
//    }
//
//    public void setAspectList(List<Aspect> aspectList)
//    {
//        this.aspectList = aspectList;
//    }
//
//    public void add(Aspect aspect)
//    {
//        if (aspect == null)
//        {
//            this.aspectList = new ArrayList<Aspect>();
//        }
//        this.aspectList.add(aspect);
//    }
}
