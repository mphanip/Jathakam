/* 
 * Yet to decide on the license
 */

package mpp.jathakamu.view.types;

import java.io.Serializable;
import mpp.jathakamu.GlobalSettings;
import mpp.jathakamu.types.Place;

/**
 *
 * @author phani
 */
public class BasicInfo
        implements Serializable
{
    private String name = "Transit";
    private long eventTime = System.currentTimeMillis();
    private Place place = GlobalSettings.astrologerPlace;

    /**
     * Creates a new instance of BasicInfo
     */
    public BasicInfo()
    {
    }

    public long getEventTime()
    {
        return eventTime;
    }

    public void setEventTime(long eventTime)
    {
        this.eventTime = eventTime;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Place getPlace()
    {
        return place;
    }

    public void setPlace(Place place)
    {
        this.place = place;
    }
}
