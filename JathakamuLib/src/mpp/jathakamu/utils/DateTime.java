/* 
 * Yet to decide on the license
 */

package mpp.jathakamu.utils;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import static java.time.temporal.ChronoField.INSTANT_SECONDS;
import static mpp.jathakamu.Constants.NANOS_IN_SECONDS_D;

/**
 * This utilities is intended to use for UI clients.
 * 
 * TODO: May be this can be part of mpp.jathakamu.utils package.
 *
 * @author phani
 */
public class DateTime
    implements Serializable
{
    public final static String DEFAULT_TIME_ZONE = "Asia/Calcutta";

    /**
     * Add new date(as long) to the give date and increment in years
     *
     * @param inputDate     Input date value in long
     * @param incInYears    Increment value in years
     * @param oneYear       value of one year, example 365.2425
     * @return  new Date as long
     */
    public static long addDate(long inputDate, double incInYears, double oneYear)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(inputDate);
        double dDays = incInYears * oneYear;
        int days = (int) dDays;
        double dHours = (dDays - days) * 1000.0 * 60.0 * 60.0 * 24.0;
        int milliSecs = Double.valueOf(dHours).intValue();
        cal.add(Calendar.DAY_OF_MONTH, days);
        cal.add(Calendar.MILLISECOND, milliSecs);
        long newDate = cal.getTimeInMillis();
        return newDate;
    }
    
    public static long addDate(long inputDate, double incInYears)
    {
        return addDate(inputDate, incInYears, 365.2425D);
    }

    private ZoneId timeZone = ZoneId.of(DEFAULT_TIME_ZONE);

    /* Event Date & Time in Milli-Seconds */
    private long eventDateTime;

    public DateTime(int year, int month, int dayOfMonth, int hour, int minutes,
            int seconds, int nanoOfSecond, String sTimeZone)
    {
        setTime(year, month, dayOfMonth, hour, minutes, seconds, nanoOfSecond,
                sTimeZone);
    }
    
    public DateTime(int year, int month, int dayOfMonth, int hour, int minutes,
            double secs, String sTimeZone)
    {
        int seconds = Double.valueOf(secs).intValue();
        double secsDiff = secs * NANOS_IN_SECONDS_D - seconds * NANOS_IN_SECONDS_D;
        int nanoOfSecond = Double.valueOf(secsDiff).intValue();
        setTime(year, month, dayOfMonth, hour, minutes, seconds, nanoOfSecond,
                sTimeZone);
    }
    
    public DateTime(int year, int month, int dayOfMonth, double time, String sTimeZone)
    {
        int[] timeParts = getTimeParts(time);
        int hour = timeParts[0];
        int minutes = timeParts[1];
        int seconds = timeParts[2];
        int nanoOfSecond = timeParts[3];
        setTime(year, month, dayOfMonth, hour, minutes, seconds, nanoOfSecond,
                sTimeZone);
    }
    
    public DateTime(Date date, String sTimeZone)
    {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of(sTimeZone)));
        cal.setTime(date);
        setTime(cal, sTimeZone);
    }
    
    public DateTime(Calendar cal, String sTimeZone)
    {
        setTime(cal, sTimeZone);
    }
    
    public final int[] getTimeParts(double time)
    {
        int []timeParts = new int[4];
        
        timeParts[0] = Double.valueOf(time).intValue();
        double mins = (time - timeParts[0]) * 60D;
        timeParts[1] = Double.valueOf(mins).intValue();
        double secs = (mins - timeParts[1]) * 60D;
        timeParts[2] = Double.valueOf(secs).intValue();
        double secsDiff = secs * NANOS_IN_SECONDS_D - timeParts[2] * NANOS_IN_SECONDS_D;
        timeParts[3] = Double.valueOf(secsDiff).intValue(); 
        
        return timeParts;
    }

    public Date getDateTime()
    {
        return new Date(eventDateTime);
    }
    
    private void setTime(Calendar calendar, String sTimeZone)
    {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        int nanoOfSecond = calendar.get(Calendar.MILLISECOND) * 1000;
        setTime(year, month, dayOfMonth, hours, minutes, seconds, nanoOfSecond,
                sTimeZone);
    }
    
    private void setTime(int year, int month, int dayOfMonth, int hour, int minutes,
            int seconds, int nanoOfSecond, String sTimeZone)
    {
        timeZone = ZoneId.of(sTimeZone);
        ZonedDateTime zdt = java.time.ZonedDateTime.of(year, month,
                dayOfMonth, hour, minutes, seconds, nanoOfSecond, timeZone);
        eventDateTime = zdt.getLong(INSTANT_SECONDS) * 1000L;
    }
}
