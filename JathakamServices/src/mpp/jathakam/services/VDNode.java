/*
 * Copyright (c) 2015, phani
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package mpp.jathakam.services;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import mpp.jathakam.services.types.Planet;

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
