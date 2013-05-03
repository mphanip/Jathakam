package mpp.jathakamu;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import swisseph.SweConst;
import swisseph.SweDate;
import swisseph.SwissEph;

import mpp.jathakamu.Constants.HORARY_NUMBER_SET;
import mpp.jathakamu.settings.Settings;
import mpp.jathakamu.types.CuspHoroDetails;
import mpp.jathakamu.types.HoroChartData;
import mpp.jathakamu.types.Raasi;
import mpp.jathakamu.utils.CalcUtils;
import mpp.jathakamu.view.ViewUtils;

import static mpp.jathakamu.JathakamLogger.LOGGER;

public class HoraryHoroscope
    extends NatalHoroscope
{
    @Override
    public List<CuspHoroDetails> getCuspHoroDetails(HoroChartData data)
    {
        if (data.getHoraryNumber() < 1)
        {
            throw new RuntimeException(new JatakamException("Invalid horary number : " + data.getHoraryNumber()));
        }
        
        LOGGER.log(Level.INFO, "Building Horary chart for {0}/{1} ",
                new Object[]{data.getHoraryNumber(), Settings.HORARY_NUMBER_GROUP});
        
        raasiToCuspMap.clear();
        List<CuspHoroDetails> cuspDetails = new ArrayList<CuspHoroDetails>();
        
        double tjd_ut = data.getJulianDay();
        double latitude = data.getLatitude();
        int horaryNum = data.getHoraryNumber();
        HORARY_NUMBER_SET horarySet = Settings.HORARY_NUMBER_GROUP;
        
        LOGGER.log(Level.INFO, "Given date is {0}", ViewUtils.getDateString(SweDate.getDate(tjd_ut).getTime()));
        SwissEph se = getSwissEphemeris(tjd_ut);
        double asc = 0;
        
        if (horarySet.equals(HORARY_NUMBER_SET.HORARY_249))
            asc = CalcUtils.getAscendent(horaryNum);
        else
            asc = CalcUtils.getAscendent2193(horaryNum);
        
        Raasi positedRaasi = Raasi.getRaasi(asc);
        LOGGER.log(Level.INFO, "Asc: " + asc + "[" + positedRaasi + " : "
                + ViewUtils.toStringDegreeForRaasi(asc) + "]");
        
        // Add ayanamsa to get Sayana Ayanamsa
        asc += ayanamsa;

        /*
         * x[0] = true obliqutiy of the Ecliptic (includes nutation)
         * x[1] = mean obliquity of the Ecliptic
         * x[2] = nutation in longitude
         * x[3] = nutation in obliquity
         * x[4] = x[5] = 0
         */
        double[] xx = new double[6];
        se.swe_calc_ut(tjd_ut, SweConst.SE_ECL_NUT, 0, xx, serr);

        LOGGER.log(Level.INFO, "obliquity of the ecliptic : " + xx[0]);

        double e = StrictMath.toRadians(xx[0]);
        double a = StrictMath.toRadians(asc);
        double v1 = StrictMath.cos(e);
        double v2 = StrictMath.tan(a);
        double v3 = StrictMath.sin(e);
        double v4 = StrictMath.sin(a);
        double v5 = v1 * v2;
        double v6 = v3 * v4;
        double ra = StrictMath.atan(v5);
        double d = StrictMath.asin(v6);
        double geocentriclat = CalcUtils.getGeocentricLatitude(latitude);
        double l = StrictMath.toRadians(geocentriclat);
        double oa = ra - StrictMath.asin(StrictMath.tan(d) * StrictMath.tan(l));
        double oaInDeg = StrictMath.toDegrees(oa);
        double ramc = StrictMath.abs(oaInDeg + 90);

        // 0 > Asc < 90 Add 270 to oa
        // 270 > Asc < 360 Add 270 to oa
        // i.e. add 180 degrees to oaInDeg
        if ((asc >= 0 && asc <= 90) || (asc > 270 && asc <= 360))
        {
            ramc += 180D;
        }
        sidrealTime = StrictMath.abs(ramc) / 15D;
        
        LOGGER.log(Level.INFO,
                "geocentriclat : {0}, RAMC : {1}, Sidereal Time : {2}",
                new Object[] { ViewUtils.toStringDegree2(geocentriclat), ramc,
                        ViewUtils.toStringDegree2(sidrealTime) });

        double eps = xx[0];
        double[] cusps = new double[37];
        double[] ascmc = new double[10];

        se.swe_houses_armc(ramc, geocentriclat, eps, Settings.HOUSE_SYSTEM, cusps,
                ascmc);
        
        setCuspDetails(cusps, cuspDetails);

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
            cusp -= ayanamsa;
            cusp = CalcUtils.degnorm(cusp);
            Raasi raasi = Raasi.getRaasi(cusp);
            CuspHoroDetails chd = new CuspHoroDetails(i, cusp);
            chd.setPositedRaasi(raasi);
            cuspDetails.add(chd);
            
            addCuspToRaasi(raasi, i);
        }
    }
}
