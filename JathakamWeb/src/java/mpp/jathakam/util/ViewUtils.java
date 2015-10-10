/*
 * This software is provided AS IS without any warranty for any kind of use. Author is not liable for any loss for using
 * this software.
 */
package mpp.jathakam.util;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phani
 */
public class ViewUtils
{

    private final static NumberFormat NUMBER_FORMAT_4_DECIMALS = NumberFormat.getInstance();
    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy");
    private final static DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss:SSSS");

    static
    {
        NUMBER_FORMAT_4_DECIMALS.setMaximumFractionDigits(4);
        DATE_FORMAT.setLenient(false);
        TIME_FORMAT.setLenient(false);
    }
    
    public static String doubleAsString4Decimals(double value) {
        return NUMBER_FORMAT_4_DECIMALS.format(value);
    }
    
    public static String dateAsString(long date) {
        return dateAsString(new Date(date));
    }
    
    public static String dateAsString(Date date) {
        return DATE_FORMAT.format(date);
    }
    
    public static Date dateOnly(String dateOnly) {
        try
        {
            return DATE_FORMAT.parse(dateOnly);
        }
        catch (ParseException ex)
        {
            Logger.getLogger(ViewUtils.class.getName()).log(Level.SEVERE, null, ex);
            return new Date();
        }
    }
    
    public static String timeAsString(long date) {
        return timeAsString(new Date(date));
    }
    
    public static String timeAsString(Date date) {
        return TIME_FORMAT.format(date);
    }
    
    public static Date timeOnly(String timeOnly) {
        try
        {
            return TIME_FORMAT.parse(timeOnly);
        }
        catch (ParseException ex)
        {
            Logger.getLogger(ViewUtils.class.getName()).log(Level.SEVERE, null, ex);
            return new Date();
        }
    }
}
