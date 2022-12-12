package it.ecteam.easycharge.entity;

public class Business {
    private String name;
    private String address;
    private String username;
    private String id;

    public Business(String name, String address, String username, String id){
        this.name = name;
        this.address = address;
        this.username = username;
        this.id = id;
    }

    public Business(String name, String address, String id){
        this.name = name;
        this.address = address;
        this.id = id;
    }

    public String getName(){ return name; }
    public String getAddress(){ return address; }
    public String getUsername(){ return username; }
    public String getId(){ return id; }

}
