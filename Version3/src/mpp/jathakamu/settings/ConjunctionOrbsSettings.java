package mpp.jathakamu.settings;

import static mpp.jathakamu.JathakamLogger.LOGGER;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import mpp.jathakamu.types.Planet;

public class ConjunctionOrbsSettings
{
    private static ConjunctionOrbs orbs;
    
    private static final String CONJUNCTION_ORB_FILE_NAME
            = Settings.CONFIG_DIR_WITH_SEPARATOR + "conjunction_orbs.xml";
    
    private static final File CONJUNCTION_ORB_FILE 
            = new File(CONJUNCTION_ORB_FILE_NAME);
    
    private static Map<Planet, ConjunctionPlanet> planetMap
            = new HashMap<Planet, ConjunctionPlanet>();

    static
    {
        initialize();
    }

    private static void initialize()
    {
        try
        {
            JAXBContext jc = JAXBContext.newInstance(ConjunctionOrbs.class);
            Unmarshaller um = jc.createUnmarshaller();
            orbs = (ConjunctionOrbs) um.unmarshal(CONJUNCTION_ORB_FILE);

            for (ConjunctionPlanet cp : orbs.planetList)
            {
                planetMap.put(cp.planet, cp);
            }

            LOGGER.log(Level.INFO, "Reading aspect orbs from xml completed");
        }
        catch (JAXBException e)
        {
            LOGGER.log(Level.WARNING, "Exception occured while instantiating"
                    + " JAXB context. Following is the stack trace", e);
        }
    }

    public static double[] getOrb(mpp.jathakamu.types.Planet planet)
    {
        for (ConjunctionPlanet cp : orbs.planetList )
        {
            if (cp.planet.getIndex() == planet.getIndex())
            {
                return new double[] {cp.applying, cp.separating};
            }
        }

        return new double[0];
    }
    
    public static void update(Planet planet, double separating, double applying)
    {
        for (ConjunctionPlanet cp : orbs.planetList )
        {
            if (cp.planet.getIndex() == planet.getIndex())
            {
                cp.applying = applying;
                cp.separating = separating;
                return;
            }
        }
    }
    
    public static boolean save()
    {
        try
        {
            JAXBContext jc = JAXBContext.newInstance(ConjunctionOrbs.class);
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(orbs, CONJUNCTION_ORB_FILE);
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

//    public static void main(String[] args)
//    {
//        update(Planet.SUN, 12, 12);
//        save();
//        System.out.println(getOrb(mpp.jathakamu.types.Planet.SUN)[0]);
//    }
}
