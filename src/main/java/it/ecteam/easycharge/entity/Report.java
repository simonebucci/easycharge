package it.ecteam.easycharge.entity;

import java.util.Date;

public class Report {
    private String chargingStationID;
    private String username;
    private String comment;
    private Date date;
    private int point;
    private String giver;

    public Report(String chargingStationID, String username, String comment, Date date, int point){
        this.chargingStationID = chargingStationID;
        this.username = username;
        this.comment = comment;
        this.date = date;
        this.point = point;
    }

    public Report(String username){
        this.username = username;
    }

    public String getChargingStationID(){ return chargingStationID; }
    public String getUsername(){ return username; }
    public String getComment(){ return comment; }
    public Date getDate(){ return date; }
    public int getPoint() { return point; }
    public String getGiver() { return giver; }
}
