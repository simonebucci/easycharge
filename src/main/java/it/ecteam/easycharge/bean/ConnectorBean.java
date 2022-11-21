package it.ecteam.easycharge.bean;

import java.io.Serializable;

public class ConnectorBean implements Serializable {

    private String type;
    private long total;
    private long available;
    private long occupied;
    private long reserved;
    private long unknown;
    private long outOfService;

    public String getType() {
        return type;
    }
    public void setType(String type){ this.type = type;}

    public long getTotal() {
        return total;
    }
    public void setTotal(long total ){ this.total = total;}

    public long getAvailable() {
        return available;
    }
    public void setAvailable(long available){ this.available = available;}

    public long getOccupied() {
        return occupied;
    }
    public void setOccupied(long occupied){ this.occupied = occupied;}

    public long getReserved() {
        return reserved;
    }
    public void setReserved(long reserved){ this.reserved = reserved;}

    public long getUnknown() {
        return unknown;
    }
    public void setUnknown(long unknown){ this.unknown = unknown;}

    public long getOutOfService() {
        return outOfService;
    }
    public void setOutOfService(long outOfService){ this.outOfService = outOfService;}
}
