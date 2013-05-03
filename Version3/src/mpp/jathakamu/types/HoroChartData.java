/*
 *
 *
 */
package mpp.jathakamu.types;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

import mpp.jathakamu.Constants.HORARY_NUMBER_SET;
import mpp.jathakamu.view.ViewUtils;
import swisseph.SweDate;

/**
 * This is the one of the main entry point class to provide fact for the natal
 * or horary chart.
 * 
 * @author phani
 */
public class HoroChartData
    implements Serializable, Cloneable
{
    private static final long serialVersionUID = -4969849249465183846L;

    private int year;
    private int month;
    private int dayOfMonth;
    private int hours;
    private int minutes;
    private double seconds;
    private long dateTimeInMillis;
    private double julianDay;
    private String timeZoneStringID;
    private double longitude;
    private double latitude;
    private long lmtDateTimeInMillis;
    private int horaryNumber = 0;

    private HoroChartData()
    {
        super();
    }

    public HoroChartData(int year, int month, int dayOfMonth, int hours,
            int minutes, double seconds, String timeZoneStringID,
            double longitude, double latitude, int horaryNumber)
    {
        this(year, month, dayOfMonth, hours, minutes, seconds,
                timeZoneStringID, longitude, latitude);
        this.horaryNumber = horaryNumber;
    }

    public HoroChartData(int year, int month, int dayOfMonth, int hours,
                            int mins, double secs, String timezoneStringID,
                            double longitude, double latitude)
    {
        super();
        timeZoneStringID = timezoneStringID;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.hours = hours;
        this.minutes = mins;
        this.seconds = secs;
        this.longitude = longitude;
        this.latitude = latitude;
        initialize();
    }
    
    public HoroChartData(long dateTime, String timezoneStringID,
            double longitude, double latitude, int horaryNumber)
    {
        timeZoneStringID = timezoneStringID;
        this.horaryNumber = horaryNumber;
        this.longitude = longitude;
        this.latitude = latitude;
        
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateTime);
        this.year = cal.get(Calendar.YEAR);
        this.month = cal.get(Calendar.MONTH);
        this.dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        this.hours = cal.get(Calendar.HOUR_OF_DAY);
        this.minutes = cal.get(Calendar.MINUTE);
        this.seconds = cal.get(Calendar.SECOND) + cal.get(Calendar.MILLISECOND);
        initialize();
    }
    
    public HoroChartData(Calendar cal,
            double longitude, double latitude, int horaryNumber)
    {
        timeZoneStringID = cal.getTimeZone().getID();
        this.year = cal.get(Calendar.YEAR);
        this.month = cal.get(Calendar.MONTH);
        this.dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        this.hours = cal.get(Calendar.HOUR_OF_DAY);
        this.minutes = cal.get(Calendar.MINUTE);
        this.seconds = cal.get(Calendar.SECOND) + cal.get(Calendar.MILLISECOND);
        this.horaryNumber = horaryNumber;
        this.longitude = longitude;
        this.latitude = latitude;

        initialize();
    }
    
    public HoroChartData(long dateTime, String timezoneStringID,
            double longitude, double latitude)
    {
        timeZoneStringID = timezoneStringID;
        this.longitude = longitude;
        this.latitude = latitude;
        
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateTime);
        this.year = cal.get(Calendar.YEAR);
        this.month = cal.get(Calendar.MONTH);
        this.dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        this.hours = cal.get(Calendar.HOUR_OF_DAY);
        this.minutes = cal.get(Calendar.MINUTE);
        this.seconds = cal.get(Calendar.SECOND) + cal.get(Calendar.MILLISECOND);

        initialize();
    }
    
    public HoroChartData(Calendar cal,
            double longitude, double latitude)
    {
        timeZoneStringID = cal.getTimeZone().getID();
        this.year = cal.get(Calendar.YEAR);
        this.month = cal.get(Calendar.MONTH);
        this.dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        this.hours = cal.get(Calendar.HOUR_OF_DAY);
        this.minutes = cal.get(Calendar.MINUTE);
        this.seconds = cal.get(Calendar.SECOND) + cal.get(Calendar.MILLISECOND);
        this.longitude = longitude;
        this.latitude = latitude;

        initialize();
    }

    private void initialize()
    {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timeZoneStringID));
        int sec = Double.valueOf(seconds).intValue();
        int millis = Double.valueOf((seconds - sec) * 1000).intValue();

        cal.set(year, month, dayOfMonth, hours, minutes, sec);
        cal.set(Calendar.MILLISECOND, millis);
        cal.setTimeZone(TimeZone.getTimeZone(timeZoneStringID));

        /*
         * Below "get" methods are needed for correct calcualation of date and
         * time
         */
        cal.get(Calendar.YEAR);
        cal.get(Calendar.MONTH);
        cal.get(Calendar.DAY_OF_MONTH);
        cal.get(Calendar.HOUR_OF_DAY);
        cal.get(Calendar.MINUTE);
        cal.get(Calendar.SECOND);

        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        
        year =  cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        hours = cal.get(Calendar.HOUR_OF_DAY);
        minutes = cal.get(Calendar.MINUTE);
        seconds = cal.get(Calendar.SECOND) + cal.get(Calendar.MILLISECOND)/1000D;
        
        dateTimeInMillis = cal.getTimeInMillis();
        julianDay = _getJulianDay();
        setLMT();
    }

    public int getDay()
    {
        return dayOfMonth;
    }

    public int getMonth()
    {
        return month;
    }

    public int getYear()
    {
        return year;
    }

    public int getHours()
    {
        return hours;
    }

    public int getMinutes()
    {
        return minutes;
    }

    public double getSeconds()
    {
        return seconds;
    }
    
    public double getLongitude()
    {
        return longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getTime()
    {
        return (hours + minutes / 60D + seconds / 3600D);
    }
    
    public long getTimeInMillis()
    {
        return dateTimeInMillis;
    }
    
    private double _getJulianDay()
    {
        double dHours = getTime();
        double jd = SweDate.getJulDay(year, month+1, dayOfMonth, dHours);
        
        return jd;
    }

    public double getJulianDay()
    {
        return julianDay;
    }
    
    private void setLMT()
    {
        TimeZone tz = TimeZone.getTimeZone(timeZoneStringID);
        int offset = tz.getRawOffset();
        int diff = Double.valueOf(longitude*4D*60D*1000D).intValue() - offset;
        long diffInMillis = Double.valueOf(diff).longValue();
        lmtDateTimeInMillis = dateTimeInMillis + diffInMillis;
    }
    
    public long getLMT()
    {
        return lmtDateTimeInMillis;
    }

    public int getHoraryNumber()
    {
        return horaryNumber;
    }

    @Override
    public String toString()
    {
        return ViewUtils.toStringDateTime(year, month, dayOfMonth,
                getTime());
    }

    @Override
    public Object clone()
        throws CloneNotSupportedException
    {
        super.clone();
        HoroChartData newbdt = new HoroChartData();
        newbdt.year = this.year;
        newbdt.month = this.month;
        newbdt.dayOfMonth = this.dayOfMonth;
        newbdt.hours = this.hours;
        newbdt.minutes = this.minutes;
        newbdt.seconds = this.seconds;
        newbdt.dateTimeInMillis = this.dateTimeInMillis;
        newbdt.julianDay = this.julianDay;
        newbdt.longitude = this.longitude;
        newbdt.latitude = this.latitude;
        newbdt.horaryNumber = this.horaryNumber;
        
        return newbdt;
    }
}
