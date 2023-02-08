package it.ecteam.easycharge.controller;

import it.ecteam.easycharge.bean.ChargingStationBean;
import it.ecteam.easycharge.bean.ConnectorBean;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ChargingStationController {
    private ChargingStationController() {
    }
    private static final String APIKEY = System.getProperty("api.key");
    public static List<ChargingStationBean> getNearby(int radius) throws IOException, ParseException, LocationNotFoundException, org.json.simple.parser.ParseException, ChargingStationNotFoundException {

        List<ChargingStationBean> chargingStationList = new ArrayList<>();
        String jsonString;
        StringBuilder str = new StringBuilder();
        //Request to the tomtom service
        URL geocodingUrl = new URL("https://api.tomtom.com/search/2/nearbySearch/.json?"+MapController.getLocation()+"&radius="+radius+"&categorySet=7309&view=Unified&key="+getAPI());
        URLConnection geocoding = geocodingUrl.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        geocoding.getInputStream()));
        String inputLine;

        //Create a JSON string from the response
        while ((inputLine = in.readLine()) != null)
            str.append("\n").append(inputLine);
        in.close();

        jsonString = str.toString();
        chargingStationList = ChargingStationController.chargingStationParse(jsonString);
        return chargingStationList;
    }

    public static List<ConnectorBean> getChargingAvailability(String id) throws IOException, org.json.simple.parser.ParseException, ChargingStationNotFoundException {
        List<ConnectorBean> connectorList= new ArrayList<>();
        String jsonString;
        StringBuilder str = new StringBuilder();
        //Request to the tomtom service
        URL geocodingUrl = new URL("https://api.tomtom.com/search/2/chargingAvailability.json?key="+getAPI()+"&chargingAvailability="+id);
        URLConnection geocoding = geocodingUrl.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        geocoding.getInputStream()));
        String inputLine;

        //Create a JSON string from the response
        while ((inputLine = in.readLine()) != null)
            str.append("\n").append(inputLine);
        in.close();

        jsonString = str.toString();
        JSONParser parser = new JSONParser();

        //Get coordinates from JSON string
        JSONObject object = (JSONObject) parser.parse(jsonString);
        JSONArray resultsArray = (JSONArray) object.get("connectors");

        if(resultsArray.isEmpty()) {
            throw new ChargingStationNotFoundException("No EV charging station availability found");
        }

        int i;
        for(i=0; i < resultsArray.size(); i++) {
            ConnectorBean connectorBean = new ConnectorBean();
            JSONObject results = (JSONObject) resultsArray.get(i);
            JSONObject availability = (JSONObject) results.get("availability");
            JSONObject current = (JSONObject) availability.get("current");
            String type = (String) results.get("type");
            connectorBean.setType(type);
            Long total = (Long) results.get("total");
            connectorBean.setTotal(total);
            Long available = (Long) current.get("available");
            connectorBean.setAvailable(available);
            Long occupied = (Long) current.get("occupied");
            connectorBean.setOccupied(occupied);
            Long reserved = (Long) current.get("reserved");
            connectorBean.setReserved(reserved);
            Long unknown = (Long) current.get("unknown");
            connectorBean.setUnknown(unknown);
            Long outOfService = (Long) current.get("outOfService");
            connectorBean.setOutOfService(outOfService);

            connectorList.add(connectorBean);
        }
        return connectorList;
    }

    public static ChargingStationBean getCSInfo(String csID) throws IOException, ParseException, LocationNotFoundException, org.json.simple.parser.ParseException, ChargingStationNotFoundException {

        ChargingStationBean chargingStation = new ChargingStationBean();
        String jsonString;
        StringBuilder str = new StringBuilder();
        //Request to the tomtom service
        URL geocodingUrl = new URL("https://api.tomtom.com/search/2/place.json?entityId="+csID+"&key="+getAPI());
        URLConnection geocoding = geocodingUrl.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        geocoding.getInputStream()));
        String inputLine;

        //Create a JSON string from the response
        while ((inputLine = in.readLine()) != null)
            str.append("\n").append(inputLine);
        in.close();

        jsonString = str.toString();

        JSONParser parser = new JSONParser();

        //Get data from JSON string
        JSONObject object = (JSONObject) parser.parse(jsonString);
        JSONArray resultsArray = (JSONArray) object.get("results");

        if(resultsArray.isEmpty()) {
            throw new LocationNotFoundException("No EV charging station found");
        }

        //Entering data into a ChargingStationBean
        JSONObject results = (JSONObject) resultsArray.get(0);
        JSONObject poi = (JSONObject) results.get("poi");
        JSONObject address = (JSONObject) results.get("address");
        JSONObject position = (JSONObject) results.get("position");

        String name = (String) poi.get("name");
        chargingStation.setName(name);
        String id = (String) results.get("info");
        chargingStation.setId(id.replace("search:ev:",""));
        String addressString = (String) address.get("freeformAddress");
        chargingStation.setFreeformAddress(addressString);
        double lat = (double) position.get("lat");
        double lon = (double) position.get("lon");
        chargingStation.setLatitude(lat);
        chargingStation.setLongitude(lon);

        return chargingStation;
    }

    public static List<ChargingStationBean> chargingStationParse(String jsonString) throws org.json.simple.parser.ParseException, LocationNotFoundException {
        List<ChargingStationBean> chargingStationList = new ArrayList<>();

        JSONParser parser = new JSONParser();

        //Get data from JSON string
        JSONObject object = (JSONObject) parser.parse(jsonString);
        JSONArray resultsArray = (JSONArray) object.get("results");

        if(resultsArray.isEmpty()) {
            throw new LocationNotFoundException("No EV charging station found");
        }

        //Entering data into a ChargingStationBean list
        int i;
        for(i=0;i<resultsArray.size();i++) {
            ChargingStationBean chargingStation = new ChargingStationBean();

            JSONObject results = (JSONObject) resultsArray.get(i);
            JSONObject poi = (JSONObject) results.get("poi");
            JSONObject address = (JSONObject) results.get("address");
            JSONObject position = (JSONObject) results.get("position");

            String name = (String) poi.get("name");
            chargingStation.setName(name);
            String id = (String) results.get("info");
            chargingStation.setId(id.replace("search:ev:",""));
            String addressString = (String) address.get("freeformAddress");
            chargingStation.setFreeformAddress(addressString);
            double lat = (double) position.get("lat");
            double lon = (double) position.get("lon");
            chargingStation.setLatitude(lat);
            chargingStation.setLongitude(lon);

            chargingStationList.add(chargingStation);
        }
        return chargingStationList;

    }

    public static String getAPI() {
        return ChargingStationController.APIKEY;
    }
}
