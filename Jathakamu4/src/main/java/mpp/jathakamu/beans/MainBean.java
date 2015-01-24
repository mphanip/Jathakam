/* 
 * Yet to decide on the license
 */
package mpp.jathakamu.beans;

import mpp.jathakamu.view.types.LordsInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import mpp.jathakamu.GlobalSettings;

import static mpp.jathakamu.Constants.CUSP_AND_PLANETS;
import mpp.jathakamu.JathakamuException;
import mpp.jathakamu.Profile;
import mpp.jathakamu.ProfileSettings;
import mpp.jathakamu.calculations.EphemerisCalcs;
import mpp.jathakamu.calculations.SupportCalcs;
import mpp.jathakamu.types.Place;
import mpp.jathakamu.types.Planet;
import mpp.jathakamu.types.VDNode;
import mpp.jathakamu.utils.ViewUtils;
import swisseph.SweConst;
import swisseph.SweDate;

import static mpp.jathakamu.JathakamLogger.LOGGER;

/**
 *
 * @author Phani
 */
@Named(value = "mainBean")
@SessionScoped
public class MainBean
        extends DefaultHouseInfo
{
    private Date eventTime = new Date();
    private String timeZone = GlobalSettings.DEFAULT_TIMEZONE;
    
    @Inject
    private EventData eventData;
    
    private final List<LordsInfo> cuspDetails = new ArrayList<>();
    private final List<LordsInfo> planetDetails = new ArrayList<>();
    
    private String ayanamsa;
    private String sidrealTime;
    private String mc;
    
    private Profile profile;
    
    private List<VDNode> mahaDasaList;
    private VDNode selectedMahaDasa;
    private String selectedMahaDasaString;
    private VDNode selectedBhukthi;
    private String selectedBhukthiString;
    private VDNode selectedPratyantara;
    private String selectedPratyantaraString;
    private VDNode selectedSookshma;
    private String selectedSookshmaString;
    
    public MainBean()
    {
        super();
        if (eventData != null)
        {
            eventTime = eventData.getEventTime();
        }
        else
        {
            eventTime = new Date();
        }
//        initialize();
        System.out.println("**************Creating instance of mainBean : " + eventTime);
    }

    public Date getEventTime()
    {
        return eventTime;
    }

    public void setEventTime(Date eventTime)
    {
        this.eventTime = eventTime;
    }

    public String getTimeZone()
    {
        return timeZone;
    }

    public void setTimeZone(String timeZone)
    {
        this.timeZone = timeZone;
    }

    public String getAyanamsa()
    {
        return ayanamsa;
    }

    public void setAyanamsa(String ayanamsa)
    {
        this.ayanamsa = ayanamsa;
    }

    public String getSidrealTime()
    {
        return sidrealTime;
    }

    public void setSidrealTime(String sidrealTime)
    {
        this.sidrealTime = sidrealTime;
    }

    public String getMc()
    {
        return mc;
    }

    public void setMc(String mc)
    {
        this.mc = mc;
    }

    public String getSelectedMahaDasaString()
    {
        return selectedMahaDasaString;
    }

    public void setSelectedMahaDasaString(String selectedMahaDasaString)
    {
        this.selectedMahaDasaString = selectedMahaDasaString;
    }

    public String getSelectedBhukthiString()
    {
        return selectedBhukthiString;
    }

    public void setSelectedBhukthiString(String selectedBhukthiString)
    {
        this.selectedBhukthiString = selectedBhukthiString;
    }

    public String getSelectedPratyantaraString()
    {
        return selectedPratyantaraString;
    }

    public void setSelectedPratyantaraString(String selectedPratyantaraString)
    {
        this.selectedPratyantaraString = selectedPratyantaraString;
    }

    public String getSelectedSookshmaString()
    {
        return selectedSookshmaString;
    }

    public void setSelectedSookshmaString(String selectedSookshmaString)
    {
        this.selectedSookshmaString = selectedSookshmaString;
    }
    
    @PostConstruct
    public void submit2()
    {
        try
        {
            if (eventData == null)
            {
                return;
            }

            eventTime = eventData.getEventTime();
            timeZone = eventData.getTimeZone();
            clearInformation();
            addInformation(eventData.getName());
            addInformation("Date & Time : " + ViewUtils.getDateAsStringLongFormat(eventTime.getTime()));
            addInformation("Longitude   : " + ViewUtils.toStringDegree3(eventData.getLongitude()));
            addInformation("Latitude    : " + ViewUtils.toStringDegree3(eventData.getLatitude()));
            addInformation("");
            initializeHouseEntity();
            double tjd_ut = SupportCalcs.getJulianDay(eventTime, timeZone,
                    eventData.getLongitude());
            System.out.println("jdt_ut = " + tjd_ut
                    + ", Again coverting it back to date = "
                    + SweDate.getDate(tjd_ut));

            ProfileSettings profileSettings = new ProfileSettings();
            profileSettings.setKPNewAynamsa();
            profileSettings.setUseGeocentric(false);

            Place place = new Place.PlaceBuilder().values("", eventData.getLongitude(),
                    eventData.getLatitude(), timeZone).build();
            
            profile = new Profile(tjd_ut, place, profileSettings);
            
            EphemerisCalcs ephCalc = new EphemerisCalcs(profile);
            ayanamsa = ViewUtils.toStringDegree3(ephCalc.getAyanamsa());
            double[] cusps = ephCalc.getCuspDetails();
            sidrealTime = ViewUtils.toStringDegree3(ephCalc.getSidrealTime());
            mc = ViewUtils.toStringDegree3(ephCalc.getMC());
            
            addInformation("Ayanamsa: " + ayanamsa);
            addInformation("Sidreal Time: " + sidrealTime);

            cuspDetails.clear();
            int i = 0;
            for (double cusp : cusps)
            {
                List<Planet> lordsList = SupportCalcs.getLordsWithSignLord(cusp);
                cuspDetails.add(new LordsInfo(i+1, cusp, lordsList));
                String value = getLordsString(lordsList);
                CUSP_AND_PLANETS obj = CUSP_AND_PLANETS.values()[i++];
                setHouseEntity(obj, cusp, value);
            }

            planetDetails.clear();
            for (i = SweConst.SE_SUN; i <= SweConst.SE_PLUTO; i++)
            {
                double[] planetInfo = ephCalc.getPlanetDetails(i);
                List<Planet> lordsList = SupportCalcs.getLordsWithSignLord(planetInfo[0]);
                String planetName = ephCalc.getPlanetName(i);
                double deg = planetInfo[0];
                planetDetails.add(new LordsInfo(planetName, deg, lordsList));
                
                String value = getLordsString(lordsList);
                
                CUSP_AND_PLANETS obj = CUSP_AND_PLANETS.values()[12+i];
                setHouseEntity(obj, planetInfo[0], value);
            }
            
            // Rahu
            int rahu = (profileSettings.isTrueNode()) ? SweConst.SE_TRUE_NODE : SweConst.SE_MEAN_NODE;
            double[] planetInfo = ephCalc.getPlanetDetails(rahu);
            double rahuPosition = planetInfo[0];
            List<Planet> lordsList = SupportCalcs.getLordsWithSignLord(rahuPosition);
            planetDetails.add(new LordsInfo("Rahu", planetInfo[0], lordsList));
            String value = getLordsString(lordsList);
            CUSP_AND_PLANETS obj = CUSP_AND_PLANETS.RAHU;
            setHouseEntity(obj, planetInfo[0], value);
            
            // Ketu
            double ketuPosition = SupportCalcs.degnorm(180+rahuPosition);
            lordsList = SupportCalcs.getLordsWithSignLord(ketuPosition);
            planetDetails.add(new LordsInfo("Ketu", ketuPosition, lordsList));
            value = getLordsString(lordsList);
            obj = CUSP_AND_PLANETS.KETU;
            setHouseEntity(obj, ketuPosition, value);

            sortHouseEntities();
        }
        catch (JathakamuException ex)
        {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        mahaDasaList = null;
        selectedMahaDasa = null;
        selectedBhukthi = null;
        selectedPratyantara = null;
        selectedSookshma = null;
    }
    
    public void submit() {
        
    }

    public List<LordsInfo> getCuspDetails()
    {
        return cuspDetails;
    }

    public List<LordsInfo> getPlanetDetails()
    {
        return planetDetails;
    }
    
    public List<VDNode> getMahaDasaList()
    {
        if (mahaDasaList == null)
        {
            try {
                long dob = SweDate.getDate(profile.getDateTime()).getTime();
                EphemerisCalcs ephCalc = new EphemerisCalcs(profile);
                double[] moonInfo = ephCalc.getPlanetDetails(SweConst.SE_MOON);
                VDNode vdRoot = SupportCalcs.getVDTree2(dob, moonInfo[0], profile);
                mahaDasaList = vdRoot.getChildren();
                System.out.println("#### VD List: " + mahaDasaList + " for moon position: " + moonInfo[0]);
            }
            catch (JathakamuException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }

        return mahaDasaList;
    }
    
    public void onMahaDasa()
    {
        System.out.println("Mahadasa selected 2 = " + selectedMahaDasa + " / " + selectedMahaDasaString);
        
        Optional<VDNode> ovd = mahaDasaList.stream()
                .filter(node -> node.toString().equals(selectedMahaDasaString))
                .findFirst()
                ;
                
        if (ovd.isPresent())
        {
            selectedMahaDasa = ovd.get();
            System.out.println("Select MahaDasa = " + selectedMahaDasa);
            
            selectedBhukthi = null;
            selectedPratyantara = null;
            selectedSookshma = null;
        }
    }
    
    public List<VDNode> getBhukthiList()
    {
        if (selectedMahaDasa != null)
        {
            return selectedMahaDasa.getChildren();
        }
        
        return Collections.emptyList();
    }
    
    public void onBhukthi()
    {
        System.out.println("Selected Bhukhi = " + selectedBhukthiString);
        
        Optional<VDNode> ovd = getBhukthiList().stream()
                .filter(node -> node.toString().equals(selectedBhukthiString))
                .findFirst();
        
        if (ovd.isPresent())
        {
            selectedBhukthi = ovd.get();
            selectedPratyantara = null;
            selectedSookshma = null;
        }
    }
    
    public List<VDNode> getPratyantaraList()
    {
        if (selectedBhukthi != null)
        {
            return selectedBhukthi.getChildren();
        }
        
        return Collections.emptyList();
    }
    
    public void onPratyantara()
    {
        System.out.println("Selected Pratyantara = " + selectedBhukthiString);
        
        Optional<VDNode> ovd = getPratyantaraList().stream()
                .filter(node -> node.toString().equals(selectedPratyantaraString))
                .findFirst();
        
        if (ovd.isPresent())
        {
            selectedPratyantara = ovd.get();
            selectedSookshma = null;
        }
    }
    
    public List<VDNode> getSookshmaList()
    {
        if (selectedPratyantara != null)
        {
            return selectedPratyantara.getChildren();
        }
        
        return Collections.emptyList();
    }
    
    public void onSookshma()
    {
        System.out.println("Selected Sookshma = " + selectedBhukthiString);
        
        Optional<VDNode> ovd = getSookshmaList().stream()
                .filter(node -> node.toString().equals(selectedSookshmaString))
                .findFirst();
        
        if (ovd.isPresent())
        {
            selectedSookshma = ovd.get();
        }
    }
    
    public List<VDNode> getPranaList()
    {
        if (selectedSookshma != null)
        {
            return selectedSookshma.getChildren();
        }
        
        return Collections.emptyList();
    }
    
//    private void initialize()
//    {
//        submit2();
//    }
    
}
