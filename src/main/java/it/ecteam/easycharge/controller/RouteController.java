package it.ecteam.easycharge.controller;

import it.ecteam.easycharge.bean.ChargingStationBean;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RouteController {

    public static List<ChargingStationBean> getPerfectRoute(List<ChargingStationBean> csonroute, List<Double> start, List<Double> end, int range) throws IOException, ParseException, LocationNotFoundException {
        List<ChargingStationBean> perfectRoute = new ArrayList<>();

        Long totalDistance = MapController.getDistance(start, end);

        if(range < ((totalDistance-50000)/1000)) //if the capacity of the car is less than the total distance of the trip minus 50 km
        {
            Long stopsCount = (((totalDistance-50000)/1000)/range);
            System.out.println((totalDistance-50000)/1000);
            System.out.println(range);
            System.out.println(stopsCount);
        }else{
            return perfectRoute;
        }

        return perfectRoute;
    }
}
