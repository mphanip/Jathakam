package mpp.jathakam.core.vd;

import java.text.SimpleDateFormat;
import java.util.Date;

import mpp.jathakam.core.Planet;

public class VDNode
{
	private final static SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	private Planet planet;
	private long dasaEndPeriod;
	private SimpleDateFormat dateFormat = DEFAULT_DATE_FORMAT;

	public VDNode(Planet planet, long dasaEndPeriod, SimpleDateFormat dateFormat)
	{
		super();
		this.planet = planet;
		this.dasaEndPeriod = dasaEndPeriod;
		this.dateFormat = dateFormat;
	}
	
	public VDNode(Planet planet, long dasaEndPeriod)
	{
		super();
		this.planet = planet;
		this.dasaEndPeriod = dasaEndPeriod;
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

	@Override
	public String toString()
	{
		String planetName = planet.get2LetterName();
		String sDate = dateFormat.format(new Date(dasaEndPeriod));
		
		return planetName + " [" + sDate + "]";
	}
}
