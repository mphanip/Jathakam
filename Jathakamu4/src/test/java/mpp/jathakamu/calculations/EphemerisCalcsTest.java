/* 
 * Yet to decide on the license
 */
package mpp.jathakamu.calculations;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpp.jathakamu.GlobalSettings;
import mpp.jathakamu.JathakamuException;
import mpp.jathakamu.Profile;
import mpp.jathakamu.ProfileSettings;
import mpp.jathakamu.types.Place;
import mpp.jathakamu.types.Planet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author phani
 */
public class EphemerisCalcsTest
{
    private static Profile profile;
    
    public EphemerisCalcsTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(1973, Calendar.APRIL, 4, 8, 59, 8);
        cal.set(Calendar.MILLISECOND, 0);
        Place place = new Place.PlaceBuilder().values("", 78.5D,
                17D + (1D / 3D), GlobalSettings.DEFAULT_TIMEZONE).build();
        double tjd_ut = 0;
        try
        {
            tjd_ut = SupportCalcs.getJulianDay(cal, place);
        }
        catch (JathakamuException ex)
        {
            Logger.getLogger(EphemerisCalcsTest.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        profile = new Profile(tjd_ut, place, ProfileSettings.DEFAULT);
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of getPlanetName method, of class EphemerisCalcs.
     */
    @Test
    public void testGetPlanetName()
    {
        System.out.println("getPlanetName");
        int planet = 0;
        EphemerisCalcs instance = new EphemerisCalcs(profile);
        String expResult = Planet.getPlanet(planet).getName();
        String result = instance.getPlanetName(planet);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPlanetDetails method, of class EphemerisCalcs.
     */
    @Test
    public void testGetPlanetDetails() throws Exception
    {
        System.out.println("getPlanetDetails");
        int planet = 0;
        EphemerisCalcs instance = new EphemerisCalcs(profile);
//        double[] expResult = null;
        double[] result = instance.getPlanetDetails(planet);
//        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getCuspDetails method, of class EphemerisCalcs.
     */
    @Test
    public void testGetCuspDetails() throws Exception
    {
        System.out.println("getCuspDetails");
         EphemerisCalcs instance =  new EphemerisCalcs(profile);
//        double[] expResult = null;
        double[] result = instance.getCuspDetails();
//        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getAyanamsa method, of class EphemerisCalcs.
     */
    @Test
    public void testGetAyanamsa()
    {
        System.out.println("getAyanamsa");
         EphemerisCalcs instance =  new EphemerisCalcs(profile);
        double expResult = 23.393445966058607d;
        double result = instance.getAyanamsa();
        System.out.println("Ayanamsa = " + result);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getLagna method, of class EphemerisCalcs.
     */
    @Test
    public void testGetLagna()
    {
        System.out.println("getLagna");
         EphemerisCalcs instance =  new EphemerisCalcs(profile);
        double expResult = 0.0;
        double result = instance.getLagna();
        System.out.println("Lagna = " + result);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getMC method, of class EphemerisCalcs.
     */
    @Test
    public void testGetMC()
    {
        System.out.println("getMC");
         EphemerisCalcs instance =  new EphemerisCalcs(profile);
        double expResult = 0.0;
        double result = instance.getMC();
        assertEquals(expResult, result, 0.0);
        System.out.println("MC = " + result);
    }

    /**
     * Test of getSidrealTime method, of class EphemerisCalcs.
     */
    @Test
    public void testGetSidrealTime()
    {
        System.out.println("getSidrealTime");
         EphemerisCalcs instance =  new EphemerisCalcs(profile);
        double expResult = 0.0;
        double result = instance.getSidrealTime();
        System.out.println("Sidreal Time = " + result);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getVertex method, of class EphemerisCalcs.
     */
    @Test
    public void testGetVertex()
    {
        System.out.println("getVertex");
         EphemerisCalcs instance =  new EphemerisCalcs(profile);
        double expResult = 0.0;
        double result = instance.getVertex();
        System.out.println("Vertex = " + result);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getEquatorialAsc method, of class EphemerisCalcs.
     */
    @Test
    public void testGetEquatorialAsc()
    {
        System.out.println("getEquatorialAsc");
         EphemerisCalcs instance =  new EphemerisCalcs(profile);
        double expResult = 0.0;
        double result = instance.getEquatorialAsc();
        System.out.println("EquatorialAsc = " + result);
        assertEquals(expResult, result, 0.0);
    }
    
}
