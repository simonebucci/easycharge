package it.ecteam.easycharge.controller;

import it.ecteam.easycharge.bean.ChargingStationBean;
import it.ecteam.easycharge.bean.ConnectorBean;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RouteController {

    public static List<ChargingStationBean> getPerfectRoute(List<ChargingStationBean> csOnRoute, List<Double> start, List<Double> end, int range, String cType) throws IOException, ParseException, LocationNotFoundException, ChargingStationNotFoundException {
        List<ChargingStationBean> perfectRoute = new ArrayList<>();
        List<ConnectorBean> connectorBeanList = new ArrayList<>();

        Long totalDistance = MapController.getDistance(start, end);

        if (range < ((totalDistance - 50000) / 1000)) //if the capacity of the car is less than the total distance of the trip minus 50km
        {
            Long stopsCount = (((totalDistance - 50000) / 1000) / range);
            int i;
            int stops = 0;
            for (i = 0; i < csOnRoute.size() && stops <= stopsCount; i++) {
                connectorBeanList = MapController.getChargingAvailability(csOnRoute.get(i).getId());
                int newStop = 0;
                /*int k;
                for(k=0; k<connectorBeanList.size(); k++){
                    if(Objects.equals(connectorBeanList.get(k).getType().substring(0, 12), cType.substring(0, 12))&& newStop == 0){
                        List<Double> coords = new ArrayList<>();
                        coords.add(csOnRoute.get(i).getLatitude());
                        coords.add(csOnRoute.get(i).getLongitude());
                        Long distance = MapController.getDistance(start, coords);
                        if(distance > range - 50000 && distance < range){
                            perfectRoute.add(csOnRoute.get(i));
                            start = coords;
                            stops++;
                            newStop = 1;
                        }
                    }
                }*/
                int k;
                List<Double> coords = new ArrayList<>();
                for (k = 0; k < connectorBeanList.size(); k++) {
                    if (Objects.equals(connectorBeanList.get(k).getType(), "Chademo")) {
                        if (Objects.equals(connectorBeanList.get(k).getType(), cType) && newStop == 0) {
                            coords.add(csOnRoute.get(i).getLatitude());
                            coords.add(csOnRoute.get(i).getLongitude());
                            Long distance = MapController.getDistance(start, coords);
                            if (distance > range - 50000 && distance < range) {
                                perfectRoute.add(csOnRoute.get(i));
                                start = coords;
                                stops++;
                                newStop = 1;
                            }
                            k = connectorBeanList.size();
                        }
                    } else if (Objects.equals(connectorBeanList.get(k).getType().substring(0, 13), cType.substring(0, 13)) && newStop == 0) {
                        coords.add(csOnRoute.get(i).getLatitude());
                        coords.add(csOnRoute.get(i).getLongitude());
                        Long distance = MapController.getDistance(start, coords);
                        if (distance > range - 50000 && distance < range) {
                            perfectRoute.add(csOnRoute.get(i));
                            start = coords;
                            stops++;
                            newStop = 1;
                        }
                        k = connectorBeanList.size();
                    }
                }
            }
        } else {
            return perfectRoute;
        }
        return perfectRoute;
    }

    public static List<ChargingStationBean> orderRoute(List<ChargingStationBean> routeToOrder, List<Double> start, List<Double> end) throws IOException, ParseException, LocationNotFoundException {
        List<ChargingStationBean> orderedRoute = new ArrayList<>();
        List<Double> coordsA = new ArrayList<>();
        List<Double> coordsB = new ArrayList<>();
        List<Double> coordsC = new ArrayList<>();
        ChargingStationBean min = routeToOrder.get(0);

        int pos = 0;
        for (int i = 1; i < routeToOrder.size(); i++) {
            coordsA.add(min.getLatitude());
            coordsA.add(min.getLongitude());
            coordsB.add(routeToOrder.get(i).getLatitude());
            coordsB.add(routeToOrder.get(i).getLongitude());
            if (MapController.getDistance(start, coordsA) > MapController.getDistance(start, coordsB)) {
                min = routeToOrder.get(i);
                pos = i;
            }
        }
        //orderedRoute.set(0, min);
        ChargingStationBean tmp = routeToOrder.get(0);
        routeToOrder.set(0, min);
        routeToOrder.set(pos, tmp);
        for (int k = 0; k < routeToOrder.size() - 2; k++) {
            coordsA.add(routeToOrder.get(k).getLatitude());
            coordsA.add(routeToOrder.get(k).getLongitude());
            coordsB.add(routeToOrder.get(k + 1).getLatitude());
            coordsB.add(routeToOrder.get(k + 1).getLongitude());
            coordsC.add(routeToOrder.get(k + 2).getLatitude());
            coordsC.add(routeToOrder.get(k + 2).getLongitude());
            Long distanceAB = MapController.getDistance(coordsA, coordsB);
            Long distanceAC = MapController.getDistance(coordsA, coordsC);
            if (distanceAB > distanceAC) {
                routeToOrder.set(k, routeToOrder.get(k+1));
            }
        }
        return routeToOrder;
    }
}
