/* 
 * Yet to decide on the license
 */
package mpp.jathakamu.beans;

import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import mpp.jathakamu.Constants;
import mpp.jathakamu.GlobalSettings;
import mpp.jathakamu.JathakamuException;
import mpp.jathakamu.Profile;
import mpp.jathakamu.ProfileSettings;
import mpp.jathakamu.calculations.EphemerisCalcs;
import mpp.jathakamu.calculations.SupportCalcs;
import mpp.jathakamu.types.HouseEntity;
import mpp.jathakamu.types.Place;
import mpp.jathakamu.types.Planet;
import mpp.jathakamu.utils.ViewUtils;
import swisseph.SweConst;
import swisseph.SweDate;

import static mpp.jathakamu.JathakamLogger.LOGGER;

/**
 *
 * @author phani
 */
@Named(value = "transitChart")
@SessionScoped
public class TransitChart
        extends DefaultHouseInfo
{

    private final Place place = GlobalSettings.astrologerPlace;
    private long trasitTime;
    private String ayanamsa;
    private String sidrealTime;

    /**
     * Creates a new instance of TransitChart
     */
    public TransitChart()
    {
        super();
    }

    /**
     * Call this method to generate new transit
     */
    @PostConstruct
    public void generateNewTransit()
    {
        LOGGER.log(Level.INFO, "Called generateNewTransit");
        trasitTime = System.currentTimeMillis();
        try
        {
            initializeHouseEntity();
            clearInformation();
            addInformation("Date & Time : " + ViewUtils.getDateAsStringLongFormat(trasitTime));
            addInformation("Longitude   : " + ViewUtils.toStringDegree3(
                    place.getLongitude()));
            addInformation("Latitude    : " + ViewUtils.toStringDegree3(
                    place.getLatitude()));
            addInformation("");
            double tjd_ut = SupportCalcs.getJulianDay(trasitTime, place);
            System.out.println("jdt_ut = " + tjd_ut
                    + ", Again coverting it back to date = "
                    + SweDate.getDate(tjd_ut));

            ProfileSettings profileSettings = new ProfileSettings();
            profileSettings.setKPNewAynamsa();
            profileSettings.setUseGeocentric(false);

            Profile profile = new Profile(tjd_ut, place, profileSettings);

            EphemerisCalcs ephCalc = new EphemerisCalcs(profile);
            ayanamsa = ViewUtils.toStringDegree3(ephCalc.getAyanamsa());
            double[] cusps = ephCalc.getCuspDetails();
            sidrealTime = ViewUtils.toStringDegree3(ephCalc.getSidrealTime());

            addInformation("Ayanamsa: " + ayanamsa);
            addInformation("Sidreal Time: " + sidrealTime);

            int i = 0;
            for (double cusp : cusps)
            {
                List<Planet> lordsList = SupportCalcs.getLordsWithSignLord(cusp);
                String value = getLordsString(lordsList);
                Constants.CUSP_AND_PLANETS obj = Constants.CUSP_AND_PLANETS.values()[i++];
                setHouseEntity(obj, cusp, "Lords: " + value);
            }

            for (i = SweConst.SE_SUN; i <= SweConst.SE_PLUTO; i++)
            {
                double[] planetInfo = ephCalc.getPlanetDetails(i);
                List<Planet> lordsList = SupportCalcs.getLordsWithSignLord(
                        planetInfo[0]);
                double deg = planetInfo[0];

                String value = getLordsString(lordsList);

                Constants.CUSP_AND_PLANETS obj = Constants.CUSP_AND_PLANETS.values()[12 + i];
                setHouseEntity(obj, planetInfo[0], "Lords: " + value);
            }

            // Rahu
            int rahu = (profileSettings.isTrueNode()) ? SweConst.SE_TRUE_NODE : SweConst.SE_MEAN_NODE;
            double[] planetInfo = ephCalc.getPlanetDetails(rahu);
            double rahuPosition = planetInfo[0];
            List<Planet> lordsList = SupportCalcs.getLordsWithSignLord(
                    rahuPosition);
            String value = getLordsString(lordsList);
            Constants.CUSP_AND_PLANETS obj = Constants.CUSP_AND_PLANETS.RAHU;
            setHouseEntity(obj, planetInfo[0], value);

            // Ketu
            double ketuPosition = SupportCalcs.degnorm(180 + rahuPosition);
            lordsList = SupportCalcs.getLordsWithSignLord(ketuPosition);
            value = getLordsString(lordsList);
            obj = Constants.CUSP_AND_PLANETS.KETU;
            setHouseEntity(obj, ketuPosition, value);

            sortHouseEntities();
        }
        catch (JathakamuException ex)
        {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public HouseEntity[] getHouseInfo(int housePosition)
    {
        List<HouseEntity> rtnValue = houseEntities.get(housePosition);
        HouseEntity[] hes = new HouseEntity[rtnValue.size()];

        rtnValue.toArray(hes);
        return hes;
    }
    
    public void reloadChart(ActionEvent event)
    {
        LOGGER.info("reload Chart called...");
        generateNewTransit();
    }

}
