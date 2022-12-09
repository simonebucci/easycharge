package it.ecteam.easycharge.entity;

public class ChargingStation {
    private String id;
    private String name;
    private String freeformAddress;
    private double latitude;
    private double longitude;

    public ChargingStation(String id){
        this.id = id;
    }

    public String getId(){ return id; }
    public String getName(){ return name; }
    public String getFreeformAddress(){ return freeformAddress; }
    public double getLatitude(){ return latitude; }
    public double getLongitude(){ return longitude; }
}
