package it.ecteam.easycharge.bean;

import java.io.Serializable;

public class CarBean implements Serializable {
    private String name;
    private String capacity;
    private String range;
    private String connectorType;

    public String getName() {
        return name;
    }

    public void setName(String name){ this.name = name;}

    public String getCapacity(){ return capacity; }

    public void setCapacity(String capacity){ this.capacity = capacity;}

    public void setRange(String range){ this.range = range; }

    public String getRange(){ return range; }

    public void setConnectorType(String connectorType){ this.connectorType = connectorType; }

    public String getConnectorType(){ return connectorType; }

}
