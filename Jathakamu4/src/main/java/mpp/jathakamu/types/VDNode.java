package mpp.jathakamu.types;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class VDNode
    implements Serializable
{
	private transient final static SimpleDateFormat
            DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	private Planet planet;
	private long dasaEndPeriod;
	private SimpleDateFormat dateFormat = DEFAULT_DATE_FORMAT;
    
    private final List<VDNode> children;

	public VDNode(Planet planet, long dasaEndPeriod, SimpleDateFormat dateFormat)
	{
		super();
        this.children = new ArrayList<>();
		this.planet = planet;
		this.dasaEndPeriod = dasaEndPeriod;
		this.dateFormat = dateFormat;
	}
	
	public VDNode(Planet planet, long dasaEndPeriod)
	{
		super();
        this.children = new ArrayList<>();
		this.planet = planet;
		this.dasaEndPeriod = dasaEndPeriod;
	}
    
    public static VDNode getRootNode()
    {
        return new VDNode(Planet.PLUTO, 0);
    }
	
	public Planet getPlanet()
	{
		return planet;
	}

	public void setPlanet(Planet planet)
	{
		this.planet = planet;
	}

	public long getDasaEndPeriod()
	{
		return dasaEndPeriod;
	}

	public void setDasaEndPeriod(long dasaEndPeriod)
	{
		this.dasaEndPeriod = dasaEndPeriod;
	}

	public SimpleDateFormat getDateFormat()
	{
		return dateFormat;
	}

	public void setDateFormat(SimpleDateFormat dateFormat)
	{
		this.dateFormat = dateFormat;
	}
    
    public List<VDNode> getChildren()
    {
        return new ArrayList(children);
    }
    
    public void add(VDNode child)
    {
        children.add(child);
    }
    
    public void add(Collection<VDNode> child)
    {
        children.addAll(child);
    }
    
    public boolean hasChild()
    {
        return children.size() > 0;
    }
    
    public void removeAllChildren()
    {
        children.clear();
    }

	@Override
	public String toString()
	{
        if (planet.getIndex() == planet.PLUTO.getIndex())
        {
            return "Vimshottari Dasa";
        }

		String planetName = planet.get2LetterName();
		String sDate = dateFormat.format(new Date(dasaEndPeriod));
		
		return planetName + " [" + sDate + "]";
	}
}
