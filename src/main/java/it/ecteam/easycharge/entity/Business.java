package it.ecteam.easycharge.entity;

public class Business {
    private String name;
    private String address;
    private String username;
    private String id;
    private String ad;

    public Business(String name, String address, String username, String id, String ad){
        this.name = name;
        this.address = address;
        this.username = username;
        this.id = id;
        this.ad = ad;
    }

    public Business(String name, String address, String id, String ad){
        this.name = name;
        this.address = address;
        this.id = id;
        this.ad = ad;
    }

    public String getName(){ return name; }
    public String getAddress(){ return address; }
    public String getUsername(){ return username; }
    public String getId(){ return id; }
    public String getAd(){ return ad; }

}
