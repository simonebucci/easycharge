package it.ecteam.easycharge.bean;

import java.io.Serializable;
import java.util.Date;

public class ReportBean implements Serializable {
    private String chargingStationID;
    private String username;
    private String comment;
    private Date date;
    private int point;

    public String getChargingStationID() {
        return chargingStationID;
    }
    public void setChargingStationID(String chargingStationID){ this.chargingStationID = chargingStationID;}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public int getPoint() { return point; }
    public void setPoint(int point) { this.point = point; }
}
