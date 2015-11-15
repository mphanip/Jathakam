/*
 * This software is provided AS IS without any warranty for any kind of use. Author is not liable for any loss for using
 * this software.
 */
package mpp.jathakam;

import java.util.Calendar;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import mpp.jathakam.model.NatalChartInfo;
import mpp.jathakam.services.Defaults;

/**
 * REST Web Service
 *
 * @author phani
 */
@Path("jathakamService")
public class JathakamService
{

    @Context
    private UriInfo context;

    private NatalChartInfo chartInfo = new NatalChartInfo();

    /**
     * Creates a new instance of JathakamService
     */
    public JathakamService()
    {
        System.out.println("Initializing JathakamService");
        Calendar cal = Calendar.getInstance();
        double newLongitude = Defaults.DEFAULT_PLACE.getLongitude(),
                newLatitude = Defaults.DEFAULT_PLACE.getLatitude();
        int newDay = cal.get(Calendar.DAY_OF_MONTH);
        int newMonth = cal.get(Calendar.MONTH)+1;
        int newYear = cal.get(Calendar.YEAR);
        int newHours = cal.get(Calendar.HOUR_OF_DAY);
        int newMins = cal.get(Calendar.MINUTE);
        int newSecs = cal.get(Calendar.SECOND);
        int newMillis = cal.get(Calendar.MILLISECOND);
        String newName = "Untitled";
        String newPlaceName = Defaults.DEFAULT_PLACE.getName();
        String newTimezone = Defaults.DEFAULT_PLACE.getTimeZone();

        chartInfo.setLongitude(newLongitude);
        chartInfo.setLatitude(newLatitude);
        chartInfo.setDay(newDay);
        chartInfo.setMonth(newMonth);
        chartInfo.setYear(newYear);
        chartInfo.setHours(newHours);
        chartInfo.setMinutes(newMins);
        chartInfo.setSeconds(newSecs);
        chartInfo.setMillis(newMillis);
        chartInfo.setTimezoneOffsetInMinutes(newTimezone);
        chartInfo.setPlaceName(newPlaceName);
        chartInfo.setName(newName);

        chartInfo.calculate();
    }

    /**
     * Retrieves representation of an instance of mpp.jathakam.JathakamService
     *
     * @param name
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson()
    {
        String jsonChart = chartInfo.toJSON();
        System.out.println("jsonChart = " + jsonChart);
        
        return jsonChart;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String postJson(String content)
    {
        System.out.println("Post method called " + content);
        this.chartInfo.setFromJson(content);
        chartInfo.calculate();
        return getJson();
    }

    /**
     * PUT method for updating or creating an instance of JathakamService
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content)
    {
        System.out.println("Post method called " + content);
    }

}
