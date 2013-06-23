/*
 *
 *
 */
package mpp.jathakam.core.dc;

import java.util.List;

import mpp.jathakam.core.Raasi;
import mpp.jathakam.core.Range;

/**
 *
 * @author phani
 */
public interface DivisionalChart
{
    /**
     *
     * @return name of the divisional chart for example "D9"
     */
    public String getName();

    /**
     * @return List of Range(Extent) for the divisional chart
     */
    public List<Range> getDivisions();

    /**
     * Get the divisional chart division
     * @return division for this chart
     */
    public short getDivision();

    /**
     * Get divisional chart longitude
     * @param longitude
     * @return
     */
    public double getDCLongitude();

    /**
     * To get the position of the Divisional chart Raasi aka Sign.
     *
     * @param raasi_d1
     * @param planetLongitude
     * @return Divisional chart Raasi/Sign
     */
    public Raasi getRaasi();
    
    public void setLongitude(double longitude);
    
    public double getLongitude();
}
