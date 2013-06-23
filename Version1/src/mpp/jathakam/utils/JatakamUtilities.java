/*
 *
 *
 */
package mpp.jathakam.utils;

import static mpp.jathakam.core.JathakamMain.LOGGER;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import mpp.jathakam.core.BirthDateTime;
import mpp.jathakam.core.PlaceInfo;
import mpp.jathakam.core.TimeZoneInfo;
import swisseph.SweDate;

/**
 *
 * @author phani
 */
public final class JatakamUtilities
{
    public final static NumberFormat NUM_FORMAT = NumberFormat.getNumberInstance();
    public final static NumberFormat DEG_NUM_FORMAT = NumberFormat.getNumberInstance();
    public final static NumberFormat MIN_NUM_FORMAT = NumberFormat.getNumberInstance();
    public final static SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    
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

    private final static String DEG_SEP = "-";
    private final static String TIME_SEP = ":";
    private final static String DATE_SEP = "/";

    private final static double D_HOURS_IN_MILLS = 60D * 60D * 1000D;
    private final static long L_HOURS_IN_MILLS = 60 * 60 * 1000;
    private final static long L_MINS_IN_MILLIS = 60 * 1000;

    public static String toStringDegree(double degree)
    {
        long lDegree = Double.valueOf(Math.round(degree * D_HOURS_IN_MILLS)).longValue();

        long deg = lDegree / L_HOURS_IN_MILLS;
        long min = (lDegree - (deg * L_HOURS_IN_MILLS)) / L_MINS_IN_MILLIS;
        double sec = (lDegree - (deg * L_HOURS_IN_MILLS) - (min * L_MINS_IN_MILLIS)) / 1000D;

        StringBuilder sb = new StringBuilder();
        sb.append(DEG_NUM_FORMAT.format(deg)).append(DEG_SEP);
        sb.append(MIN_NUM_FORMAT.format(min)).append(DEG_SEP);
        sb.append(NUM_FORMAT.format(sec));

        return sb.toString();
    }

    public static String toStringDegreeForRaasi(double degree)
    {
        long lDegree = Double.valueOf(Math.round(degree * D_HOURS_IN_MILLS)).longValue();

        long deg = lDegree / L_HOURS_IN_MILLS;
        long min = (lDegree - (deg * L_HOURS_IN_MILLS)) / L_MINS_IN_MILLIS;
        double sec = (lDegree - (deg * L_HOURS_IN_MILLS) - (min * L_MINS_IN_MILLIS)) / 1000D;

        StringBuilder sb = new StringBuilder();
        sb.append(MIN_NUM_FORMAT.format(deg)).append(DEG_SEP);
        sb.append(MIN_NUM_FORMAT.format(min)).append(DEG_SEP);
        sb.append(NUM_FORMAT.format(sec));

        return sb.toString();
    }

    public static String toStringDateTime(int year, int month, int day, double hrs)
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
        
        double dHrs = (dDays-days) * 24D;

        String rtnStr = toStringDate(yrs, mths, days) + toStringTime(dHrs);

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

    public static double getGeocentricLatitude(double geolat)
    {
        double geolatInRad = Math.toRadians(geolat);
        double t1 = Math.tan(geolatInRad);

        /*
         * For more precession, we can try Polar Radius / Equatorial Radius i.e
         * 6356.755/6378.140
         */
        t1 = t1 * 0.99330546D;

        t1 = Math.atan(t1);

        t1 = Math.toDegrees(t1);

        return t1;
    }

    public static BirthDateTime convertToUT(BirthDateTime bdt, double dTimeZone)
    {
        int iyear = bdt.getYear();
        int imonth = bdt.getMonth();
        int iday = bdt.getDay();
        int ihour = bdt.getHours();
        int imin = bdt.getMinutes();
        double dsec = bdt.getSeconds();
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, iyear);
        cal.set(Calendar.MONTH, imonth-1);
        cal.set(Calendar.DAY_OF_MONTH, iday);
        cal.set(Calendar.HOUR_OF_DAY, ihour);
        cal.set(Calendar.MINUTE, imin);
        
