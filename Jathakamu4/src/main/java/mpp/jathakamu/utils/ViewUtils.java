/* 
 * Yet to decide on the license
 */
package mpp.jathakamu.utils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import mpp.jathakamu.Constants;

/**
 * @author Phani

 * Utilities in this class are intended to be used for presentation
 * which can be Web or Swing
 */
public class ViewUtils
{

    public final static NumberFormat NUM_FORMAT
            = NumberFormat.getNumberInstance();

    public final static NumberFormat DEG_NUM_FORMAT
            = NumberFormat.getNumberInstance();

    public final static NumberFormat MIN_NUM_FORMAT
            = NumberFormat.getNumberInstance();

    public final static String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    
    public final static String DATE_FORMATT_LONG = "EEE, dd/MM/yyyy HH:mm:ss";

    public final static String DEG_SEP = "-";
    public final static String TIME_SEP = ":";
    public final static String DATE_SEP = "/";

    static
    {
        NUM_FORMAT.setGroupingUsed(false);
        NUM_FORMAT.setMaximumFractionDigits(2);
        NUM_FORMAT.setMinimumFractionDigits(2);
        NUM_FORMAT.setMinimumIntegerDigits(2);

        DEG_NUM_FORMAT.setGroupingUsed(false);
        DEG_NUM_FORMAT.setMaximumFractionDigits(0);
        DEG_NUM_FORMAT.setMinimumFractionDigits(0);
        DEG_NUM_FORMAT.setMinimumIntegerDigits(3);

        MIN_NUM_FORMAT.setGroupingUsed(false);
        MIN_NUM_FORMAT.setMaximumFractionDigits(0);
        MIN_NUM_FORMAT.setMinimumFractionDigits(0);
        MIN_NUM_FORMAT.setMinimumIntegerDigits(2);
    }

    public static String toStringDegree(double degree)
    {
        long lDegree = Double.valueOf(
                Math.round(degree * Constants.HOURS_IN_MILLS))
                .longValue();

        long deg = lDegree / Constants.HOURS_IN_MILLS;
        long min = (lDegree - (deg * Constants.HOURS_IN_MILLS))
                / Constants.MINS_IN_MILLIS;
        double sec = (lDegree - (deg * Constants.HOURS_IN_MILLS) - (min * Constants.MINS_IN_MILLIS)) / 1000D;

        StringBuilder sb = new StringBuilder();
        sb.append(DEG_NUM_FORMAT.format(deg)).append(
                DEG_SEP);
        sb.append(MIN_NUM_FORMAT.format(min)).append(
                DEG_SEP);
        sb.append(NUM_FORMAT.format(sec));

        return sb.toString();
    }

    public static String toStringDegreeForRaasi(double degree)
    {
        int deg = (int) degree;
        double dmin = (degree - deg) * 60D;
        int min = (int) dmin;
        double sec = (dmin - min) * 60D;

        StringBuilder sb = new StringBuilder();
        sb.append(MIN_NUM_FORMAT.format(deg % 30)).append(DEG_SEP);
        sb.append(MIN_NUM_FORMAT.format(min)).append(DEG_SEP);
        sb.append(MIN_NUM_FORMAT.format(Math.round(sec)));

        return sb.toString();
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

        sb.append(MIN_NUM_FORMAT.format(deg)).append(DEG_SEP);
        sb.append(MIN_NUM_FORMAT.format(min)).append(DEG_SEP);
        sb.append(NUM_FORMAT.format(sec));

        return sb.toString();
    }
    
    public static String toStringDegree3(double degree)
    {
        StringBuilder sb = new StringBuilder();
        
        int deg = (int) degree;
        double dmin = (degree - deg) * 60D;
        int min = (int) dmin;
        double sec = (dmin - min) * 60D;
        
        sb.append(MIN_NUM_FORMAT.format(deg)).append(DEG_SEP);
        sb.append(MIN_NUM_FORMAT.format(min)).append(DEG_SEP);
        sb.append(MIN_NUM_FORMAT.format(Math.round(sec)));
        
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

        sb.append(day).append(DATE_SEP);
        sb.append(month).append(DATE_SEP);
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

        sb.append(hours).append(TIME_SEP);
        sb.append(minutes).append(TIME_SEP);
        sb.append(NUM_FORMAT.format(secs));

        return sb.toString();
    }

    public static String getDateString(long dateTime)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(
                ViewUtils.DEFAULT_DATE_FORMAT);
        return sdf.format(new Date(dateTime));
    }
    
    public static String getDateAsStringLongFormat(long dateTime)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(
                ViewUtils.DATE_FORMATT_LONG);
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
        
        if (degParts[2] >= 60)
        {
            degParts[1]++;
            degParts[2] = degParts[1] - 60;
        }

        if (degParts[1] >= 60)
        {
            degParts[0]++;
            degParts[1] = degParts[1] - 60;    
        }

        return degParts;
    }
}
