package it.ecteam.easycharge.commandlineinterface;

import it.ecteam.easycharge.bean.ChargingStationBean;
import it.ecteam.easycharge.bean.ConnectorBean;
import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.controller.ChargingStationController;
import it.ecteam.easycharge.controller.MapController;
import it.ecteam.easycharge.controller.RouteController;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import it.ecteam.easycharge.utils.SessionUser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLIRouteController {
    private UserBean ub = SessionUser.getInstance().getSession();
    private List<ChargingStationBean> chargingStationList = new ArrayList<>();
    private List<ConnectorBean> connectorBeanList = new ArrayList<>();
    private List<Double> startPoint = new ArrayList<>();
    private List<Double> endPoint = new ArrayList<>();

    protected void searchRoute(String start, String end) {
        try {
            startPoint = MapController.getCoordinates(start);
            endPoint = MapController.getCoordinates(end);
        } catch (IOException | ParseException | LocationNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            chargingStationList = RouteController.getOnRoute(startPoint, endPoint);
            int i;
            for(i=0; i < chargingStationList.size(); i++){
                System.out.println(i+1+". "+chargingStationList.get(i).getName()+", "+chargingStationList.get(i).getFreeformAddress());

                connectorBeanList = ChargingStationController.getChargingAvailability(chargingStationList.get(i).getId());
                int k;
                for(k=0; k < connectorBeanList.size(); k++) {
                    System.out.println("Type:"+ connectorBeanList.get(k).getType() + "\nTotal: " + connectorBeanList.get(k).getTotal() + "\nAvailable: " + connectorBeanList.get(k).getAvailable() + "\nOccupied: " + connectorBeanList.get(k).getOccupied() + "\nReserved: " + connectorBeanList.get(k).getReserved() + "\nUnknown: " + connectorBeanList.get(k).getUnknown() + "\nOutOfService: " + connectorBeanList.get(k).getOutOfService() + "\n     ");
                }
            }
        } catch (IOException | ParseException | ChargingStationNotFoundException | LocationNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Click on this link to see the route on a map: https://www.google.com/maps/dir/?api=1&origin="+ start +"&destination="+ end +"&travelmode=driving&waypoint_place_ids=ChIJ5zZx3kNjLxMRAIuXSFIRfwk%ChIJL6pCbOVhLxMRODH8uDzXLDo");
    }

    public void print(){
        System.out.println("----------EasyCharge----------");
        System.out.println("--------Welcome "+ ub.getUsername() + "!--------");
        System.out.println("----What can I do for you?----");
        System.out.println("1. Search Charging Stations on a route");
        System.out.println("2. Back");
        System.out.println("3. Exit");
    }

    public void init() {
        Scanner input = new Scanner(System.in);
        print();
        do {
            switch (input.nextLine()) {
                case "1" -> {
                    System.out.println("Insert a start position: ");
                    String start = input.nextLine();
                    System.out.println("Insert a destination: ");
                    String end = input.nextLine();
                    searchRoute(start, end);
                    print();
                }
                case "2" -> {
                    CLIUserHomeController uhc = new CLIUserHomeController();
                    uhc.init();
                }
                case "3" -> {
                    System.out.println("Bye bye");
                    input.close();
                    return;
                }
                default -> System.out.println("Command not found\n");
            }
            System.out.flush();

        }while(input.hasNext());
    }
}

