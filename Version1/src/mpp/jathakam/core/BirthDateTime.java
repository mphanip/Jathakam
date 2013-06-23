/*
 *
 *
 */
package mpp.jathakam.core;

import java.io.Serializable;
import java.util.Calendar;

import mpp.jathakam.core.BirthDateTime;
import mpp.jathakam.utils.JatakamUtilities;

/**
 *
 * @author phani
 */
public class BirthDateTime
    implements Serializable, Cloneable
{

    private static final long serialVersionUID = 1713182587552788433L;

    private int year;
    private int month;
    private int dayOfMonth;
    private int hours;
    private int minutes;
    private double seconds;

    private BirthDateTime()
    {
    }

    public BirthDateTime(int year, int month, int dayOfMonth, int hours, int mins, double secs)
    {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.hours = hours;
        this.minutes = mins;
        this.seconds = secs;
    }

    public BirthDateTime(int year, int month, int dayOfMonth, double time)
    {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;

        double dHrs = time / 24.0;
        hours = Double.valueOf(dHrs).intValue();

        double dMins = (time - (double)hours) / 60.0;
        minutes = Double.valueOf(dMins).intValue();

        seconds = time - dHrs - dMins;
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

    public double getTime()
    {
        return (hours + minutes / 60.0D + seconds / 3600.0D);
    }

    public long getTimeInMillis()
    {
        int sec = Double.valueOf(seconds).intValue();
        double dMillis = (seconds - sec) * 1000.0D;
        int millis = (int) Math.round(dMillis);

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(this.year, this.month - 1, this.dayOfMonth, hours, minutes, sec);
        cal.set(Calendar.MILLISECOND, millis);

        long rtnTime = cal.getTimeInMillis();

        return rtnTime;
    }

    @Override
    public String toString()
    {
        return JatakamUtilities.toStringDateTime(year, month, dayOfMonth,
            hours + minutes/60D + seconds/3600D);
    }

    @Override
    public Object clone()
        throws CloneNotSupportedException
    {
        super.clone();
        BirthDateTime newbdt = new BirthDateTime();
        newbdt.year = this.year;
        newbdt.month = this.month;
        newbdt.dayOfMonth = this.dayOfMonth;
        newbdt.hours = this.hours;
        newbdt.minutes = this.minutes;
        newbdt.seconds = this.seconds;
        return newbdt;
    }
}
