package it.ecteam.easycharge.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*;

import it.ecteam.easycharge.exceptions.LocationNotFoundException;

public class GoogleMapBoundary {
	
	private static final String GOOGLEAPI = "";
	
	public GoogleMapBoundary(){
		
	}
	
/*	public static List<Double> locateAddress(String address) throws IOException, ParseException, LocationNotFoundException, org.json.simple.parser.ParseException {
		String jsonString = "";
		List<Double> coordinates = new ArrayList<>();
        StringBuilder str = new StringBuilder();
        
		//Request to the geocoding service
		URL geocodingUrl = new URL("https://maps.googleapis.com/maps/api/geocode/json?"
        		+ "address=" + address.replace(" ", "+") + "&key=" + GOOGLEAPI);
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
        	throw new LocationNotFoundException("The location " + address + " was not found");
        }
        
        JSONObject results = (JSONObject) resultsArray.get(0);
        JSONObject geometry = (JSONObject) results.get("geometry");
        JSONObject location = (JSONObject) geometry.get("location");
        
        double lat = (double) location.get("lat"); 
        double lng = (double) location.get("lng");
           
        coordinates.add(lat);
        coordinates.add(lng);
        
        return coordinates;
	}*/

    public static void getNearby(int radius) throws IOException, ParseException, LocationNotFoundException, org.json.simple.parser.ParseException {
        String jsonString;
        StringBuilder str = new StringBuilder();
        //Request to the geocoding service
        URL geocodingUrl = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+getLocation()+"&radius="+radius+"&type=ev_charging_station&keyword=colonnina+ricarica+elettrica"
                + "&key=" + GOOGLEAPI);
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
            JSONObject geometry = (JSONObject) results.get("geometry");
            JSONObject location = (JSONObject) geometry.get("location");
            String name = (String) results.get("name");
            String status = (String) results.get("business_status");
            String place_id = (String) results.get("place_id");
            String address = (String) results.get("vicinity");
            System.out.println(name);
            System.out.println(status);
            System.out.println(place_id);
            System.out.println(address);

            double lat = (double) location.get("lat");
            double lng = (double) location.get("lng");

            System.out.println(lat);
            System.out.println(lng);
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

        System.out.println(lat+"%2C"+lng);


        return lat+"%2C"+lng;
    }

	public static String getAPI() {
		return GoogleMapBoundary.GOOGLEAPI;
	}
}
