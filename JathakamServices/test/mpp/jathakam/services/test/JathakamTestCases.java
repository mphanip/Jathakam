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
package mpp.jathakam.services.test;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpp.jathakam.services.Defaults;
import mpp.jathakam.services.JathakamuException;
import mpp.jathakam.services.Profile;
import mpp.jathakam.services.ProfileBuilder;
import mpp.jathakam.services.ProfileSettings;
import mpp.jathakam.services.calculations.EphemerisCalcs;
import mpp.jathakam.services.calculations.SupportCalcs;
import mpp.jathakam.services.types.Place;
import mpp.jathakam.services.types.Planet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import swisseph.SweConst;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author phani
 */
public class JathakamTestCases
{
    private static Profile profile;
    
    public JathakamTestCases()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(1973, Calendar.APRIL, 4, 8, 59, 8);
        cal.set(Calendar.MILLISECOND, 0);
        Place place = Defaults.DEFAULT_PLACE;
        double tjd_ut = 0;
        try
        {
            tjd_ut = SupportCalcs.getJulianDay(cal, place);
        }
        catch (JathakamuException ex)
        {
            Logger.getLogger(JathakamTestCases.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        
        ProfileBuilder pb = new ProfileBuilder();
        profile = pb.tjd_ut(tjd_ut).settings(ProfileSettings.DEFAULT).place(place).build();
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
        int planet = SweConst.SE_MOON;
        EphemerisCalcs instance = new EphemerisCalcs(profile);
        double expResult = 359.65008779449636D;
        double[] result = instance.getPlanetDetails(planet);
        System.out.println("Ephemeris flags used = " + profile.getProfileSetting().getEphemerisFlags());
        System.out.println("Planet Details");
        System.out.println("---------------------------------------------");
        for (double val: result) {
            System.out.println(val);
        }
        System.out.println("---------------------------------------------");
        assertEquals(expResult, result[0], 0.0005);
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
