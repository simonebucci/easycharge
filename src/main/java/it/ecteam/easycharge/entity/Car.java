package it.ecteam.easycharge.entity;

public class Car {
    private String name;
    private String capacity;
    private String range;
    private String connectorType;

    public Car(String name, String capacity, String range, String cType){
        this.name = name;
        this.capacity = capacity;
        this.range = range;
        this.connectorType = cType;
    }

    public Car(String name){
        this.name = name;
    }

    public String getName(){ return name; }
    public String getCapacity(){ return capacity; }
    public String getRange(){ return range; }
    public String getConnectorType(){ return connectorType; }
}
