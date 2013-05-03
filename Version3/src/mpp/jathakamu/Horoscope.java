package mpp.jathakamu;

import java.util.List;

import mpp.jathakamu.types.AspectInfo;
import mpp.jathakamu.types.CuspHoroDetails;
import mpp.jathakamu.types.HoroChartData;
import mpp.jathakamu.types.Planet;
import mpp.jathakamu.types.PlanetHoroDetails;
import mpp.jathakamu.types.Raasi;

public interface Horoscope
{
    /**
     * This should be one of the first call. see <code>getCuspHoroDetails</code>
     * Get all the planet details. Note that only logitude, posited raasi are
     * calculated. call analyze to populate other fields.
     * 
     * @param data
     *            provide all the detail for calculation in DateTimeLocation
     * @return List of PlanetHoroDetails
     */
    List<PlanetHoroDetails> getPlanetHoroDetails(HoroChartData data);

    /**
     * This should be one of the first call. see
     * <code>getPlanetHoroDetails</code> Get all the Cusp details. Note that
     * only logitude, posited raasi are calculated. call analyze to populate
     * other fields.
     * 
     * @param data
     *            data provide all the detail for calculation in
     *            DateTimeLocation
     * @return List of CuspHoroDetails
     */
    List<CuspHoroDetails> getCuspHoroDetails(HoroChartData data);
    
    /**
     *
     * @param planetDetails
     *              List of PlanetHoroDetails
     * @param cuspDetails
     *              List of CuspHoroDetails
     * @return fortuna details
     */
    PlanetHoroDetails getFortuna(List<PlanetHoroDetails> planetDetails,
            List<CuspHoroDetails> cuspDetails);
    
    /**
     * Get Fortuna details. Should be called only after calling
     * getPlanetHoroDetails and getCuspHoroDetails
     * 
     * @param ascLogitude
     *            Lagna Longitude
     * @param moonLogitude
     *            Moon Longitude
     * @param sunLogitude
     *            Sun Longitude
     * @return fortuna details
     */
    PlanetHoroDetails getFortuna(double ascLogitude, double moonLogitude,
            double sunLogitude);
    
    /**
     * Get sidereal time. Should be called only after calling
     * getPlanetHoroDetails and getCuspHoroDetails
     * 
     * @return sidereal time for the give data and time
     */
    double getSiderealTime();
    
    /**
     * Get ayanamsa used to calculate planet and cusp positions.
     * 
     * @return ayanamsa, default is Old Krishnamurthi Ayanamsa
     */
    double getAyanamsa();

    /**
     * Call this method just after calling getPlanetHoroDetails and getCuspHoroDetails. This will
     * calculate the following
     * 1) populate all the field in PlanetHoroDetails
     * 2) populate all the field in CuspHoroDetails
     * 3) calculate aspects
     * 4) Calculate significators
     * 5) calculate lords
     * 
     * @param planetDetails List of PlanetHoroDetails
     * @param cuspDetails List of CuspHoroDetails
     */
    void analyze(List<PlanetHoroDetails> planetDetails,
            List<CuspHoroDetails> cuspDetails);

    /**
     * Get Planet to planet and planet to cusp aspects
     * 
     * @param planetDetails
     *            List of PlanetHoroDetails
     * @param cuspDetails
     *            List of CuspHoroDetails
     * @return 2 double dimension array, first is planet to planet and second is
     *         planet to cusp
     */
    AspectInfo[][][] getAspects(List<PlanetHoroDetails> planetDetails,
            List<CuspHoroDetails> cuspDetails);
    
    /**
     * Get Planet to planet aspects
     * 
     * @param planetDetails
     *            List of PlanetHoroDetails (getPlanetHoroDetails)
     * @return planet to planet aspects
     */
    AspectInfo[][] getPlanetToPlanetAspects(
            List<PlanetHoroDetails> planetDetails);
    
    /**
     * Get planet to cusp aspects
     * 
     * @param planetDetails List of PlanetHoroDetails
     * @param cuspDetails List of CuspHoroDetails
     * @return planet to cusp aspects
     */
    AspectInfo[][] getPlanetToCuspAspects(
            List<PlanetHoroDetails> planetDetails,
            List<CuspHoroDetails> cuspDetails);
    
    /**
     * Convenience method to get planets in a raasi
     * 
     * @param raasi
     *                  Planets posited in the given raasi
     * @param planetDetails
     *                  List of PlanetHoroDetails
     * @return 
     *          Empty list or planet list
     */
    List<PlanetHoroDetails> getPlanetsInRaasi(Raasi raasi,
            List<PlanetHoroDetails> planetDetails);
    
    /**
     * Convenience method to get cusps in a raasi
     * @param raasi
     *                  cusps posited in the given raasi
     * @param cuspDetails
     *                  List of CuspHoroDetails
     * @return 
     *          Empty list or cusps list
     */
    List<CuspHoroDetails> getCuspsInRaasi(Raasi raasi,
            List<CuspHoroDetails> cuspDetails);
    
    /**
     * Get Planet rise time, mainly used for sunrise or moonrise
     * 
     * @param planet
     * @param data
     * @return
     */
    long getPlanetRiseTime(Planet planet, HoroChartData data);
    
    /**
     * Get Planet rise time, mainly used for sunset or moonset
     * 
     * @param planet
     * @param data
     * @return
     */
    long getPlanetSetTime(Planet planet, HoroChartData data);
    
    /**
     * To add any notes for this horoscope
     * @param note any string is accepted without any validation
     * @return
     */
    String addNote(String note);
    
    /**
     * return the notes added.
     * 
     * @return
     */
    String getNotes();
    
    /**
     * clear notes
     */
    void clearNotes();
}
