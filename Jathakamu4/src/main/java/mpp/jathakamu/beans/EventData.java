/* 
 * Yet to decide on the license
 */
package mpp.jathakamu.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import mpp.jathakamu.GlobalSettings;

import static mpp.jathakamu.JathakamLogger.LOGGER;

import mpp.jathakamu.types.Place;
import mpp.jathakamu.utils.DateTime;
import mpp.jathakamu.utils.ViewUtils;
import mpp.jathakamu.view.Constants;

/**
 *
 * @author phani
 */
@Named(value = "eventData")
@SessionScoped
public class EventData
        implements Serializable
{
    private Place place;
    private Date eventTime = new Date();
    private String name = "";
    private String gender = "Male";

    private final transient Place.PlaceBuilder placeBuilder = new Place.PlaceBuilder();
    private transient int longitudeDeg;
    private transient int longitudeMins;
    private transient int longitudeSecs;
    private transient int latitudeDeg;
    private transient int latitudeMins;
    private transient int latitudeSecs;
    private transient String longitudeDirection;
    private transient String latitudeDirection;

    /**
     * Creates a new instance of EventData
     */
    public EventData()
    {
        LOGGER.log(Level.INFO, "Initializing Event Data");
//        this.place = GlobalSettings.astrologerPlace;
//        placeBuilder.values(place.getName(), place.getLongitude(),
//                place.getLatitude(), place.getTimeZone());
        placeBuilder.values("Hyderabad", 78.5, (17d + 20d/60d), mpp.jathakamu.Constants.DEFAULT_TIME_ZONE);
        this.place = placeBuilder.build();
        longitudeDirection = Constants.LONGITUDE_DIRECTIONS[(place.getLongitude() > 0) ? 0 : 1];
        latitudeDirection = Constants.LATITUDE_DIRECTIONS[(place.getLatitude() > 0) ? 0 : 1];
        int []degParts = ViewUtils.getDegreeParts(place.getLongitude());
        longitudeDeg = degParts[0];
        longitudeMins = degParts[1];
        longitudeSecs = degParts[2];
        degParts = ViewUtils.getDegreeParts(place.getLatitude());
        latitudeDeg = degParts[0];
        latitudeMins = degParts[1];
        latitudeSecs = degParts[2];
        
        Calendar cal = Calendar.getInstance();
        cal.set(1973, 3, 4, 8, 59, 8);
        cal.set(Calendar.MILLISECOND, 0);
        setEventTime(cal.getTime());
    }

    public Date getEventTime()
    {
        return eventTime;
    }

    public void setEventTime(Date eventTime)
    {
        DateTime dateTime = new DateTime(eventTime, place.getTimeZone());

        this.eventTime = dateTime.getDateTime();
    }
    
    public double getViewLongitude()
    {
        return Math.abs(place.getLongitude());
    }

    public void setViewLongitude(double longitude)
    {
        longitude = Math.abs(longitude);
        int []degParts = ViewUtils.getDegreeParts(longitude);
        longitudeDeg = degParts[0];
        longitudeMins = degParts[1];
        longitudeSecs = degParts[2];
        
        if (longitudeDirection.equalsIgnoreCase(Constants.LONGITUDE_DIRECTIONS[1]))
        {
            longitude = -1 * longitude;
        }
        
        placeBuilder.longitude(longitude);
        place = placeBuilder.build();
    }
    
    public double getLongitude()
    {
        return place.getLongitude();
    }

    public double getLatitude()
    {
        return place.getLatitude();
    }

    public double getViewLatitude()
    {
        return Math.abs(place.getLatitude());
    }

    public void setViewLatitude(double latitude)
    {
        latitude = Math.abs(latitude);
        int []degParts = ViewUtils.getDegreeParts(latitude);
        latitudeDeg = degParts[0];
        latitudeMins = degParts[1];
        latitudeSecs = degParts[2];
        
        if (latitudeDirection.equalsIgnoreCase(Constants.LATITUDE_DIRECTIONS[1]))
        {
            latitude = -1 * latitude;
        }

        placeBuilder.latitude(latitude);
        place = placeBuilder.build();
    }

    public String getTimeZone()
    {
        return place.getTimeZone();
    }

    public void setTimeZone(String timeZone)
    {
        DateTime dateTime = new DateTime(eventTime, timeZone);
        this.eventTime = dateTime.getDateTime();
        
        placeBuilder.timeZone(timeZone);
        place = placeBuilder.build();
    }
    
    public List<String> getAutoCompleteTimeZones(String input)
    {
        List<String> availableTZs = getAvailableTimeZones();
        if (input == null || input.trim().length() == 0)
        {
            return availableTZs;
        }
        
        List<String> newList = availableTZs.stream()
                .filter(name -> name.toLowerCase().contains(input.toLowerCase()))
                .collect(Collectors.toList());
        
        return newList;
    }
    
    public List<String> getAvailableTimeZones()
    {
        Set<String> tzs = Constants.getTimeZones();
        
        return new ArrayList<>(tzs);
    }
    
    public Place getPlace()
    {
        return place;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }
    
    public String[] getGenders()
    {
        String []rtnGenders = new String[3];
        System.arraycopy(Constants.GENDERS, 0, rtnGenders, 0, Constants.GENDERS.length);

        return rtnGenders;
    }
    
    public int getLongitudeDeg()
    {
        return Math.abs(longitudeDeg);
    }

    public void setLongitudeDeg(int longitudeDeg)
    {
        this.longitudeDeg = longitudeDeg;
        double l = ViewUtils.getDegreeAsDouble(longitudeDeg, longitudeMins, longitudeSecs);
        setViewLongitude(l);
    }

    public int getLongitudeMins()
    {
        return Math.abs(longitudeMins);
    }

    public void setLongitudeMins(int longitudeMins)
    {
        this.longitudeMins = longitudeMins;
        double l = ViewUtils.getDegreeAsDouble(longitudeDeg, longitudeMins, longitudeSecs);
        setViewLongitude(l);
    }

    public int getLongitudeSecs()
    {
        return Math.abs(longitudeSecs);
    }

    public void setLongitudeSecs(int longitudeSecs)
    {
        this.longitudeSecs = longitudeSecs;
        double l = ViewUtils.getDegreeAsDouble(longitudeDeg, longitudeMins, longitudeSecs);
        setViewLongitude(l);
    }

    public int getLatitudeDeg()
    {
        return Math.abs(latitudeDeg);
    }

    public void setLatitudeDeg(int latitudeDeg)
    {
        this.latitudeDeg = latitudeDeg;
        double l = ViewUtils.getDegreeAsDouble(latitudeDeg, latitudeMins, latitudeSecs);
        setViewLatitude(l);
    }

    public int getLatitudeMins()
    {
        return Math.abs(latitudeMins);
    }

    public void setLatitudeMins(int latitudeMins)
    {
        this.latitudeMins = latitudeMins;
        double l = ViewUtils.getDegreeAsDouble(latitudeDeg, latitudeMins, latitudeSecs);
        setViewLatitude(l);
    }

    public int getLatitudeSecs()
    {
        return Math.abs(latitudeSecs);
    }

    public void setLatitudeSecs(int latitudeSecs)
    {
        this.latitudeSecs = latitudeSecs;
        double l = ViewUtils.getDegreeAsDouble(latitudeDeg, latitudeMins, latitudeSecs);
        setViewLatitude(l);
    }
    
    public String getLongitudeDirection()
    {
        return longitudeDirection;
    }
    
    public String[] getLongitudeDirectons()
    {
        return Constants.LONGITUDE_DIRECTIONS;
    }
    
    public void setLongitudeDirection(String direction)
    {
        longitudeDirection = direction;
        double longitude = getLongitude();
        
        if (direction.equalsIgnoreCase(Constants.LONGITUDE_DIRECTIONS[1]))
        {
            longitude = -1 * longitude;
        }
        else
        {
            longitude = Math.abs(longitude);
        }
        
        placeBuilder.longitude(longitude);
        place = placeBuilder.build();
    }
    
    public String getLatitudeDirection()
    {
        if (getLongitude() > 0)
            return Constants.LATITUDE_DIRECTIONS[0];
        else
            return Constants.LATITUDE_DIRECTIONS[1];
    }
    
    public String[] getLatitudeDirectons()
    {
        return Constants.LATITUDE_DIRECTIONS;
    }
    
    public void setLatitudeDirection(String direction)
    {
        latitudeDirection = direction;
        double latitude = getLatitude();
        
        if (direction.equalsIgnoreCase(Constants.LATITUDE_DIRECTIONS[1]))
        {
            latitude = -1 * latitude;
        }
        else
        {
            latitude = Math.abs(latitude);
        }
        
        placeBuilder.latitude(latitude);
        place = placeBuilder.build();
    }

}
