/*
 *
 *
 */
package mpp.jathakam.core.dc;

import mpp.jathakam.core.Raasi;

/**
 *
 * @author phani
 */
public class D9
    extends BaseDivisionalChart
{
    public D9(double longitude)
    {
        super("D9", (short) 9);
        this.longitude = longitude;
    }

    /**
     * D9 Reference Table
     *
     * Degree Aries Taurus Gemini Cancer Leo Virgo Libra Scorpio Sagittarius CApricorn Aquarius Pisces
     * 3-20     1    10      7      4     1     10   7      4       1           10       7       4
     * 6-40     2    11      8      5
     * 10-00    3    12      9      6
     * 13-20    4     1      10     7
     * 16-40    5     2      11     8
     * 20-00    6     3      12     9
     * 23-20    7     4      1      10
     * 26-40    8     5      2      11
     * 30-00    9     6      3      12
     *
     * @param longitude
     * @return
     */
    @Override
    public Raasi getRaasi()
    {
        Raasi raasi = Raasi.getRaasi(longitude);
        int start  = 1;
        switch (raasi.ordinal())
        {
            case 2:
            case 6:
            case 10:
                start = 10;
                break;
            case 3:
            case 7:
            case 11:
                start = 7;
                break;
            case 4:
            case 8:
            case 12:
                start = 4;
                break;
        }

        int planetPositionInRaasiDivision = getDivisionIndex();

        int dcRaasi = (start + planetPositionInRaasiDivision) % 12;
        
        if (dcRaasi == 0)
        	dcRaasi = 12;

        Raasi rtnRaasi = Raasi.getRaasi(dcRaasi);

        return rtnRaasi;
    }

}
