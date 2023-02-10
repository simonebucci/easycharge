package it.ecteam.easycharge.bean;

import java.io.Serializable;

public class ChargingStationBean implements Serializable {

    private String id;
    private String name;
    private String freeformAddress;
    private double latitude;
    private double longitude;

    public void setId(String id) { this.id = id; }

    public String getId() { return id; }

    public void setName(String name){ this.name = name;}

    public String getName(){ return name; }

    public void setFreeformAddress(String freeformAddress){ this.freeformAddress = freeformAddress; }

    public String getFreeformAddress(){ return freeformAddress; }

    public void setLatitude(double latitude){ this.latitude = latitude; }

    public double getLatitude(){ return latitude; }

    public void setLongitude(double longitude){ this.longitude = longitude; }

    public double getLongitude(){ return longitude; }

}
