package it.ecteam.easycharge.utils;

import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;

public class JsonRequest {

    public static String getNearby(int radius) throws IOException, ParseException, LocationNotFoundException, org.json.simple.parser.ParseException {
        String jsonString;
        StringBuilder str = new StringBuilder();
        //Request to the geocoding service
        URL geocodingUrl = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+ MapBoundary.getLocation()+"&radius="+radius+"&type=ev_charging_station&keyword=colonnina+ricarica+elettrica"
                + "&key=" + MapBoundary.getAPI());
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

        FileManager file = new FileManager();
        if (!file.fileExists("nearby")) {
            file.createFile("nearby");
        }
        file.writeToFile("nearby", jsonString);

        return jsonString;
        }
    }

