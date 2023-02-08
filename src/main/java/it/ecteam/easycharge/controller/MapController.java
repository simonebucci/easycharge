package it.ecteam.easycharge.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject; 
import org.json.simple.parser.*;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;

public class MapController {
    private MapController(){
        throw new IllegalStateException("MapController class");
    }
    private static final String APIKEY = System.getProperty("api.key");
    private static final String GAPIKEY = System.getProperty("gapi.key");
    private static final String LAPIKEY = System.getProperty("lapi.key");


    public static String getLocation() throws IOException, LocationNotFoundException, org.json.simple.parser.ParseException {
        String jsonString;
        StringBuilder str = new StringBuilder();

        //Request to the geocoding service
        URL locationUrl = new URL("https://ipgeolocation.abstractapi.com/v1/?api_key="+getLGAPI());
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

        return "lat="+lat+"&lon="+lng;
    }

    public static List<Double> getCoordinates(String string) throws IOException, org.json.simple.parser.ParseException, LocationNotFoundException {
        List<Double> coord = new ArrayList<>();

        String jsonString;
        StringBuilder str = new StringBuilder();
        //Request to the Google service
        URL geocodingUrl = new URL("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="+string.replaceAll("\\s","%20")+"&inputtype=textquery&fields=formatted_address%2Cname%2Crating%2Copening_hours%2Cgeometry&key="+getGAPI());
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
        JSONArray resultsArray = (JSONArray) object.get("candidates");
        String status = (String) object.get("status");

        if(resultsArray.isEmpty() || Objects.equals(status, "ZERO_RESULTS")) {
            throw new LocationNotFoundException("No coordinates found for this location");
        }

            JSONObject results = (JSONObject) resultsArray.get(0);
            JSONObject geometry = (JSONObject) results.get("geometry");
            JSONObject location = (JSONObject) geometry.get("location");

            Double lat = (Double) location.get("lat");
            Double lng = (Double) location.get("lng");

            coord.add(lat);
            coord.add(lng);

            return coord;
        }

    public static Long getDistance(List<Double>  start, List<Double>  end) throws IOException, org.json.simple.parser.ParseException, LocationNotFoundException {

        String jsonString;
        StringBuilder str = new StringBuilder();
        //Request to the tomtom service
        URL geocodingUrl = new URL("https://api.tomtom.com/routing/1/calculateRoute/"+start.get(0)+"%2C"+start.get(1)+"%3A"+end.get(0)+"%2C"+end.get(1)+"/json?key="+getAPI());
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
        JSONArray resultsArray = (JSONArray) object.get("routes");


        if(resultsArray.isEmpty()) {
            throw new LocationNotFoundException("No distance found for this locations");
        }

        JSONObject results = (JSONObject) resultsArray.get(0);
        JSONObject summary = (JSONObject) results.get("summary");

        return (Long) summary.get("lengthInMeters");
    }

        public static String getAPI() {
            return MapController.APIKEY;
        }
        public static String getGAPI() {
            return MapController.GAPIKEY;
            }
        public static String getLGAPI() {
        return MapController.LAPIKEY;
    }

}
