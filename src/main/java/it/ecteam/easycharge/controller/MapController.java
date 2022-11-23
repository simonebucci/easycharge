package it.ecteam.easycharge.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.ecteam.easycharge.bean.ChargingStationBean;
import it.ecteam.easycharge.bean.ConnectorBean;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject; 
import org.json.simple.parser.*;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class MapController {

    private MapController() {

    }

	private static final String APIKEY = "";
    private static final String GAPIKEY = "";

    public static List<ChargingStationBean> getNearby(int radius) throws IOException, ParseException, LocationNotFoundException, org.json.simple.parser.ParseException, ChargingStationNotFoundException {

        List<ChargingStationBean> chargingStationList = new ArrayList<>();
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
            String id = (String) results.get("id");
            chargingStation.setId(id);
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

    public static String getLocation() throws IOException, LocationNotFoundException, org.json.simple.parser.ParseException {
        String jsonString;
        StringBuilder str = new StringBuilder();

        //Request to the geocoding service
        URL locationUrl = new URL("https://ipgeolocation.abstractapi.com/v1/?api_key=");
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

    public static List<ConnectorBean> getChargingAvailability(String id) throws IOException, org.json.simple.parser.ParseException, ChargingStationNotFoundException {
        List<ConnectorBean> connectorList= new ArrayList<>();
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

    public static List<Double> getCoordinates(String string) throws IOException, org.json.simple.parser.ParseException, LocationNotFoundException {
        List<Double> coord = new ArrayList<>();

        String jsonString;
        StringBuilder str = new StringBuilder();
        //Request to the geocoding service
        URL geocodingUrl = new URL("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="+string+"&inputtype=textquery&fields=formatted_address%2Cname%2Crating%2Copening_hours%2Cgeometry&key="+getGAPI());
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

            String formattedAddress = (String) results.get("formatted_address");
            System.out.println(formattedAddress);

            Double lat = (Double) location.get("lat");
            Double lng = (Double) location.get("lng");

            coord.add(lat);
            coord.add(lng);
            System.out.println(lat+","+lng);
            return coord;
        }


    public static List<ChargingStationBean> getOnRoute(List<Double> start, List<Double> end) {
        List<ChargingStationBean> chargingStationList = new ArrayList<>();
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("https://api.tomtom.com/search/2/searchAlongRoute/charging%20station.json?maxDetourTime=1200&categorySet=7309&key=" + getAPI());
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            String json = "{\r\n" +
                    "  \"route\": {\r\n" +
                    "  \"points\": [\r\n" +
                    "  {\r\n" +
                    "  \"lat\": "+start.get(0)+",\r\n" +
                    "  \"lon\": "+start.get(1)+"\r\n" +
                    "  },\r\n" +
                    "  {\r\n" +
                    "  \"lat\": "+end.get(0)+",\r\n" +
                    "  \"lon\": "+end.get(1)+"\r\n" +
                    "  }\r\n" +
                    "  ]\r\n" +
                    "  }\r\n" +
                    "}";
            StringEntity stringEntity = new StringEntity(json);
            httpPost.setEntity(stringEntity);

            System.out.println("Executing request " + httpPost.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };

            String responseBody = httpclient.execute(httpPost, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);

            JSONParser parser = new JSONParser();

            //Get coordinates from JSON string
            JSONObject object = (JSONObject) parser.parse(responseBody);
            JSONArray resultsArray = (JSONArray) object.get("results");

            if (resultsArray.isEmpty()) {
                throw new ChargingStationNotFoundException("No EV charging station availability found");
            }

            int i;
            for(i=0;i<resultsArray.size();i++) {
                ChargingStationBean chargingStation = new ChargingStationBean();

                JSONObject results = (JSONObject) resultsArray.get(i);
                JSONObject poi = (JSONObject) results.get("poi");
                JSONObject address = (JSONObject) results.get("address");
                JSONObject position = (JSONObject) results.get("position");

                String name = (String) poi.get("name");
                chargingStation.setName(name);
                String id = (String) results.get("id");
                chargingStation.setId(id);
                String addressString = (String) address.get("freeformAddress");
                chargingStation.setFreeformAddress(addressString);
                double lat = (double) position.get("lat");
                double lon = (double) position.get("lon");
                chargingStation.setLatitude(lat);
                chargingStation.setLongitude(lon);

                chargingStationList.add(chargingStation);
            }


        } catch (ChargingStationNotFoundException | org.json.simple.parser.ParseException | IOException e) {
            e.printStackTrace();
        }
        return chargingStationList;
    }

        public static String getAPI() {
            return MapController.APIKEY;
        }
        public static String getGAPI() {
            return MapController.GAPIKEY;
            }

}
