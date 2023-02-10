package it.ecteam.easycharge.entity;

public class ChargingStation {
    private String id;
    private String name;
    private String freeformAddress;
    private double latitude;
    private double longitude;

    public ChargingStation(String id){ this.id = id; }

    public String getId(){ return this.id; }
    public String getName(){ return this.name; }
    public String getFreeformAddress(){ return this.freeformAddress; }
    public double getLatitude(){ return this.latitude; }
    public double getLongitude(){ return this.longitude; }
}
