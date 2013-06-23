/*
 *
 *
 */
package mpp.jathakam.core.vd;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import mpp.jathakam.core.Planet;
import mpp.jathakam.core.PlanetDasaIterator;
import mpp.jathakam.core.Range;
import mpp.jathakam.utils.CircularList;
import mpp.jathakam.utils.JatakamUtilities;

/**
 *
 * @author phani
 */
public class VimshottariDasa
{
    private static final Logger LOGGER = Logger.getLogger(VimshottariDasa.class.getName());

    private final static NavigableMap<Range, List<DasaNode>> vdasaTable
        = new ConcurrentSkipListMap<Range, List<DasaNode>>();

    static
    {
        String logFolder = System.getProperty("java.io.tmpdir", ".");
        if (!logFolder.endsWith(File.separator))
        {
        	logFolder = logFolder + File.separator;
        }
        String logFile = logFolder + "VimshottariDasa.log";
        System.out.println("Creating log under " + logFile);
        JatakamUtilities.setLoggerSettings(LOGGER, logFile, Level.FINEST);

        /*
         * Note that most of the below logic is based on minutes.
         * For Example 800'=13Deg 20'
         */
        double dasaduration = 800D;
        double mahaDasaStart;
        double mahaDasaEnd = 0D;

        for (int i = 0; i < 3; i++)
        {
            for (Planet mahaDasaPlanet : Planet.values())
            {
                mahaDasaStart = mahaDasaEnd;
                mahaDasaEnd += dasaduration;
                Range mahaDasaRange = new Range(mahaDasaStart/60D, mahaDasaEnd/60D);
                double mahaDasaTimeFraction = mahaDasaPlanet.getDasaDuration(); 
                DasaNode mahaDasaNode = new DasaNode(mahaDasaPlanet, mahaDasaRange, mahaDasaTimeFraction);
                PlanetDasaIterator bukthi = new PlanetDasaIterator(mahaDasaPlanet);
                double buktiStart;
                double buktiEnd = mahaDasaStart;

                for (Planet bukthiPlanet : bukthi)
                {
                    buktiStart = buktiEnd;
                    buktiEnd += dasaduration * (bukthiPlanet.getDasaDuration() / 120D);
                    Range buktiRange = new Range(buktiStart/60D, buktiEnd/60D);
                    double buktiTimeFraction = mahaDasaTimeFraction * bukthiPlanet.getDasaDuration() / 120D;
                    DasaNode buktiDasaNode = new DasaNode(bukthiPlanet, buktiRange, buktiTimeFraction);

                    double pratyantaraStart;
                    double pratyantaraEnd = buktiStart;
                    double buktiDuration = buktiEnd - buktiStart;
                    PlanetDasaIterator pratyantara = new PlanetDasaIterator(bukthiPlanet);

                    for (Planet pratyantaraPlanet : pratyantara)
                    {
                        pratyantaraStart = pratyantaraEnd;
                        pratyantaraEnd += buktiDuration * (pratyantaraPlanet.getDasaDuration() / 120D);
                        Range pratyantaraRange = new Range(pratyantaraStart/60D, pratyantaraEnd/60D);
                        double pratyantaraTimeFraction = buktiTimeFraction * pratyantaraPlanet.getDasaDuration() / 120D;
                        DasaNode pratyantaraDasaNode = new DasaNode(pratyantaraPlanet, pratyantaraRange, pratyantaraTimeFraction);

                        double sookshmaStart;
                        double sookshmaEnd = pratyantaraStart;
                        double pratyantaraDuration = pratyantaraEnd - pratyantaraStart;
                        PlanetDasaIterator sookshma = new PlanetDasaIterator(pratyantaraPlanet);

                        for (Planet sookshmaPlanet : sookshma)
                        {
                            sookshmaStart = sookshmaEnd;
                            sookshmaEnd += pratyantaraDuration * (sookshmaPlanet.getDasaDuration() / 120D);
                            Range sookshmaRange = new Range(sookshmaStart/60D, sookshmaEnd/60D);
                            double sookshmaTimeFraction = pratyantaraTimeFraction * sookshmaPlanet.getDasaDuration() / 120D;
                            DasaNode sookshmaDasaNode = new DasaNode(sookshmaPlanet,sookshmaRange, sookshmaTimeFraction);
                            
                            double pranaStart;
                            double pranaEnd = sookshmaStart;
                            double sookshmaDuration = sookshmaEnd - sookshmaStart;
                            PlanetDasaIterator prana = new PlanetDasaIterator(sookshmaPlanet);
                            
                            for (Planet pranaPlanet : prana)
                            {
                            	pranaStart = pranaEnd;
                            	pranaEnd += sookshmaDuration * (pranaPlanet.getDasaDuration() / 120D);
                            	Range pranaRange = new Range(pranaStart/60D, pranaEnd/60D);
                            	double pranaTimeFraction = sookshmaTimeFraction * pranaPlanet.getDasaDuration() / 120D;
                            	DasaNode pranaNode = new DasaNode(pranaPlanet, pranaRange, pranaTimeFraction);
                            	
	                            List<DasaNode> planetList = new ArrayList<DasaNode>();
	                            planetList.add(mahaDasaNode);
	                            planetList.add(buktiDasaNode);
	                            planetList.add(pratyantaraDasaNode);
	                            planetList.add(sookshmaDasaNode);
	                            planetList.add(pranaNode);
	
	                            vdasaTable.put(pranaRange, planetList);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void dumpDasaTable()
    {
        /* Just for debugging purpose printing the table */
        for (Map.Entry<Range,List<DasaNode>> entry : vdasaTable.entrySet())
        {
            Range key = entry.getKey();
            List<DasaNode> value = entry.getValue();

//            double diff = key.end() - key.start();
//            String sDiff = JatakamUtilities.toStringDegree(diff);

            LOGGER.log(Level.FINEST, "{0}={1}", new Object[]{key, value});
        }
    }
    
    private double planetLongitude;
    
    public VimshottariDasa(double longitude)
    {
    	planetLongitude = longitude;
    }

    public Map.Entry<Range, List<DasaNode>> getDasaEntry()
    {
        Map.Entry<Range, List<DasaNode>> entry = vdasaTable.ceilingEntry(new Range(0D, planetLongitude));

        return entry;
    }
    
    public TreeNode getVDTree1(long dateTime)
    {
		System.out.println("Given Longitude = "
				+ JatakamUtilities.toStringDegree(planetLongitude));
		Map.Entry<Range, List<DasaNode>> entry = getDasaEntry();
		List<DasaNode> nodeList = entry.getValue();
		System.out.println(entry.getKey() + "->" + nodeList);
		
		long dob = dateTime;
		
		Planet []planets = new Planet[5];
		long []endDate = new long[5];
		String indent = "";
		int i = 0;
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Vimshottari Dasa", true);
		 
		for (DasaNode dasa : nodeList)
		{
			planets[i] = dasa.getPlanet();
			double duration = dasa.getDurationInYears(planetLongitude);
			
			endDate[i] = JatakamUtilities.addDate(dob, duration);
			String sEndDate = JatakamUtilities.getDateString(endDate[i]);
			
			System.out.println(indent + dasa.getPlanet()
					+ "[" + sEndDate + "]"
					+ " {" + JatakamUtilities.toStringDateTime(duration) + "}");

			indent += "\t";
			i++;
		}
		
		/*
		 * For each of the dasa we are iterating thru the all the planet till
		 * planet with the given longitude is reached.
		 * 
		 * For Example: If the starting dasa is Mercury-Saturn-Rahu-Sun-Moon
		 * then we create iterator from Mercury,Mercury,Saturn,Rahu,Sun and 
		 * perform next() till we reach Mercury, Saturn, Rahu, sun, Moon
		 * Respectively
		 */
		PlanetDasaIterator mahaDasaIter = new PlanetDasaIterator(planets[0]);
		Planet endPlanet;
		Planet p;
		
		PlanetDasaIterator buktiIter = new PlanetDasaIterator(planets[0]);
		if (!planets[0].equals(planets[1]))
		{
			endPlanet =  Planet.getPlanetByOrdinal(planets[1].ordinal()-1);
			p = buktiIter.next();
			while (buktiIter.hasNext() && !p.equals(endPlanet))
			{
				p = buktiIter.next();
			}
		}
		
		PlanetDasaIterator antaraIter = new PlanetDasaIterator(planets[1]);
		if (!planets[2].equals(planets[1]))
		{
			endPlanet =  Planet.getPlanetByOrdinal(planets[2].ordinal()-1);
			p = antaraIter.next();
			while (antaraIter.hasNext() && !p.equals(endPlanet))
			{
				p = antaraIter.next();
			}
		}
		
		PlanetDasaIterator sookshmaIter = new PlanetDasaIterator(planets[2]);
		if (!planets[3].equals(planets[2]))
		{
			endPlanet =  Planet.getPlanetByOrdinal(planets[3].ordinal()-1);
			p = sookshmaIter.next();
			while (sookshmaIter.hasNext() && !p.equals(endPlanet))
			{
				p = sookshmaIter.next();
			}
		}
		
		PlanetDasaIterator pranaIter = new PlanetDasaIterator(planets[3]);
		if (!planets[4].equals(planets[3]))
		{
			endPlanet =  Planet.getPlanetByOrdinal(planets[4].ordinal()-1);
			p = pranaIter.next();
			while (pranaIter.hasNext() && !p.equals(endPlanet))
			{
				p = pranaIter.next();
			}
		}
		
		while (mahaDasaIter.hasNext())
		{
			Planet mp = mahaDasaIter.next();
			
			double mahaDasaDuration = mp.getDasaDuration();
			DefaultMutableTreeNode mNode = addNode1(mp, root, true,
					mahaDasaDuration, endDate, 0);
			
			if (buktiIter == null)
			{
				buktiIter = new PlanetDasaIterator(mp);
			}
			while (buktiIter.hasNext())
			{
				Planet bp = buktiIter.next();
				double buktiDasaDuration = mahaDasaDuration * bp.getDasaDuration()/120D;
				DefaultMutableTreeNode bNode = addNode1(bp, mNode, true, buktiDasaDuration, endDate, 1);
				
				if (antaraIter == null)
				{
					antaraIter = new PlanetDasaIterator(bp);
				}
				while (antaraIter.hasNext())
				{
					Planet ap = antaraIter.next();
					double antaraDasaDuration = buktiDasaDuration * ap.getDasaDuration()/120D;
					DefaultMutableTreeNode aNode = addNode1(ap, bNode, true, antaraDasaDuration, endDate, 2);
					
					if (sookshmaIter == null)
					{
						sookshmaIter = new PlanetDasaIterator(ap);
					}
					while (sookshmaIter.hasNext())
					{
						Planet sp = sookshmaIter.next();
						double sookshmaDasaDuration = antaraDasaDuration * sp.getDasaDuration()/120D;
						DefaultMutableTreeNode sNode = addNode1(sp, aNode, true, sookshmaDasaDuration, endDate, 3);
						
						if (pranaIter == null)
						{
							pranaIter = new PlanetDasaIterator(sp);
						}
						while (pranaIter.hasNext())
						{
							Planet pp = pranaIter.next();
							double pranaDasaDuration = sookshmaDasaDuration * pp.getDasaDuration()/120D;
							addNode(pp, sNode, false, pranaDasaDuration, endDate, 4);
						}
						pranaIter = null;
					}
					sookshmaIter = null;
				}
				antaraIter = null;
			}
			buktiIter = null;
		}
		
		return root;
    }
    
    public DefaultMutableTreeNode getVDTree(long dob)
    {
		System.out.println("Given Longitude = "
				+ JatakamUtilities.toStringDegree(planetLongitude));
		Map.Entry<Range, List<DasaNode>> entry = getDasaEntry();
		DasaNode[] startingDasaPlanets = new DasaNode[5];
		double[] remainDasaFraction = new double[5];
		
		entry.getValue().toArray(startingDasaPlanets);
		
		int i = 0;
		for (DasaNode node: startingDasaPlanets)
		{
			System.out.println(node);
			remainDasaFraction[i++] = node.getRemainingDurationForLongitude(planetLongitude);
		}
		
		long[] dasaEndDate = {dob, dob, dob, dob, dob}; 
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Vimshottari Dasa", true);
		Planet startingMahaDasaPlanet = startingDasaPlanets[0].getPlanet();
		Planet startingBhuktiPlanet = startingDasaPlanets[1].getPlanet();
		Planet startingPratyantaraPlanet = startingDasaPlanets[2].getPlanet();
		Planet startingSookshmaPlanet = startingDasaPlanets[3].getPlanet();
		Planet startingPranaPlanet = startingDasaPlanets[4].getPlanet();
		PlanetDasaIterator mdIter = new PlanetDasaIterator(startingMahaDasaPlanet);
		 
		while (mdIter.hasNext())
		{
			Planet mahaDasaPlanet = mdIter.next();
			double mdDuration = mahaDasaPlanet.getDasaDuration() * remainDasaFraction[0]; // Required for 1st iteration only
			DefaultMutableTreeNode mahaDasaNode = addNode(mahaDasaPlanet, root, true, mdDuration, dasaEndDate, 0);
			remainDasaFraction[0] = 1;
			mdDuration = mahaDasaPlanet.getDasaDuration(); // Required for use by bukti loop below
			
			List<Planet> bukthiPlanets = getDasaPlanetList(mahaDasaPlanet, startingBhuktiPlanet);
			for (Planet bp : bukthiPlanets)
			{
				double bdDuration = mdDuration * bp.getDasaDuration()/120D * remainDasaFraction[1];
				remainDasaFraction[1] = 1;
				DefaultMutableTreeNode bukthiDasaNode = addNode(bp, mahaDasaNode, true, bdDuration, dasaEndDate, 1);
				bdDuration = mdDuration * bp.getDasaDuration()/120D;
				
				List<Planet> pratyantaraPlanets = getDasaPlanetList(bp, startingPratyantaraPlanet);
				for (Planet pap : pratyantaraPlanets)
				{
					double paDuration = bdDuration * pap.getDasaDuration()/120D * remainDasaFraction[2];
					remainDasaFraction[2] = 1;
					DefaultMutableTreeNode pratyantaraDasaNode = addNode(pap, bukthiDasaNode, true, paDuration, dasaEndDate, 2);
					paDuration = bdDuration * pap.getDasaDuration()/120D;
					
					List<Planet> sookshmaPlanets = getDasaPlanetList(pap, startingSookshmaPlanet);
					for (Planet sp : sookshmaPlanets)
					{
						double sdDuration = paDuration * sp.getDasaDuration()/120D * remainDasaFraction[3];
						remainDasaFraction[3] = 1;
						DefaultMutableTreeNode sookshmaDasaNode = addNode(sp, pratyantaraDasaNode, true, sdDuration, dasaEndDate, 3);
						sdDuration = paDuration * sp.getDasaDuration()/120D;
						
						List<Planet> pranaPlanets = getDasaPlanetList(sp, startingPranaPlanet);
						for (Planet pp : pranaPlanets)
						{
							double pdDuration = sdDuration * pp.getDasaDuration()/120D * remainDasaFraction[4];
							remainDasaFraction[4] = 1;
							addNode(pp, sookshmaDasaNode, false, pdDuration, dasaEndDate, 4);
						}
						startingPranaPlanet = null;
					}
					startingSookshmaPlanet = null;
				}
				startingPratyantaraPlanet = null;
			}
			startingBhuktiPlanet = null;
		}
		
		return root;
    }
    
    private List<Planet> getDasaPlanetList(Planet parentDasa, Planet start)
    {
    	CircularList<Planet>  planetList = new CircularList<Planet>(Planet.getPlanetList(), true);
    	List<Planet> rtnList = new ArrayList<Planet>();
    	if (start == null)
    	{
    		Iterator<Planet> iter = planetList.iterator(parentDasa);
    		while (iter.hasNext())
    		{
    			Planet p = iter.next();
    			rtnList.add(p);
    		}
    	}
    	else
    	{
    		Iterator<Planet> iter = planetList.iterator(start);
    		while (iter.hasNext())
    		{
    			Planet p = iter.next();
    			if (p.ordinal() == parentDasa.ordinal())
    			{
    				break;
    			}
    			rtnList.add(p);
    		}
    	}
    	return rtnList;
    }
    
	private DefaultMutableTreeNode addNode(Planet dasaPlanet,
			DefaultMutableTreeNode parent, boolean allowChildren,
			double dasaDuration, long[] dasaEndDate, int endDateIndex)
	{
		VDNode vdNode = new VDNode(dasaPlanet, 0L);
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(vdNode,
				allowChildren);
		parent.add(newNode);
		
		dasaEndDate[endDateIndex] = JatakamUtilities.addDate(dasaEndDate[endDateIndex], dasaDuration);
		
		vdNode.setDasaEndPeriod(dasaEndDate[endDateIndex]);

		return newNode;
	}

    private long getDasaEndDate1(long defaultDate, double dasaDuration, DefaultMutableTreeNode prevNode)
    {
    	long endDate = defaultDate;
		
		if (prevNode != null)
		{
			VDNode prevmvNode = (VDNode) prevNode.getUserObject();
			long prevEndDate = prevmvNode.getDasaEndPeriod();
			endDate = JatakamUtilities.addDate(prevEndDate, dasaDuration);
		}
		
		return endDate;
    }
    
	private DefaultMutableTreeNode addNode1(Planet dasaPlanet,
			DefaultMutableTreeNode parent, boolean allowChildren,
			double dasaDuration, long[] dasaEndDate, int endDateIndex)
	{
		VDNode vdNode = new VDNode(dasaPlanet, 0L);
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(vdNode,
				allowChildren);
		boolean hasChildren = (parent.getChildCount() > 0);
		parent.add(newNode);
		
		if (!hasChildren)
		{
			DefaultMutableTreeNode PrevSibling = getPrevSibling(parent, newNode);
			if (PrevSibling != null)
			{
				VDNode lastChildVNode = (VDNode) PrevSibling.getUserObject();
				dasaEndDate[endDateIndex] = lastChildVNode.getDasaEndPeriod();
				dasaEndDate[endDateIndex] = JatakamUtilities.addDate(dasaEndDate[endDateIndex], dasaDuration);
			}
		}

		DefaultMutableTreeNode prevSibling = newNode.getPreviousSibling();
		long endDate = getDasaEndDate1(dasaEndDate[endDateIndex], dasaDuration, prevSibling);

		dasaEndDate[endDateIndex] = endDate;
		vdNode.setDasaEndPeriod(endDate);

		return newNode;
	}
	
	private DefaultMutableTreeNode getPrevSibling(DefaultMutableTreeNode parent, DefaultMutableTreeNode childNode)
	{
		if (parent.getLevel() == 0)
			return null;

		DefaultMutableTreeNode prevSibling = parent.getPreviousSibling();
		
		if (prevSibling != null)
		{
			DefaultMutableTreeNode lastChildNode = (DefaultMutableTreeNode) prevSibling.getLastChild();
			return lastChildNode;
		}
		
		while (prevSibling == null)
		{
			parent = (DefaultMutableTreeNode) parent.getParent();
			
			if (parent == null)
			{
				break;
			}
			if (parent.getLevel() == 0)
				return null;
			
			prevSibling = parent.getPreviousSibling();
		}
		
		for (int i = prevSibling.getLevel(); i < childNode.getLevel(); i++)
		{
			prevSibling = (DefaultMutableTreeNode) prevSibling.getLastChild();
		}
		
		return prevSibling;
	}
}
