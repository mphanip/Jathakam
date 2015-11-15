/*
 * This software is provided AS IS without any warranty for any kind of use. Author is not liable for any loss for using
 * this software.
 */
package mpp.jathakam.model;

import java.util.*;

import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;

import mpp.jathakam.services.Defaults;
import mpp.jathakam.services.JathakamuException;
import mpp.jathakam.services.Profile;
import mpp.jathakam.services.ProfileBuilder;
import mpp.jathakam.services.ProfileSettings;
import mpp.jathakam.services.calculations.EphemerisCalcs;
import mpp.jathakam.services.calculations.SupportCalcs;
import mpp.jathakam.services.types.Place;
import mpp.jathakam.services.types.Raasi;

/**
 *
 * @author phani
 */
public class NatalChartInfo
{
    static final Logger LOGGER = Logger.getLogger("mpp.jathakam");
    private final static Gson GSON = new Gson();
    
    private transient Calendar now = new Calendar.Builder().setInstant(new Date()).build();
    
    private String name = "Untitled chart";
    private int day = now.get(Calendar.DAY_OF_MONTH);
    private int month = now.get(Calendar.MONTH);
    private int year = now.get(Calendar.YEAR);
    private int hours = now.get(Calendar.HOUR_OF_DAY);
    private int minutes = now.get(Calendar.MINUTE);
    private int seconds = now.get(Calendar.SECOND);
    private int millis = now.get(Calendar.MILLISECOND);
    private String timezone = Defaults.DEFAULT_TIME_ZONE; // IST
    private double longitude = Defaults.DEFAULT_PLACE.getLongitude();
    private double latitude = Defaults.DEFAULT_PLACE.getLatitude();
    private String placeName = Defaults.DEFAULT_PLACE.getName();
    private ZodiacInfoContainer []zodiacInfo = new ZodiacInfoContainer[12];

    public NatalChartInfo()
    {
        super();
        
        for (int i = 0; i < 12; i++) {
            zodiacInfo[i] = new ZodiacInfoContainer();
        }
    }

    public NatalChartInfo(String name, int day, int month, int year, int hours, int minutes, int seconds, int millis,
            String timezone, double longitude, double latitude, String placeName)
    {
        this.name = name;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.millis = millis;
        this.timezone = timezone;
        this.longitude = longitude;
        this.latitude = latitude;
        this.placeName = placeName;
    }
    
    public NatalChartInfo(NatalChartInfo old) {
        this.name = old.name;
        this.day = old.day;
        this.month = old.month;
        this.year = old.year;
        this.hours = old.hours;
        this.minutes = old.minutes;
        this.seconds = old.seconds;
        this.millis = old.millis;
        this.timezone = old.timezone;
        this.longitude = old.longitude;
        this.latitude = old.latitude;
        this.placeName = old.placeName;
    }
    
    public NatalChartInfo(String jsonStr) {
        setFromJson(jsonStr);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getDay()
    {
        return day;
    }

    public void setDay(int day)
    {
        this.day = day;
    }

    public int getMonth()
    {
        return month;
    }

    public void setMonth(int month)
    {
        this.month = month;
    }

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public int getHours()
    {
        return hours;
    }

    public void setHours(int hours)
    {
        this.hours = hours;
    }

    public int getMinutes()
    {
        return minutes;
    }

    public void setMinutes(int minutes)
    {
        this.minutes = minutes;
    }

    public int getSeconds()
    {
        return seconds;
    }

    public void setSeconds(int seconds)
    {
        this.seconds = seconds;
    }

    public int getMillis()
    {
        return millis;
    }

    public void setMillis(int millis)
    {
        this.millis = millis;
    }

    public String getTimezoneOffsetInMinutes()
    {
        return timezone;
    }

    public void setTimezoneOffsetInMinutes(String timezone)
    {
        this.timezone = timezone;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public String getPlaceName()
    {
        return placeName;
    }

    public void setPlaceName(String placeName)
    {
        this.placeName = placeName;
    }
    
    public String toJSON() {
        return GSON.toJson(this);
    }

    public final void setFromJson(String jsonStr) {
        NatalChartInfo newProfile = GSON.fromJson(jsonStr, NatalChartInfo.class);
        this.name = newProfile.name;
        this.day = newProfile.day;
        this.month = newProfile.month;
        this.year = newProfile.year;
        this.hours = newProfile.hours;
        this.minutes = newProfile.minutes;
        this.seconds = newProfile.seconds;
        this.millis = newProfile.millis;
        this.timezone = newProfile.timezone;
        this.longitude = newProfile.longitude;
        this.latitude = newProfile.latitude;
        this.placeName = newProfile.placeName;
    }
    
    public static NatalChartInfo fromJSON(String jsonStr) {
        return new NatalChartInfo(jsonStr);
    }
    
    public void calculate() {
        System.out.println("calculate called");
        //clear existing zodiac info
        for (ZodiacInfoContainer zi: zodiacInfo) {
            zi.clear();
        }
        
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, day, hours, minutes, seconds);
        cal.set(Calendar.MILLISECOND, millis);
        Place place = new Place.PlaceBuilder().latitude(latitude).longitude(longitude).build();
        double tjd_ut = 0;
        
        try
        {
            tjd_ut = SupportCalcs.getJulianDay(cal, place);
        }
        catch (JathakamuException ex)
        {
            Logger.getLogger(NatalChartInfo.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        
        ProfileBuilder pb = new ProfileBuilder();
        Profile profile = pb.tjd_ut(tjd_ut).settings(ProfileSettings.DEFAULT).place(place).build();
        EphemerisCalcs ephCalc = new EphemerisCalcs(profile);
        
        for (int i = 0; i < 10; i++) {
            try
            {
                double[] planetDetails = ephCalc.getPlanetDetails(i);
                // Negative longitude for retrograde planets
                ZodiacInfo zi = new ZodiacInfo(i, planetDetails[0] * (planetDetails[1] < 0 ? -1 : 1));
                addZodiacInfo(zi);
            }
            catch (JathakamuException ex)
            {
                Logger.getLogger(NatalChartInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try
        {
            double[] cuspDetails = ephCalc.getCuspDetails();
            int cuspIndex = 20;
             
            for (double cusp: cuspDetails) {
                ZodiacInfo zi = new ZodiacInfo(cuspIndex++, cusp);
                addZodiacInfo(zi);
            }
        }
        catch (JathakamuException ex)
        {
            Logger.getLogger(NatalChartInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ZodiacInfoContainer[] getZodiac() {
        return zodiacInfo;
    }
    
    private void addZodiacInfo(ZodiacInfo info) {
        Raasi zodiacSign = Raasi.getRaasi(Math.abs(info.getPosition()));
        LOGGER.log(Level.INFO, "Zodiac Sign for longitude {0} is {1}", new Object[] {info.getPosition(), zodiacSign.toString()});
        int raasiIndex = zodiacSign.ordinal();
        zodiacInfo[raasiIndex > 0 ? raasiIndex - 1: 0].add(info);
    }
}
 