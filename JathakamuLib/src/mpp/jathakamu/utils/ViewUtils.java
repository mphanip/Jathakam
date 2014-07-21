/* 
 * Yet to decide on the license
 */
package mpp.jathakamu.utils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import mpp.jathakamu.Constants;
import mpp.jathakamu.types.Degree;

/**
 * @author Phani

 * ilities in this class are intended to be used for presentation
 * lar which can be Web or Swing
 */
public class ViewUtils
{
    public static String toStringDegree(double degree)
    {
        Degree rtn = new Degree(degree);

        return rtn.toString();
    }

    public static String toStringDegreeForRaasi(double degree)
    {
        Degree rtn = new Degree(degree);

        return rtn.toStringRaasi();
    }

    /**
     * Used for debugging
     *
     * @param degree
     * @return
     */
    public static String toStringDegree2(double degree)
    {
        StringBuilder sb = new StringBuilder();

        int deg = (int) degree;
        double dmin = (degree - deg) * 60D;
        int min = (int) dmin;
        double sec = (dmin - min) * 60D;

        sb.append(Constants.MIN_NUM_FORMAT.format(deg)).append(Constants.DEG_SEP);
        sb.append(Constants.MIN_NUM_FORMAT.format(min)).append(Constants.DEG_SEP);
        sb.append(Constants.NUM_FORMAT.format(sec));

        return sb.toString();
    }
    
    public static String toStringDegree3(double degree)
    {
        StringBuilder sb = new StringBuilder();
        
        int deg = (int) degree;
        double dmin = (degree - deg) * 60D;
        int min = (int) dmin;
        double sec = (dmin - min) * 60D;
        
        sb.append(Constants.MIN_NUM_FORMAT.format(deg)).append(Constants.DEG_SEP);
        sb.append(Constants.MIN_NUM_FORMAT.format(min)).append(Constants.DEG_SEP);
        sb.append(Constants.MIN_NUM_FORMAT.format(Math.round(sec)));
        
        return sb.toString(); 
    }

    public static String toStringDateTime(int year, int month, int day,
            double hrs)
    {
        StringBuilder sb = new StringBuilder();

        String sDate = toStringDate(year, month, day);
        sb.append(sDate);

        String sTime = toStringTime(hrs);
        sb.append(" ").append(sTime);

        return sb.toString();
    }

    public static String toStringDateTime(double years)
    {
        int yrs = (int) years;
        double dMon = (years - yrs) * 12D;
        int mths = (int) dMon;
        double dDays = (dMon - mths) * 30D;
        int days = (int) dDays;

        double dHrs = (dDays - days) * 24D;

        String rtnStr = toStringDate(yrs, mths, days)
                + toStringTime(dHrs);

        return rtnStr;
    }

    public static String toStringDate(int year, int month, int day)
    {
        StringBuilder sb = new StringBuilder();

        sb.append(day).append(Constants.DATE_SEP);
        sb.append(month).append(Constants.DATE_SEP);
        sb.append(year).append(" ");

        return sb.toString();
    }

    public static String toStringTime(double hrs)
    {
        StringBuilder sb = new StringBuilder();

        int hours = Double.valueOf(hrs).intValue();
        double mins = (hrs - hours) * 60D;
        int minutes = Double.valueOf(mins).intValue();
        double secs = (mins - minutes) * 60D;

        sb.append(hours).append(Constants.TIME_SEP);
        sb.append(minutes).append(Constants.TIME_SEP);
        sb.append(Constants.NUM_FORMAT.format(secs));

        return sb.toString();
    }

    public static String getDateString(long dateTime)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(
                Constants.DEFAULT_DATE_FORMAT);
        return sdf.format(new Date(dateTime));
    }
    
    public static String getDateAsStringLongFormat(long dateTime)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(
                Constants.DATE_FORMATT_LONG);
        return sdf.format(new Date(dateTime));
    }
    
    public static double getDegreeAsDouble(int deg, int mins, int secs)
    {
        return (deg + mins/60D + secs/3600D);
    }
    
    public static int[] getDegreeParts(double degs)
    {
        int[] degParts = new int[3];
        
        degParts[0] = Double.valueOf(degs).intValue();
        double dmin = (degs - degParts[0]) * 60D;
        degParts[1] = Double.valueOf(dmin).intValue();
        degParts[2] = Long.valueOf(Math.round((dmin - degParts[1]) * 60D)).intValue();
        
        if (degParts[2] == 60)
        {
            degParts[1]++;
            degParts[2] = 0;
        }
        
        if (degParts[1] == 60)
        {
            degParts[0]++;
            degParts[1] = 0;
        }
        
        return degParts;
    }
}
