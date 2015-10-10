package mpp.jathakam.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.event.map.StateChangeEvent;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

//@javax.faces.bean.ManagedBean(name = "location")
//@javax.faces.bean.SessionScoped
public class GeoMapModel
        implements Serializable
{

    private MapModel model;
    private String title = "Bangalore";
    private double lat = 12.9667d;
    private double lng =  77.5667d;
    private String center = lat + ", " + lng;
    private int zoom = 10;

    private List<LatLng> coordinates;

    public List<LatLng> getCoordinates()
    {
        return coordinates;
    }

    public void setCoordinates(List<LatLng> coordinates)
    {
        this.coordinates = coordinates;
    }

    public GeoMapModel()
    {
        model = new DefaultMapModel();
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public double getLat()
    {
        return lat;
    }

    public void setLat(double lat)
    {
        this.lat = lat;
    }

    public double getLng()
    {
        return lng;
    }

    public void setLng(double lng)
    {
        this.lng = lng;
    }

    public MapModel getModel()
    {
        return model;
    }

    public void setModel(MapModel model)
    {
        this.model = model;
    }

    public String getCenter()
    {
        return center;
    }

    public void setCenter(String center)
    {
        this.center = center;
    }

    public int getZoom()
    {
        return zoom;
    }

    public void setZoom(int zoom)
    {
        this.zoom = zoom;
    }
    
    public void onPointSelect(PointSelectEvent event) {
        LatLng latlng = event.getLatLng();
        lat = latlng.getLat();
        lng = latlng.getLng();
        center = lat + ", " + lng;
        System.out.println("Center = " + center);
        List<Marker> markersList = model.getMarkers();
        markersList.clear();
        markersList.add(new Marker(event.getLatLng(), "Birth Place"));
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

    public void populateTable(List<LatLng> lista)
    {
        lista.add(new LatLng(getLat(), getLng()));
    }

    public void addMarker(ActionEvent actionEvent)
    {
        Marker marker = new Marker(new LatLng(lat, lng), title);
        coordinates = new ArrayList<>();
        model.addOverlay(marker);

        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Added", "Lat:" + lat + ", Lng:" + lng));

        populateTable(coordinates);
    }

    public void addMessage(FacesMessage message)
    {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
