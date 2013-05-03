package mpp.jathakamu.settings;

import static mpp.jathakamu.JathakamLogger.LOGGER;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Level;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import mpp.jathakamu.Constants.ASPECT_EFFECT;
import mpp.jathakamu.types.AspectInfo;
import mpp.jathakamu.utils.Utils;

public class AspectOrbsSettings
{
    private static Orbs orbs;
    private final static NavigableMap<Double, Aspect> aspectMap = new ConcurrentSkipListMap<Double, Aspect>();
    private static final String ASPECT_ORB_FILE_NAME
            = Settings.CONFIG_DIR_WITH_SEPARATOR + "aspect_orbs.xml";
    private static final File ASPECT_ORB_FILE = new File(ASPECT_ORB_FILE_NAME);
    private static double[][] aspectOrbsTable;
    private static Map<String, Integer> aspectIndexMap = new HashMap<String, Integer>();
    private static boolean initialized = false;
    
    static
    {
        initialize();
    }
    
    private static void initialize()
    {
        try
        {
            JAXBContext jc = JAXBContext.newInstance(Orbs.class);
            Unmarshaller um =  jc.createUnmarshaller();
            orbs = (Orbs) um.unmarshal(ASPECT_ORB_FILE);
            analyze();
            initialized = true;
            LOGGER.log(Level.INFO, "Reading aspect orbs from xml completed");
        }
        catch (JAXBException e)
        {
            LOGGER.log(Level.WARNING, "Exception occured while instantiating" +
            		" JAXB context. Following is the stack trace", e);
        }
    }
    
    /**
     * Should be called only after initializing aspectList;
     */
    private static void analyze()
    {
        if (orbs == null)
        {
            return;
        }

        int size = orbs.aspectList.size();
        
        aspectOrbsTable = new double[size][12];
        
        for (int index = 0; index < size; index++)
        {
            Aspect aspect = orbs.aspectList.get(index);

            /*
             * Initialize with default orbs, since all planet tags may not be
             * present under aspect tag.
             */
            for (int i = 0; i < 12; i++)
            {
                aspectOrbsTable[index][i] = aspect.getDefaultOrbs();
            }
            
            aspectMap.put(aspect.getAngle(), aspect);
            aspectIndexMap.put(aspect.getName(), index);
            List<AspectPlanet> planetList = aspect.getPlanet();
            
            if (planetList == null)
            {
                continue;
            }
            
            for (AspectPlanet p : planetList)
            {
                int planetIndex = mpp.jathakamu.types.Planet.valueOf(p.getName().toUpperCase()).getIndex();
                aspectOrbsTable[index][planetIndex] = p.getOrbs();
            }
        }
    }
    
    public static boolean isInitialized()
    {
        return initialized;
    }
    
    public static void update(double angle, int planetIndex, double orbs)
    {
        if (angle > 360 || angle < 0 || planetIndex > 12 || planetIndex < 0
                || orbs < 0)
            return;
        
        Aspect aspect = aspectMap.get(angle);
        List<AspectPlanet> planetList = aspect.getPlanet();
        AspectPlanet pl = planetList.get(planetIndex);
        pl.setOrbs(orbs);
    }
    
    public static boolean save()
    {
        try
        {
            JAXBContext jc = JAXBContext.newInstance(Orbs.class);
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(orbs, ASPECT_ORB_FILE);
            return true;
        }
        catch (JAXBException e)
        {
            LOGGER.log(Level.WARNING, "Exception occured while saving" +
            		" aspect_orbs.xml. Following is the exception stack trace",
            		e);
            return false;
        }
    }

    public static void setAspectAttr(AspectInfo aspectInfo)
    {
        int fromPlanetIndex = aspectInfo.getFrom();
        double longitude = aspectInfo.getLongitude();
        
        for (Entry<Double, Aspect> entry : aspectMap.entrySet())
        {
            Aspect aspect = entry.getValue();
            int aspectIndex = aspectIndexMap.get(aspect.getName());
            double dOrbs = aspectOrbsTable[aspectIndex][fromPlanetIndex];
            double aspectAngle = aspect.getAngle();
            double min = aspectAngle - dOrbs;
            double max = aspectAngle  + dOrbs;
            
            boolean flag = Utils.valueIn(min, max, longitude);
            if (flag)
            {
                ASPECT_EFFECT effect = aspect.getEffect();
                String name = aspect.getName();
                aspectInfo.setAspect(effect);
                aspectInfo.setName(name);
                return;
            }
            
            if (aspectAngle > longitude)
            {
                break;
            }
        }
        
        aspectInfo.setAspect(ASPECT_EFFECT.NONE);
    }
}
