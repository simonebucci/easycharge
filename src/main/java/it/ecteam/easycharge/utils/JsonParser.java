package it.ecteam.easycharge.utils;

import it.ecteam.easycharge.exceptions.FileNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class JsonParser {

    public static void parseNearby(int i) throws LocationNotFoundException, org.json.simple.parser.ParseException, FileNotFoundException {
        FileManager file = new FileManager();
        if (!file.fileExists("nearby")) {
            throw new FileNotFoundException("No EV charging station file found");
        }
        String nearby = file.readFile("nearby");
        JSONParser parser = new JSONParser();

        //Get coordinates from JSON string
        JSONObject object = (JSONObject) parser.parse(nearby);
        JSONArray resultsArray = (JSONArray) object.get("results");

        if (resultsArray.isEmpty()) {
            throw new LocationNotFoundException("No EV charging station found");
        }

            JSONObject results = (JSONObject) resultsArray.get(i);
            JSONObject geometry = (JSONObject) results.get("geometry");
            JSONObject location = (JSONObject) geometry.get("location");
            String name = (String) results.get("name");
            String status = (String) results.get("business_status");
            String place_id = (String) results.get("place_id");
            String address = (String) results.get("vicinity");
            double lat = (double) location.get("lat");
            double lng = (double) location.get("lng");
            System.out.println("parsed: "+lat+lng);

    }
}
