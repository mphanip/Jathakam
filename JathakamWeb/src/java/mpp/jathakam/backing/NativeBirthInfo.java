/*
 * This software is provided AS IS without any warranty for any kind of use. Author is not liable for any loss for using
 * this software.
 */
package mpp.jathakam.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.faces.model.SelectItem;

import mpp.jathakam.util.ViewUtils;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author phani
 */
@javax.faces.bean.ManagedBean(name = "birthInfo")
@javax.faces.bean.SessionScoped
public class NativeBirthInfo
    implements Serializable
{
    private String name = "New name";
    private String place = "Bangalore";
    private double longitude = 77.5667;
    private double latitude = 12.9667;
    @SuppressWarnings("FieldMayBeFinal")
    private Calendar datetime = Calendar.getInstance(TimeZone.getDefault());
    private List<SelectItem> availableTimeZones;
    private MapModel model;
    private int zoom = 10; // map zoom
    
    public NativeBirthInfo() {
        super();
        model = new DefaultMapModel();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPlace()
    {
        return place;
    }

    public void setPlace(String place)
    {
        this.place = place;
    }

    public double getLongitude()
    {
        return longitude;
    }
    
    public String getLongitudeAsString() {
        return degreeToString(longitude, false);
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }
    
    public String getLatitudeAsString() {
        return degreeToString(latitude, true);
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public Date getDatetime()
    {
        return datetime.getTime();
    }

    public void setDatetime(Date datetime)
    {
        datetime.setTime(datetime.getTime());
    }
    
    public List<SelectItem> getAvailableTimeZones() {
        if (availableTimeZones != null && availableTimeZones.size() > 0) {
            return availableTimeZones;
        }
        
        String[] tzs = TimeZone.getAvailableIDs();
        
        availableTimeZones = new ArrayList<>();
        
        for (String tz: tzs) {
            SelectItem si = new SelectItem(tz, tz);
            availableTimeZones.add(si);
        }
        
        return availableTimeZones;
    }

    public String getTimezone()
    {
        return datetime.getTimeZone().getID();
    }

    public void setTimezone(String tzId)
    {
        TimeZone tz = TimeZone.getTimeZone(tzId);
        datetime.setTimeZone(tz);
    }
    
    public void setDate(String date) {
        Date newDate = ViewUtils.dateOnly(date);
        Calendar newCal = Calendar.getInstance(datetime.getTimeZone());
        newCal.setTime(newDate);
        
        datetime.set(Calendar.YEAR, newCal.get(Calendar.YEAR));
        datetime.set(Calendar.MONTH, newCal.get(Calendar.MONTH));
        datetime.set(Calendar.DAY_OF_MONTH, newCal.get(Calendar.DAY_OF_MONTH));
    }
    
    public String getDate() {
        return ViewUtils.dateAsString(datetime.getTimeInMillis());
    }
    
    public String getTime() {
        return ViewUtils.timeAsString(datetime.getTimeInMillis());
    }
    
    public void setTime(String time) {
        Date newTime = ViewUtils.timeOnly(time);
        Calendar newCal = Calendar.getInstance(datetime.getTimeZone());
        newCal.setTime(newTime);
        
        datetime.set(Calendar.HOUR_OF_DAY, newCal.get(Calendar.HOUR_OF_DAY));
        datetime.set(Calendar.MINUTE, newCal.get(Calendar.MINUTE));
        datetime.set(Calendar.SECOND, newCal.get(Calendar.SECOND));
        datetime.set(Calendar.MILLISECOND, newCal.get(Calendar.MILLISECOND));
    }
    
    public int getHours() {
        return datetime.get(Calendar.HOUR_OF_DAY);
    }
    
    public void setHours(int hours) {
        datetime.set(Calendar.HOUR_OF_DAY, hours);
    }
    
    public int getMinutes() {
        return datetime.get(Calendar.MINUTE);
    }
    
    public void setMinutes(int minutes) {
        datetime.set(Calendar.MINUTE, minutes);
    }
    
    public double getSeconds() {
        int secs = datetime.get(Calendar.SECOND);
        int ms = datetime.get(Calendar.MILLISECOND);
        return secs + ms/1000d;
    }
    
    public void setSeconds(double seconds) {
//        int millisecs = Double.valueOf(seconds*1000).intValue();
//        int secs = millisecs % 1000;
//        int millis = millisecs - secs*1000;
        datetime.set(Calendar.MILLISECOND, Double.valueOf(seconds*1000).intValue());
    }

    public MapModel getModel()
    {
        return model;
    }

    public void setModel(MapModel model)
    {
        this.model = model;
    }

    public int getZoom()
    {
        return zoom;
    }

    public void setZoom(int zoom)
    {
        this.zoom = zoom;
    }
    
    public String getCenter()
    {
        return latitude + ", " + longitude;
    }
    
    // Events
    public void onPointSelect(PointSelectEvent event) {
        System.out.println("onPointSelect occured: " + event);
        LatLng latlng = event.getLatLng();
        latitude = latlng.getLat();
        longitude = latlng.getLng();
        List<Marker> markersList = model.getMarkers();
        markersList.clear();
        Marker birthMarker = new Marker(event.getLatLng(), "Birth Place");
        markersList.add(birthMarker);
    }
    
    public void onStateChange(StateChangeEvent event) {
        zoom = event.getZoomLevel();
        System.out.println("Zoom = " + zoom);
//        LatLng latlng = event.getCenter();
//        lat = latlng.getLat();
//        lng = latlng.getLng();
//        center = lat + ", " + lng;
//        System.out.println("Center = " + center);
    }
    
    private String degreeToString(double degree, boolean latitude) {
        StringBuilder sb = new StringBuilder();
        int deg = Double.valueOf(degree).intValue();
        double minFrac = (degree - deg) * 60d;
        int min = Double.valueOf(minFrac).intValue();
        double secFrac = minFrac - min;
        
        sb.append(deg).append(degree < 0 ? ((latitude) ? 'S': 'W') : (latitude) ? 'N': 'E').append(min);
        
        if (secFrac > 0.0001) {
            String sSecs = ViewUtils.doubleAsString4Decimals(secFrac);
            sb.append('-').append(sSecs);
        }
        
        return sb.toString();
    }
}
