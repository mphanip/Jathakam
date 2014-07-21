package mpp.jathakamu.types;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import static mpp.jathakamu.JathakamLogger.LOGGER;

public class VDNode
    implements Serializable
{
	private transient final static SimpleDateFormat
            DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	private Planet planet;
    private long dasaBeginPeriod;
	private long dasaEndPeriod;
	private SimpleDateFormat dateFormat = DEFAULT_DATE_FORMAT;
    
    private final List<VDNode> children;

	public VDNode(Planet planet, long dasaBeginPeriod, long dasaEndPeriod, SimpleDateFormat dateFormat)
	{
		super();
        this.children = new ArrayList<>();
		this.planet = planet;
        this.dasaBeginPeriod = dasaBeginPeriod;
		this.dasaEndPeriod = dasaEndPeriod;
		this.dateFormat = dateFormat;
	}
	
	public VDNode(Planet planet, long dasaBeginPeriod, long dasaEndPeriod)
	{
		super();
        this.children = new ArrayList<>();
		this.planet = planet;
        this.dasaBeginPeriod = dasaBeginPeriod;
		this.dasaEndPeriod = dasaEndPeriod;
	}
    
    public static VDNode getRootNode()
    {
        return new VDNode(Planet.PLUTO, 0, 0);
    }
	
	public Planet getPlanet()
	{
		return planet;
	}

	public void setPlanet(Planet planet)
	{
		this.planet = planet;
	}

    public long getDasaBeginPeriod()
    {
        return dasaBeginPeriod;
    }

    public void setDasaBeginPeriod(long dasaBeginPeriod)
    {
        this.dasaBeginPeriod = dasaBeginPeriod;
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
        return new ArrayList<VDNode>(children);
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
    
    public List<VDNode> findDBA(long eventInMillis)
    {
        long t1 = System.currentTimeMillis();
        final List<VDNode> dbaList = new ArrayList<>();
        children.stream()
                .filter(node -> eventInMillis > node.dasaBeginPeriod
                        && eventInMillis < node.dasaEndPeriod)
                .forEach(node -> dbaList.add(0, node))
                ;
        
        if (dbaList.size() < 1)
            return dbaList;
        
        dbaList.get(0).children.stream()
                .filter(node -> eventInMillis > node.dasaBeginPeriod
                        && eventInMillis < node.dasaEndPeriod)
                .forEach(node -> dbaList.add(1, node))
                ;
        
        if (dbaList.size() < 2)
            return dbaList;
        
        dbaList.get(1).children.stream()
                .filter(node -> eventInMillis > node.dasaBeginPeriod
                        && eventInMillis < node.dasaEndPeriod)
                .forEach(node -> dbaList.add(2, node))
                ;
        
        if (dbaList.size() < 3)
            return dbaList;
        
        dbaList.get(2).children.stream()
                .filter(node -> eventInMillis > node.dasaBeginPeriod
                        && eventInMillis < node.dasaEndPeriod)
                .forEach(node -> dbaList.add(3, node))
                ;
        
        long t2 = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "Time taken to find DBA {0}", (t2-t1));
        
        return dbaList;
    }

	@Override
	public String toString()
	{
        if (planet.getIndex() == Planet.PLUTO.getIndex())
        {
            return "Vimshottari Dasa";
        }

		String planetName = planet.get2LetterName();
        String sStartDate = dateFormat.format(new Date(dasaBeginPeriod));
		String sEndDate = dateFormat.format(new Date(dasaEndPeriod));
        String rtnString = String.format("%s [%s - %s]", planetName, sStartDate, sEndDate);
		
		return rtnString;
	}
}
