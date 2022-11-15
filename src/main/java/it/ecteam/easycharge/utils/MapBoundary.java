package it.ecteam.easycharge.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject; 
import org.json.simple.parser.*;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;

public class MapBoundary {
	
	private static final String APIKEY = "";
	
	public MapBoundary(){
		
	}

    public static void getNearby(int radius) throws IOException, ParseException, LocationNotFoundException, org.json.simple.parser.ParseException, ChargingStationNotFoundException {
        String jsonString;
        StringBuilder str = new StringBuilder();
        //Request to the geocoding service
        URL geocodingUrl = new URL("https://api.tomtom.com/search/2/nearbySearch/.json?"+getLocation()+"&radius="+radius+"&categorySet=7309&view=Unified&key="+getAPI());
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
        JSONArray resultsArray = (JSONArray) object.get("results");

        if(resultsArray.isEmpty()) {
            throw new LocationNotFoundException("No EV charging station found");
        }

        int i;
        for(i=0;i<10;i++) {
            JSONObject results = (JSONObject) resultsArray.get(i);
            JSONObject poi = (JSONObject) results.get("poi");
            JSONObject address = (JSONObject) results.get("address");
            JSONObject position = (JSONObject) results.get("position");
            JSONObject chargingPark = (JSONObject) results.get("chargingPark");
            JSONArray connectors = (JSONArray) chargingPark.get("connectors");

            String name = (String) poi.get("name");
            String id = (String) results.get("id");
            String addressString = (String) address.get("freeformAddress");

            System.out.println(name);
            System.out.println(id);
            System.out.println(addressString);

            int k;
            for(k=0;k<1;k++){
                JSONObject cResults = (JSONObject) connectors.get(k);
                String cType = (String) cResults.get("connectorType");
                System.out.println(cType);
            }

            getChargingAvailability(id);

            double lat = (double) position.get("lat");
            double lon = (double) position.get("lon");

            System.out.println(lat);
            System.out.println(lon);
            System.out.println("fine dati \n");
        }
    }

    public static String getLocation() throws IOException, LocationNotFoundException, org.json.simple.parser.ParseException {
        String jsonString;
        StringBuilder str = new StringBuilder();

        //Request to the geocoding service
        URL locationUrl = new URL("https://ipgeolocation.abstractapi.com/v1/?api_key=01c233c3ae9c4e8aad50179916b11dd8");
        URLConnection location = locationUrl.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        location.getInputStream()));
        String inputLine;

        //Create a JSON string from the response
        while ((inputLine = in.readLine()) != null)
            str.append("\n").append(inputLine);
        in.close();

        jsonString = str.toString();
        JSONParser parser = new JSONParser();

        //Get coordinates from JSON string
        JSONObject object = (JSONObject) parser.parse(jsonString);
        if(object.isEmpty()) {
            throw new LocationNotFoundException("No location found");
        }

        double lat = (double) object.get("latitude");
        double lng = (double) object.get("longitude");

        System.out.println(lat+" "+lng);

        return "lat="+lat+"&lon="+lng;
    }

	public static String getAPI() {
		return MapBoundary.APIKEY;
	}

    public static void getChargingAvailability(String id) throws IOException, org.json.simple.parser.ParseException, ChargingStationNotFoundException {
        String jsonString;
        StringBuilder str = new StringBuilder();
        //Request to the geocoding service
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

        JSONObject results = (JSONObject) resultsArray.get(0);
        JSONObject availability = (JSONObject) results.get("availability");
        JSONObject current = (JSONObject) availability.get("current");
        String type = (String) results.get("type");
        Long total = (Long) results.get("total");
        Long available = (Long) current.get("available");
        Long occupied = (Long) current.get("occupied");
        Long reserved = (Long) current.get("reserved");
        Long unknown = (Long) current.get("unknown");
        Long outOfService = (Long) current.get("outOfService");

        System.out.println("Charging Availability");
        System.out.println(type);
        System.out.println("Total:"+total);
        System.out.println("Available="+available);
        System.out.println("Occupied="+occupied);
        System.out.println("Reserved="+reserved);
        System.out.println("Unknown="+unknown);
        System.out.println("Out of service="+outOfService);

    }
}