        int millis = (int) (dsec * 1000);
        int secs = millis / 1000;
        int millisecs = millis - secs * 1000;
        
        cal.set(Calendar.SECOND, secs);
        cal.set(Calendar.MILLISECOND, millisecs);
        
        long timeOffset = (long) (dTimeZone * D_HOURS_IN_MILLS);
        
        long localTime = cal.getTimeInMillis();
        
        long utcTime = localTime - timeOffset;
        
//        SweDate sweDate = new SweDate(iyear, imonth, iday, (ihour + imin/60D + dsec / 3600D) );
//        Date utcDate = sweDate.getDate(-1*timeOffset);
        cal.setTimeInMillis(utcTime);

		BirthDateTime bdtUT = new BirthDateTime(
				cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE),
				(cal.get(Calendar.SECOND) + cal.get(Calendar.MILLISECOND) / 1000D));

        return bdtUT;
    }

    public static double getJulianDay_UT(BirthDateTime bi, PlaceInfo placeInfo,
                                      boolean isGregorianCalendar)
    {
        String tzName = placeInfo.getTimezoneDisplayName();
        double tzoffset = TimeZoneInfo.getOffset(tzName);

        LOGGER.log(Level.FINE, "Standard Time zone offset : {0}", tzoffset);

        BirthDateTime bi_ut = convertToUT(bi, tzoffset);
        LOGGER.log(Level.FINE, "Cooridnated Universal Time : {0}", bi_ut);

        double tjd_ut = SweDate.getJulDay(bi_ut.getYear(), bi_ut.getMonth(),
            bi_ut.getDay(), bi_ut.getTime());

        return tjd_ut;
    }

    public static BirthDateTime getLMT(BirthDateTime bi, PlaceInfo placeInfo)
    {
        int y = bi.getYear();
        /* Calendar month is ZERO based */
        int m = bi.getMonth()-1;
        int d = bi.getDay();
        int h = bi.getHours();
        int mins = bi.getMinutes();
        double s = bi.getSeconds();
        int secs = Double.valueOf(s).intValue();
        int millis = Double.valueOf((s - (double)secs) * 1000).intValue();

        Calendar cal = Calendar.getInstance();
        cal.set(y, m, d, h, mins, secs);
        cal.set(Calendar.MILLISECOND, millis);

        String id = TimeZoneInfo.getJavaStringId(placeInfo.getTimezoneDisplayName());
        TimeZone tz = TimeZone.getTimeZone(id);
        cal.setTimeZone(tz);

        double longitude = placeInfo.getLogitude();
        String tzName = placeInfo.getTimezoneDisplayName();
        double stdLongitude = TimeZoneInfo.getStdLongitude(tzName);

        double diff = (longitude * 4D * 60D * 1000D) - (stdLongitude * 4D * 60D * 1000D);
        long diffInMillis = Double.valueOf(diff).longValue();

        long timeInMillis = cal.getTimeInMillis();
        long lmtTimeInMillis = timeInMillis + diffInMillis;
        cal.setTimeInMillis(lmtTimeInMillis);

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH)+1;
        d = cal.get(Calendar.DAY_OF_MONTH);
        h = cal.get(Calendar.HOUR_OF_DAY);
        mins = cal.get(Calendar.MINUTE);
        s = cal.get(Calendar.SECOND) + (cal.get(Calendar.MILLISECOND)/1000D);

        return new BirthDateTime(y, m, d, h, mins, s);
    }

    public static BirthDateTime getLMT2(BirthDateTime bi, PlaceInfo placeInfo)
    {
        double longitude = placeInfo.getLogitude();
        String tzName = placeInfo.getTimezoneDisplayName();
        double stdLongitude = TimeZoneInfo.getStdLongitude(tzName);
        double diff = ((stdLongitude - longitude) * 4D)/60D;

        BirthDateTime lmt = convertToUT(bi, diff);

        return lmt;
    }

    public static double getMoonPositionGivenAsc(double ascPosition)
    {
    	// Lagna Position * 81/360
    	double moonPosition = ascPosition * 81D / 360D;
    	
    	// Round-off moonPostion
    	long iMoonPos = Double.valueOf(moonPosition).longValue();
    	moonPosition = moonPosition - iMoonPos;
    	
    	return moonPosition * 360D;
    }
    
    /*
     * Do not use this since it has to be modified similar to addDate
     */
    public static long addDate1(long inputDate, double incDate)
    {
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(inputDate);
    	
        int yrs = (int) incDate;
        double dMon = (incDate - yrs) * 12D;
        int mths = (int) dMon;
        double dDays = (dMon - mths) * 30D;
        int days = (int) dDays;
        double dHrs = (dDays-days) * 24D;
        int milliSecs = Double.valueOf(dHrs * 60 * 60 * 1000).intValue();
        
        cal.add(Calendar.YEAR, yrs);
        cal.add(Calendar.MONTH, mths);
        cal.add(Calendar.DAY_OF_MONTH, days);
        cal.add(Calendar.MILLISECOND, milliSecs);
        
        long newDate = cal.getTimeInMillis();
    	
    	return newDate;
    }
    
    public static long addDate(long inputDate, double incInYears)
    {
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(inputDate);
    	
    	// Convert to days
    	double dDays = incInYears * 365.2425;
    	int days = (int) dDays;
    	double dHours = (dDays - days) * 1000D * 60D * 60D * 24D;
    	
    	// Convert the remaining time to MilliSeconds
    	int milliSecs = Double.valueOf(dHours).intValue();

        cal.add(Calendar.DAY_OF_MONTH, days);
        cal.add(Calendar.MILLISECOND, milliSecs);
    	
        long newDate = cal.getTimeInMillis();
    	
    	return newDate;
    }
    
    public static String getDateString(long dateTime)
    {
    	return DEFAULT_DATE_FORMAT.format(new Date(dateTime));
    }
    
    public static String getDefaultTimeZone()
    {
	    Set<String> tzi = TimeZoneInfo.getAllDisplayNames();
	    String tz = "";
	
	    for (String name : tzi)
	    {
	        if (name.toUpperCase().startsWith("IST"))
	        {
	            tz = name;
	            break;
	        }
	    }
	
	    return tz;
    }

    public static void setLoggerSettings(Logger logger, String logFileName, Level defaultLogLevel)
    {
        try
        {
            FileHandler hand = new FileHandler(logFileName);
            SimpleFormatter formatter = new SimpleFormatter();
            hand.setFormatter(formatter);

            logger.addHandler(hand);

            if (defaultLogLevel != null)
            {
                logger.setLevel(defaultLogLevel);
                return;
            }

            String logLevel = System.getProperty("LOG_LEVEL", "FINE");
            if (logLevel.equalsIgnoreCase("OFF"))
            {
                logger.setLevel(Level.OFF);
            }
            else if (logLevel.equalsIgnoreCase("SEVERE"))
            {
                logger.setLevel(Level.SEVERE);
            }
            else if (logLevel.equalsIgnoreCase("WARNING"))
            {
                logger.setLevel(Level.WARNING);
            }
            else if (logLevel.equalsIgnoreCase("INFO"))
            {
                logger.setLevel(Level.FINE);
            }
            else if (logLevel.equalsIgnoreCase("FINE"))
            {
                logger.setLevel(Level.FINE);
            }
            else if (logLevel.equalsIgnoreCase("FINER"))
            {
                logger.setLevel(Level.FINER);
            }
            else if (logLevel.equalsIgnoreCase("FINEST"))
            {
                logger.setLevel(Level.FINEST);
            }
            else if (logLevel.equalsIgnoreCase("ALL"))
            {
                logger.setLevel(Level.ALL);
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(JatakamUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SecurityException ex)
        {
        	Logger.getLogger(JatakamUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
