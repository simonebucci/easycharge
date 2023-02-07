package it.ecteam.easycharge.controller;

import it.ecteam.easycharge.bean.ChargingStationBean;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RouteController {

    private static final String APIKEY = System.getProperty("api.key");

    private RouteController() {
    }
    private static final String APP = "application/json";
    private static final String G = "{";
    private static final String G2 = "}";
    private static final String SPACE = "        ";
    public static List<ChargingStationBean> getPerfectRoute(List<Double> start, List<Double> end, int range, String cType, String capacity) throws IOException, ParseException, LocationNotFoundException, ChargingStationNotFoundException {

        List<ChargingStationBean> chargingStationList = new ArrayList<>();
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("https://api.tomtom.com/routing/1/calculateLongDistanceEVRoute/"+ start.get(0)+"%2C"+start.get(1)+"%3A"+end.get(0)+"%2C"+end.get(1)+ "/json?vehicleEngineType=electric&constantSpeedConsumptionInkWhPerHundredkm=50.0%2C6.5%3A100.0%2C8.5&currentChargeInkWh="+capacity+"&maxChargeInkWh="+capacity+"&minChargeAtDestinationInkWh=5.2&minChargeAtChargingStopsInkWh=1.5&key=" + getAPI());
            httpPost.setHeader("accept", APP);
            httpPost.setHeader("Content-type", APP);
            String json = "{\n" +
                    "  \"chargingModes\": [\n" +
                    "    {\n" +
                    "      \"chargingConnections\": [\n" +
                    SPACE+G+"\n" +
                    "          \"facilityType\": \"Charge_380_to_480V_3_Phase_at_32A\",\n" +
                    "          \"plugType\": \"IEC_62196_Type_2_Outlet\"\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"chargingCurve\": [\n" +
                    SPACE+G+"\n" +
                    "          \"chargeInkWh\": 6,\n" +
                    "          \"timeToChargeInSeconds\": 360\n" +
                    SPACE+G2+",\n" +
                    SPACE+G+"\n" +
                    "          \"chargeInkWh\": 12,\n" +
                    "          \"timeToChargeInSeconds\": 720\n" +
                    SPACE+G2+",\n" +
                    SPACE+G+"\n" +
                    "          \"chargeInkWh\": 28,\n" +
                    "          \"timeToChargeInSeconds\": 1944\n" +
                    SPACE+G2+",\n" +
                    SPACE+G+"\n" +
                    "          \"chargeInkWh\": "+capacity+",\n" +
                    "          \"timeToChargeInSeconds\": 4680\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

            StringEntity stringEntity = new StringEntity(json);
            httpPost.setEntity(stringEntity);

            // Create a custom response handler
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };

            String responseBody = httpclient.execute(httpPost, responseHandler);

            //responseBody parsing
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(responseBody);
            JSONArray resultsArray = (JSONArray) object.get("routes");

            if(resultsArray.isEmpty()) {
                throw new LocationNotFoundException("No perfect route found");
            }

            //Entering data into a ChargingStationBean list
            int i;
            for(i=0;i<resultsArray.size();i++) {
                ChargingStationBean chargingStation = new ChargingStationBean();

                JSONObject results = (JSONObject) resultsArray.get(i);
                JSONArray legs = (JSONArray) results.get("legs");
                int k;
                for(k=0;k<legs.size();k++){
                    JSONObject res = (JSONObject) legs.get(k);
                    JSONObject summary = (JSONObject) res.get("summary");
                    JSONObject cInfo = (JSONObject) summary.get("chargingInformationAtEndOfLeg");
                    if(cInfo != null){
                    JSONObject cLoc = (JSONObject) cInfo.get("chargingParkLocation");
                    String address = cLoc.get("street") +" "+cLoc.get("houseNumber")+", "+cLoc.get("postalCode")+" "+cLoc.get("city");
                    chargingStation.setFreeformAddress(address);
                    String csID = (String) cInfo.get("chargingParkExternalId");
                    chargingStation.setId(csID);
                    String name = (String) cInfo.get("chargingParkName");
                    chargingStation.setName(name);
                    chargingStationList.add(chargingStation);
                    }else{
                        k = legs.size();
                    }
                }
            }
        } catch (IOException | org.json.simple.parser.ParseException | LocationNotFoundException e) {
            e.printStackTrace();
        }
        return chargingStationList;
    }

    public static List<ChargingStationBean> getOnRoute(List<Double> start, List<Double> end) throws IOException, org.json.simple.parser.ParseException, LocationNotFoundException {
        List<ChargingStationBean> chargingStationList = new ArrayList<>();
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("https://api.tomtom.com/search/2/searchAlongRoute/charging%20station.json?maxDetourTime=1200&categorySet=7309&key=" + getAPI());
            httpPost.setHeader("Accept", APP);
            httpPost.setHeader("Content-type", APP);
            String json = "{\r\n" +
                    "  \"route\": {\r\n" +
                    "  \"points\": [\r\n" +
                    "  {\r\n" +
                    "  \"lat\": " + start.get(0) + ",\r\n" +
                    "  \"lon\": " + start.get(1) + "\r\n" +
                    "  },\r\n" +
                    "  {\r\n" +
                    "  \"lat\": " + end.get(0) + ",\r\n" +
                    "  \"lon\": " + end.get(1) + "\r\n" +
                    "  }\r\n" +
                    "  ]\r\n" +
                    "  }\r\n" +
                    "}";
            StringEntity stringEntity = new StringEntity(json);
            httpPost.setEntity(stringEntity);

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
            chargingStationList = ChargingStationController.chargingStationParse(responseBody);

        } catch (IOException | org.json.simple.parser.ParseException | LocationNotFoundException e) {
            e.printStackTrace();
        }
        return chargingStationList;
    }

    public static String getAPI() {
        return RouteController.APIKEY;
    }
}
