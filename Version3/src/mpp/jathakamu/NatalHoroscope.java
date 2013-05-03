package mpp.jathakamu;

import java.io.Serializable;
import static mpp.jathakamu.JathakamLogger.LOGGER;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import mpp.jathakamu.Constants.ASPECT_EFFECT;
import mpp.jathakamu.Constants.SIGNIFICATOR_LEVELS;
import mpp.jathakamu.settings.Settings;
import mpp.jathakamu.types.AspectInfo;
import mpp.jathakamu.types.AspectsTable;
import mpp.jathakamu.types.CuspHoroDetails;
import mpp.jathakamu.types.HoroChartData;
import mpp.jathakamu.types.LordNode;
import mpp.jathakamu.types.Planet;
import mpp.jathakamu.types.PlanetHoroDetails;
import mpp.jathakamu.types.Raasi;
import mpp.jathakamu.types.Range;
import mpp.jathakamu.utils.CalcUtils;
import mpp.jathakamu.view.ViewUtils;
import swisseph.DblObj;
import swisseph.SweConst;
import swisseph.SweDate;
import swisseph.SwissEph;

class NatalHoroscope
    implements Horoscope, Serializable
{
    private static final long serialVersionUID = 1L;
    
    protected StringBuffer serr = new StringBuffer();
    protected double ayanamsa;
    protected double sidrealTime;
    protected Map<Raasi, List<Integer>> raasiToCuspMap = new ConcurrentHashMap<Raasi, List<Integer>>(12);
    protected Map<Raasi, List<Planet>> raasiToPlanetMap = new ConcurrentHashMap<Raasi, List<Planet>>(12);
    protected StringBuilder sbAnalysisNotes = new StringBuilder();
    
    public final String getErrString()
    {
        return serr.toString();
    }
    
    public NatalHoroscope()
    {
        LOGGER.log(Level.INFO, "Initializing Natal Horoscope");
        CalcUtils.initializeLordsTable();
        Settings.setKPOldAynamsa();
    }

    @Override
    public List<PlanetHoroDetails> getPlanetHoroDetails(HoroChartData data)
    {
        raasiToPlanetMap.clear();

        List<PlanetHoroDetails> planetDetails = new ArrayList<PlanetHoroDetails>();
        double tjd_ut = data.getJulianDay();
        SwissEph se = getSwissEphemeris(tjd_ut);

        for (int i = SweConst.SE_SUN; i <= SweConst.SE_PLUTO; i++)
        {
            serr.delete(0, serr.length());
            double[] xx = new double[6];
            int rtnFlag = se.swe_calc_ut(tjd_ut, i, Settings.EPH_FLAGS, xx,
                    serr);

            LOGGER.log(Level.FINER, "Returned Flag is {0}, and iFlag was {1}",
                    new Object[] { rtnFlag, Settings.EPH_FLAGS });

            if (serr.length() > 0)
            {
                LOGGER.log(
                        Level.SEVERE,
                        "Error occured while calulating the planet position {0}",
                        serr.toString());
            }

            PlanetHoroDetails phd = newPlanetDetails(i, xx);
            planetDetails.add(phd);
            
            Raasi raasi = Raasi.getRaasi(phd.getLongitude());
            Planet p = phd.getPlanet();
            addPlanetToRaasi(raasi, p);
        }

        // Calculate Rahu and Ketu
        serr.delete(0, serr.length());
        double[] xx = new double[6];
        int rtnFlag;
        if (Settings.TRUE_NODE)
        {
            rtnFlag = se.swe_calc_ut(tjd_ut, SweConst.SE_TRUE_NODE,
                    Settings.EPH_FLAGS, xx, serr);
        } else
        {
            rtnFlag = se.swe_calc_ut(tjd_ut, SweConst.SE_MEAN_NODE,
                    Settings.EPH_FLAGS, xx, serr);
        }
        LOGGER.log(Level.FINER, "Returned Flag is {0}, and iFlag was {1}",
                new Object[] { rtnFlag, Settings.EPH_FLAGS });
        if (serr.length() > 0)
        {
            LOGGER.log(Level.SEVERE,
                    "Error occured while calulating the planet position {0}",
                    serr.toString());
        }

        PlanetHoroDetails phd = newPlanetDetails(Planet.RAHU.getIndex(), xx);
        planetDetails.add(phd);
        Raasi raasi = Raasi.getRaasi(phd.getLongitude());
        Planet p = phd.getPlanet();
        addPlanetToRaasi(raasi, p);

        xx[0] += 180.0D;
        phd = newPlanetDetails(Planet.KETU.getIndex(), xx);
        planetDetails.add(phd);
        raasi = Raasi.getRaasi(phd.getLongitude());
        p = phd.getPlanet();
        addPlanetToRaasi(raasi, p);

        se.swe_close();

        return planetDetails;
    }

    @Override
    public List<CuspHoroDetails> getCuspHoroDetails(HoroChartData data)
    {
        raasiToCuspMap.clear();
        List<CuspHoroDetails> cuspDetails = new ArrayList<CuspHoroDetails>();

        double tjd_ut = data.getJulianDay();
        SwissEph se = getSwissEphemeris(tjd_ut);
        
        double latitude = data.getLatitude();
        double longitude = data.getLongitude();
        double geoCenLat = (Settings.USE_GEOCENTRIC_LATITUDE) ? 
                CalcUtils.getGeocentricLatitude(latitude) : latitude;
        
        LOGGER.log(Level.FINE, "Julian day is {0}",
                ViewUtils.NUM_FORMAT.format(tjd_ut));
        LOGGER.log(Level.FINE, "Latitude {0}, Geocentric Latitude {1}",
                new Object[] { ViewUtils.toStringDegree(latitude),
                        ViewUtils.toStringDegree(geoCenLat) });

        double[] cusps = new double[37];
        double[] ascmc = new double[10];
        int rtnFlag = se.swe_houses(tjd_ut, Settings.EPH_FLAGS, geoCenLat,
                longitude, Settings.HOUSE_SYSTEM, cusps, ascmc);

        if (rtnFlag != 0)
        {
            LOGGER.log(Level.WARNING, "Error occured!!! Error code is {0}",
                    rtnFlag);
        }
        
        LOGGER.log(Level.FINE, "Ascendent : {0}", ViewUtils.toStringDegree(ascmc[0]));

        // asmc[2] is in degrees, to get in hours divide by 15
        sidrealTime = ascmc[2] / 15.0D;
        LOGGER.log(Level.FINE, "RAMC : {0}, Sidereal Time : {1}",
                new Object[] {ascmc[2], ViewUtils.toStringDegree(sidrealTime)});

//        int i = 0;
//        for (double cusp : cusps)
//        {
//            if (cusp == 0D)
//            {
//                continue;
//            }
//
//            i++;
//            CuspHoroDetails chd = newCuspHoroDetails(i, cusp);
//            cuspDetails.add(chd);
//            
//            Raasi raasi = Raasi.getRaasi(chd.getLongitude());
//            addCuspToRaasi(raasi, i);
//        }
        
        setCuspDetails(cusps, cuspDetails);

        se.swe_close();

        return cuspDetails;
    }
    
    protected void setCuspDetails(double[] cusps, List<CuspHoroDetails> cuspDetails)
    {
        int i = 0;
        for (double cusp : cusps)
        {
            if (cusp == 0D)
            {
                continue;
            }

            i++;
            CuspHoroDetails chd = newCuspHoroDetails(i, cusp);
            cuspDetails.add(chd);
            
            Raasi raasi = Raasi.getRaasi(chd.getLongitude());
            addCuspToRaasi(raasi, i);
        }
    }
    
    @Override
    public PlanetHoroDetails getFortuna(double ascLogitude, double moonLogitude, double sunLogitude)
    {
        double fortunaLongitude = ascLogitude + moonLogitude - sunLogitude;
        fortunaLongitude = CalcUtils.degnorm(fortunaLongitude);
        if (Settings.deductAyanamsa())
        {
            fortunaLongitude+=ayanamsa;
        }
        PlanetHoroDetails phd = newPlanetDetails(Constants.FORTUNA,
                new double[] {fortunaLongitude, 1, 1, 1, 1});
        return phd;
    }

    @Override
    public PlanetHoroDetails getFortuna(List<PlanetHoroDetails> planetDetails,
            List<CuspHoroDetails> cuspDetails)
    {
        double ascLongitude = cuspDetails.get(0).getLongitude();
        double moonLogitude = planetDetails.get(Planet.MOON.getIndex()).getLongitude();
        double sunLogitude = planetDetails.get(Planet.SUN.getIndex()).getLongitude();
        double fortunaLongitude = ascLongitude + moonLogitude - sunLogitude;
        fortunaLongitude = CalcUtils.degnorm(fortunaLongitude);
        if (Settings.deductAyanamsa())
        {
            fortunaLongitude+=ayanamsa;
        }
        PlanetHoroDetails phd = newPlanetDetails(Constants.FORTUNA,
                new double[] {fortunaLongitude, 1, 1, 1, 1});
        return phd;
    }
    
    @Override
    public double getSiderealTime()
    {
        return sidrealTime;
    }
    
    @Override
    public double getAyanamsa()
    {
        return ayanamsa;
    }
    
    @Override
    public void analyze(List<PlanetHoroDetails> planetDetails,
            List<CuspHoroDetails> cuspDetails)
    {
        /* set Cusp posited for each planet and each cusp having planets */
        addCuspNPlanetsToRaasiMap(planetDetails, cuspDetails);
        
        /*
         * Get star lord, sub lord, sub-sub lord for both planets and cusps
         */
        setLords(planetDetails, cuspDetails);
        
        /*
         * Analyze significators for both planets and cusps
         */
        setPlanetSignificators(planetDetails);
        setCuspSignificators(planetDetails, cuspDetails);
        
        /*
         * Analyze planet to planet aspects
         */
        AspectsTable aspectsTbl = new AspectsTable(planetDetails, cuspDetails);
        AspectInfo[][] planetToPlanetAspects = aspectsTbl.getPlanetToPlanetAspects();
        
        for (PlanetHoroDetails phd : planetDetails)
        {
            int planetIndex = phd.getPlanet().getIndex();
            
            AspectInfo[] planetAspects = planetToPlanetAspects[planetIndex];
            
            for (AspectInfo ai : planetAspects)
            {
                if (ai.getAspect().equals(ASPECT_EFFECT.NONE))
                {
                    continue;
                }
                
                int toAspect = ai.getTo();
                Planet toPlanet = Planet.getPlanet(toAspect);

                switch (ai.getAspect())
                {
                case GOOD:
                case MODERATELY_GOOD:
                case SLIGHTLY_GOOD:
                    phd.addGoodPlanetAspects(toPlanet);
                    break;
                    
                case BAD:
                case MODERATELY_BAD:
                case SLIGHTLY_BAD:
                    phd.addBadPlanetAspects(toPlanet);
                    break;
                case CONJUNCTION:
                    phd.addConjuctionPlanet(toPlanet);

                default:
                    break;
                }
            }
        }
        
        /*
         * Analyze planet to cusp aspects
         */
        AspectInfo[][] planetToCuspAspects = aspectsTbl.getPlanetToCuspAspects();
        
        for (int i = 0; i < planetToCuspAspects.length; i++)
        {
            for (int j = 0; j < planetToCuspAspects[i].length; j++)
            {
                AspectInfo ai = planetToCuspAspects[i][j];
                int planetIndex = ai.getFrom();
                int toCusp = ai.getTo();
                PlanetHoroDetails phd = planetDetails.get(planetIndex);
                switch (ai.getAspect())
                {
                case GOOD:
                case MODERATELY_GOOD:
                case SLIGHTLY_GOOD:
                    phd.addGoodCuspAspects(toCusp);
                    break;
                    
                case BAD:
                case MODERATELY_BAD:
                case SLIGHTLY_BAD:
                    phd.addBadCuspAspects(toCusp);
                    break;

                default:
                    break;
                }
            }
        }
        
        /*
         * Analyze special planet aspects for Mars, Jupiter, Saturn and
         * 7th Aspect for remain planets
         */
        // Mars special aspects 4 and 8
        PlanetHoroDetails phd = planetDetails.get(Planet.MARS.getIndex());
        addSpecialAspects(phd, new int[] {4, 8});
        
        // Jupiter special aspects 5 and 9
        phd = planetDetails.get(Planet.JUPITER.getIndex());
        addSpecialAspects(phd, new int[] {5, 9});
        
        // Saturn special aspects 3 and 10
        phd = planetDetails.get(Planet.SATURN.getIndex());
        addSpecialAspects(phd, new int[] {3, 10});
        
        /*
         * In "jupitersweb" web sites they gave special aspects for Rahu and
         * Ketu which are as follows
         * 
         * Rahu and Ketu aspect the 5th and 9th 
         * 
         * We are not going to add them till we find them in KP readers.
         */
        
        // find the 7th aspect for all planets
        add7thAspect(planetDetails);
        
        /*
         * Analyze whether planet is benefic or melefic
         */
        analyzePlanetCharacter(planetDetails, cuspDetails);
    }
    
    private void addSpecialAspects(PlanetHoroDetails phd, int[] specialAspects)
    {
        Raasi positedRaasi = phd.getPositedRaasi();
        int raasiIndex = positedRaasi.ordinal();
        
        for (int splAspect : specialAspects)
        {
            int splAspectRaasiIndex = (raasiIndex + splAspect - 1) % 12;
            Raasi aspectRaasi = Raasi.getRaasi(splAspectRaasiIndex);
            List<Planet> planetsInRaasi = raasiToPlanetMap.get(aspectRaasi);
            
            if (planetsInRaasi != null)
            {
                for (Planet p : planetsInRaasi)
                {
                    phd.addSpecialPlanetAspects(p);
                }
            }
            
            List<Integer> cuspsInRaasi = raasiToCuspMap.get(aspectRaasi);
            if (cuspsInRaasi != null)
            {
                for (Integer cusp: cuspsInRaasi)
                {
                    phd.addSpecialCuspAspects(cusp);
                }
            }
        }
    }
    
    private void add7thAspect(List<PlanetHoroDetails> planetDetails)
    {
        for (PlanetHoroDetails phd : planetDetails)
        {
            Raasi positedRaasi = phd.getPositedRaasi();
            int raasiIndex = positedRaasi.ordinal();
            
            int seventhAspectRaasiIndex = (raasiIndex + 6) % 12;
            Raasi aspectRaasi = Raasi.getRaasi(seventhAspectRaasiIndex);
            List<Planet> planetsInRaasi = raasiToPlanetMap.get(aspectRaasi);
            
            if (planetsInRaasi != null)
            {
                for (Planet p : planetsInRaasi)
                {
                    phd.addSeventhAspect(p);
                }
            }
            
            // Lather if needed, add 7th cusp aspect here.
        }
    }

    @Override
    public AspectInfo[][][] getAspects(List<PlanetHoroDetails> planetDetails,
            List<CuspHoroDetails> cuspDetails)
    {
        AspectsTable aspectsTbl = new AspectsTable(planetDetails, cuspDetails);
        
        AspectInfo[][] planetToPlanetAspects = aspectsTbl.getPlanetToPlanetAspects();
        AspectInfo[][] planetToCuspAspects = aspectsTbl.getPlanetToCuspAspects();
        
        return new AspectInfo[][][] {planetToPlanetAspects, planetToCuspAspects};
    }
    
    @Override
    public AspectInfo[][] getPlanetToPlanetAspects(
                                        List<PlanetHoroDetails> planetDetails)
    {
        AspectsTable aspectsTbl = new AspectsTable(planetDetails, null);
        
        AspectInfo[][] planetToPlanetAspects = aspectsTbl.getPlanetToPlanetAspects();
        
        return planetToPlanetAspects;
    }

    @Override
    public AspectInfo[][] getPlanetToCuspAspects(
                                        List<PlanetHoroDetails> planetDetails,
                                        List<CuspHoroDetails> cuspDetails)
    {
        AspectsTable aspectsTbl = new AspectsTable(planetDetails, cuspDetails);
        
        AspectInfo[][] planetToCuspAspects = aspectsTbl.getPlanetToCuspAspects();
        
        return planetToCuspAspects;
    }

    private void addCuspNPlanetsToRaasiMap(List<PlanetHoroDetails> planetDetails,
            List<CuspHoroDetails> cuspDetails)
    {
        // Build NavigableMap for cusps and its range
        NavigableMap<Range, Integer> cuspRange = new TreeMap<Range, Integer>();
        
        int n = cuspDetails.size();
        for (int i = 0; i < n; i++)
        {
            double begin = cuspDetails.get(i).getLongitude();
            int next = i+1;
            if (next > 11)
            {
                next = 0;
            }
            double end = cuspDetails.get(next).getLongitude();
            Range range;
            int cusp = i+1;
            if (begin < end)
            {
                range = new Range(begin, end);
                cuspRange.put(range, cusp);
            }
            else
            {
                /*
                 * This condition happens only when begin is greater than end.
                 * so there will be total 13 most of the times.
                 */
                range = new Range(begin, 360);
                cuspRange.put(range, cusp);
                Range range2 = new Range(0, end);
                cuspRange.put(range2, cusp);
            }
        }
        
        for (PlanetHoroDetails phd : planetDetails)
        {
            double longitude = phd.getLongitude();
            Range range = new Range(0D, longitude);
            Entry<Range, Integer> entry = cuspRange.ceilingEntry(range);
            int cusp = entry.getValue();
            
            phd.setCusp(cusp);
            
            CuspHoroDetails chd = cuspDetails.get(cusp-1);
            Planet planetInCusp = phd.getPlanet();
            chd.addPlanets(planetInCusp);
        }
    }

    private void setLords(List<PlanetHoroDetails> planetDetails,
            List<CuspHoroDetails> cuspDetails)
    {
        for (PlanetHoroDetails phd : planetDetails)
        {
            LOGGER.log(Level.INFO, "Process planet " + phd.getPlanet());
            
            double longitude = phd.getLongitude();
            Raasi raasi = phd.getPositedRaasi();
            Planet signLord = Planet.getSignLord(raasi);
            
            phd.addLord(signLord);
            
            List<LordNode> lordsList = CalcUtils.getDasaEntry(longitude).getValue();
            for (LordNode lord : lordsList)
            {
                Planet pl = lord.getPlanet();
                phd.addLord(pl);
            }
            Planet starLord = phd.getLords().get(1);
            int starLordIndex = starLord.getIndex();
            PlanetHoroDetails starLordPlanetDetails =  planetDetails.get(starLordIndex);
            Planet starLordPl = phd.getPlanet();
            starLordPlanetDetails.addStarLordForPlanet(starLordPl);
        }
        
        for (CuspHoroDetails chd : cuspDetails)
        {
            LOGGER.log(Level.INFO, "Process Cusp " + chd.getCusp());
            
            double longitude = chd.getLongitude();
            Raasi raasi = chd.getPositedRaasi();
            Planet signLord = Planet.getSignLord(raasi);
            
            chd.addLord(signLord);
            
            List<LordNode> lordsList = CalcUtils.getDasaEntry(longitude).getValue();
            for (LordNode lord : lordsList)
            {
                Planet pl = lord.getPlanet();
                chd.addLord(pl);
            }
        }
    }

    private void setPlanetSignificators(List<PlanetHoroDetails> planetDetails)
    {
        for (PlanetHoroDetails phd : planetDetails)
        {
            /*
             * Level 1: House Occupied by the Nakshatra Lord(NL) of the planet.
             * SIGNIFICATOR_LEVELS.A
             */
            Planet nl = phd.getNakshatraLord();
            if (nl != null)
            {
                PlanetHoroDetails nlphd = planetDetails.get(nl.getIndex());
                int cusp = nlphd.getCusp();
                phd.addCuspSignificator(SIGNIFICATOR_LEVELS.LEVEL_1, cusp);
            }
            
            /*
             * Level 2: House occupied by the planet itself
             * SIGNIFICATOR_LEVELS.B
             */
            int cusp = phd.getCusp();
            phd.addCuspSignificator(SIGNIFICATOR_LEVELS.LEVEL_2, cusp);

            /*
             * Level 3: Houses owned by the NL.
             * SIGNIFICATOR_LEVELS.C
             */
            if (nl != null)
            {
                Set<Raasi> raasis = nl.getOwnerRaasiList();
                for (Raasi r: raasis)
                {
                    List<Integer> cuspList = raasiToCuspMap.get(r);
                    if (cuspList == null || cuspList.size() == 0)
                    {
                        continue;
                    }
                    for (int c: cuspList)
                    {
                        phd.addCuspSignificator(SIGNIFICATOR_LEVELS.LEVEL_3, c);
                    }
                }
            }
            
            /*
             * Level 4: Houses owned by the planet itself
             * SIGNIFICATOR_LEVELS.D
             */
            Planet planet = phd.getPlanet();
            Set<Raasi> raasis = planet.getOwnerRaasiList();
            for (Raasi r: raasis)
            {
                List<Integer> cuspList = raasiToCuspMap.get(r);
                if (cuspList == null)
                {
                    continue;
                }
                for (int c: cuspList)
                {
                    phd.addCuspSignificator(SIGNIFICATOR_LEVELS.LEVEL_4, c);
                }
            }
        }
    }
    
    private void setCuspSignificators(List<PlanetHoroDetails> planetDetails,
            List<CuspHoroDetails> cuspDetails)
    {
        for (CuspHoroDetails chd : cuspDetails)
        {
            List<Planet> cuspPlanets = chd.getPlanets();
            
            for (Planet cuspPl : cuspPlanets)
            {
                // Level 2: planet posited in the cusp
                chd.addPlanetSignificator(SIGNIFICATOR_LEVELS.LEVEL_2, cuspPl);
                
                PlanetHoroDetails phd = planetDetails.get(cuspPl.getIndex());
                List<Planet> level1Planets = phd.getStarLordForPlanets();
                
                // Level 1: Planets in the star of the occupants of that house
                for (Planet level1pl : level1Planets)
                {
                    chd.addPlanetSignificator(SIGNIFICATOR_LEVELS.LEVEL_1, level1pl);
                }
            }
            
            Raasi cuspRaasi = chd.getPositedRaasi();
            Planet houseLord = Planet.getPlanetSign(cuspRaasi);
            // Level 4: owner of that house
            chd.addPlanetSignificator(SIGNIFICATOR_LEVELS.LEVEL_4, houseLord);
            
            // Level 3: Planets in the star of owners of that house
            PlanetHoroDetails nlOfHouseLordDetails = planetDetails.get(houseLord.getIndex());
            List<Planet> slPlanets = nlOfHouseLordDetails.getStarLordForPlanets();
            for (Planet slp : slPlanets)
            {
                chd.addPlanetSignificator(SIGNIFICATOR_LEVELS.LEVEL_3, slp);
            }
        }
    }
    
    protected SwissEph getSwissEphemeris(double tjd_ut)
    {
        SwissEph sweph = new SwissEph(Settings.EPHEMERIS_FILES_PATH);
        String ver = sweph.swe_version();
        LOGGER.log(Level.INFO, "Swiss Ephmeris Version is {0}", ver);

        sweph.swe_set_sid_mode(Settings.AYANAMSA, Settings.ayan_t0,
                Settings.ayan_initial_value);
        ayanamsa = sweph.swe_get_ayanamsa_ut(tjd_ut);
        LOGGER.log(Level.INFO, "Ayanamsa = {0} [{1}]", new Object[] {ayanamsa,
                ViewUtils.toStringDegree(ayanamsa)});
        
        return sweph;
    }

    private PlanetHoroDetails newPlanetDetails(int index, double[] xx)
    {
        boolean retrograde = (xx[3] < 0);
        double longitude = xx[0];
        boolean deductAyanamsa = Settings.deductAyanamsa();

        if (deductAyanamsa)
        {
            longitude = longitude - ayanamsa;
            longitude = CalcUtils.degnorm(longitude);
        }

        PlanetHoroDetails phd = new PlanetHoroDetails(Planet.getPlanet(index),
                longitude, retrograde);
        Raasi raasi = Raasi.getRaasi(longitude);

        phd.setPositedRaasi(raasi);

        return phd;
    }

    protected CuspHoroDetails newCuspHoroDetails(int index, double cusp)
    {
        boolean deductAyanamsa = Settings.deductAyanamsa();

        if (deductAyanamsa)
        {
            cusp -= ayanamsa;
            cusp = CalcUtils.degnorm(cusp);
        }
        Raasi raasi = Raasi.getRaasi(cusp);
        CuspHoroDetails chd = new CuspHoroDetails(index, cusp);
        chd.setPositedRaasi(raasi);

        return chd;
    }
    
    protected void addCuspToRaasi(Raasi raasi, int cusp)
    {
        List<Integer> cusps = raasiToCuspMap.get(raasi);
        if (cusps == null)
        {
            cusps = new ArrayList<Integer>();
            raasiToCuspMap.put(raasi, cusps);
        }
        
        cusps.add(cusp);
    }
    
    private void addPlanetToRaasi(Raasi raasi, Planet planet)
    {
        List<Planet> planetList = raasiToPlanetMap.get(raasi);
        if (planetList == null)
        {
            planetList = new ArrayList<Planet>();
            raasiToPlanetMap.put(raasi, planetList);
        }
        planetList.add(planet);
    }
    
    private void analyzePlanetCharacter(List<PlanetHoroDetails> planetDetails,
            List<CuspHoroDetails> cuspDetails)
    {
        /*
         * Planet is malefic if it is lord of 6th or 8th or 12th.
         * This is incomplete. yet to add conditions.
         */
        for (PlanetHoroDetails phd : planetDetails)
        {
            
            Planet p = phd.getPlanet();
            if (p.equals(Planet.RAHU) || p.equals(Planet.KETU))
            {
                Raasi sign = phd.getPositedRaasi();
                Planet signLord = Planet.getSignLord(sign);
                p = signLord;
            }

            Set<Raasi> ownerRassis = p.getOwnerRaasiList();
            breakhere:
            {
                for (Raasi raasi : ownerRassis)
                {
                    List<Integer> cuspList = raasiToCuspMap.get(raasi);
                    if (cuspList == null)
                    {
                        continue;
                    }
                    for (Integer cusp: cuspList)
                    {
                        if (cusp == 6 || cusp == 8 || cusp == 12 )
                        {
                            phd.setMelefic(true);
                            phd.setBenefic(false);
                            break breakhere;
                        }
                    }
                }
                
                phd.setMelefic(false);
                phd.setBenefic(true);
            }
        }
    }
    
    @Override
    public List<PlanetHoroDetails> getPlanetsInRaasi(Raasi raasi,
            List<PlanetHoroDetails> planetDetails)
    {
        List<PlanetHoroDetails> pds
                = new ArrayList<PlanetHoroDetails>();

        for (PlanetHoroDetails phd : planetDetails)
        {
            if (phd.getPositedRaasi().equals(raasi))
            {
                pds.add(phd);
            }
        }

        return pds;
    }
    
    @Override
    public List<CuspHoroDetails> getCuspsInRaasi(Raasi raasi,
            List<CuspHoroDetails> cuspDetails)
    {
        List<CuspHoroDetails> cusps
                = new ArrayList<CuspHoroDetails>();

        for (CuspHoroDetails phd : cuspDetails)
        {
            if (phd.getPositedRaasi().equals(raasi))
            {
                cusps.add(phd);
            }
        }

        return cusps;
    }
    
    @Override
    public String addNote(String note)
    {
        sbAnalysisNotes.append(note);
        
        return sbAnalysisNotes.toString();
    }
    
    @Override
    public String getNotes()
    {
        return sbAnalysisNotes.toString();
    }
    
    @Override
    public void clearNotes()
    {
        sbAnalysisNotes.delete(0, sbAnalysisNotes.length());
    }
    
    @Override
    public long getPlanetRiseTime(Planet planet, HoroChartData data)
    {
        double tjd_ut = SweDate.getJulDay(data.getYear(), data.getMonth()+1,
                data.getDay(), 0);
        
        LOGGER.log(Level.INFO, "Getting {0} rise time on {1}",
                new Object[] {planet.getName(), SweDate.getDate(tjd_ut) });
        
        SwissEph se = getSwissEphemeris(tjd_ut);
        DblObj tret = new DblObj();
        StringBuffer sbStarName = new StringBuffer();
        
        int flag = se.swe_rise_trans(tjd_ut, 0, sbStarName,
                0, SweConst.SE_CALC_RISE | SweConst.SE_BIT_DISC_CENTER,
                new double[] {data.getLongitude(), data.getLatitude(), 0},
                0, 20, tret, serr);
        
        LOGGER.log(Level.INFO, "flag = {0}, serr : {1}", new Object[]{flag, serr});
        
        double riseTime = tret.val;
        
        /* since the return valus is in julian date convert it to long */
        long rtnValue = SweDate.getDate(riseTime).getTime();
        
        LOGGER.log(Level.INFO, "{0} rise time {1}", new Object[]{planet, SweDate.getDate(riseTime)});
        
        return rtnValue;
    }
    
    @Override
    public long getPlanetSetTime(Planet planet, HoroChartData data)
    {
        double tjd_ut = SweDate.getJulDay(data.getYear(), data.getMonth()+1,
                data.getDay(), 0);
        
        LOGGER.log(Level.INFO, "Getting {0} set time on {1}",
                new Object[] {planet.getName(), SweDate.getDate(tjd_ut) });
        
        SwissEph se = getSwissEphemeris(tjd_ut);
        DblObj tret = new DblObj();
        StringBuffer sbStarName = new StringBuffer();
        
        int flag = se.swe_rise_trans(tjd_ut, 0, sbStarName,
                0, SweConst.SE_CALC_SET | SweConst.SE_BIT_DISC_CENTER,
                new double[] {data.getLongitude(), data.getLatitude(), 0},
                0, 20, tret, serr);
        
        LOGGER.log(Level.INFO, "flag = {0}, serr : {1}", new Object[]{flag, serr});
        
        double riseTime = tret.val;
        
        /* since the return valus is in julian date convert it to long */
        long rtnValue = SweDate.getDate(riseTime).getTime();
        
        LOGGER.log(Level.INFO, "{0} set time {1}", new Object[]{planet, SweDate.getDate(riseTime)});
        
        return rtnValue;
    }
}
