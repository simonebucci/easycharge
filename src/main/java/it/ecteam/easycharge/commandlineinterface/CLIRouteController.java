package it.ecteam.easycharge.commandlineinterface;

import it.ecteam.easycharge.bean.CarBean;
import it.ecteam.easycharge.bean.ChargingStationBean;
import it.ecteam.easycharge.bean.ConnectorBean;
import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.controller.ChargingStationController;
import it.ecteam.easycharge.controller.MapController;
import it.ecteam.easycharge.controller.RouteController;
import it.ecteam.easycharge.controller.UserController;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import it.ecteam.easycharge.utils.SessionUser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.ecteam.easycharge.commandlineinterface.CommandLineInterface.*;

public class CLIRouteController {
    private UserBean ub = SessionUser.getInstance().getSession();
    private List<ChargingStationBean> chargingStationList = new ArrayList<>();
    private List<ConnectorBean> connectorBeanList = new ArrayList<>();
    private List<Double> startPoint = new ArrayList<>();
    private List<Double> endPoint = new ArrayList<>();
    private CarBean cb = new CarBean();
    protected static final Logger logger = Logger.getLogger("CLI");
    protected void searchRoute(String start, String end, Scanner input) {
        try {
            startPoint = MapController.getCoordinates(start);
            endPoint = MapController.getCoordinates(end);
        } catch (IOException | ParseException | LocationNotFoundException e) {
            logger.log(Level.WARNING, e.toString());
        }
        System.out.println("Do you want to filter by your connector?(y/n)");

        if(Objects.equals(input.nextLine(), "y")) {
            try {
                chargingStationList = RouteController.getOnRoute(startPoint, endPoint);
                printFCS(chargingStationList);
            } catch (IOException | ParseException | ChargingStationNotFoundException | LocationNotFoundException e) {
                logger.log(Level.WARNING, e.toString());
            }
        }else{
            try {
                chargingStationList = RouteController.getOnRoute(startPoint, endPoint);
                printCS(chargingStationList);
            } catch (IOException | ParseException | LocationNotFoundException | ChargingStationNotFoundException e) {
                logger.log(Level.WARNING, e.toString());
            }
        }

        System.out.println("Click on this link to see the route on a map: https://www.google.com/maps/dir/?api=1&origin="+ start +"&destination="+ end +"&travelmode=driving&waypoint_place_ids=ChIJ5zZx3kNjLxMRAIuXSFIRfwk%ChIJL6pCbOVhLxMRODH8uDzXLDo");
    }

    protected void perfectRoute(String start, String end) {
        try {
            startPoint = MapController.getCoordinates(start);
            endPoint = MapController.getCoordinates(end);
        } catch (IOException | ParseException | LocationNotFoundException e) {
            logger.log(Level.WARNING, e.toString());
        }

        try {
            chargingStationList = RouteController.getPerfectRoute(startPoint, endPoint, Integer.parseInt(cb.getRange()), cb.getConnectorType(), cb.getCapacity());
            int i;
            for(i=0; i < chargingStationList.size(); i++){
                System.out.println(i+1+". "+chargingStationList.get(i).getName()+", "+chargingStationList.get(i).getFreeformAddress());

                connectorBeanList = ChargingStationController.getChargingAvailability(chargingStationList.get(i).getId());
                int k;
                for(k=0; k < connectorBeanList.size(); k++) {
                    System.out.println(TYPE+ connectorBeanList.get(k).getType() + TOTAL + connectorBeanList.get(k).getTotal() + AVAILABLE + connectorBeanList.get(k).getAvailable() + OCCUPIED + connectorBeanList.get(k).getOccupied() + RESERVED + connectorBeanList.get(k).getReserved() + UNKNOWN + connectorBeanList.get(k).getUnknown() + OOS + connectorBeanList.get(k).getOutOfService() + SPACE);
                }
            }
        } catch (IOException | ParseException | ChargingStationNotFoundException | LocationNotFoundException e) {
            logger.log(Level.WARNING, e.toString());
        }
        if(chargingStationList.isEmpty()){
            System.out.println("Your car can reach the destination without any recharge.");
        }else {
            System.out.println("Click on this link to see the route on a map: https://www.google.com/maps/dir/?api=1&origin=" + start + "&destination=" + end + "&travelmode=driving&waypoint_place_ids=ChIJ5zZx3kNjLxMRAIuXSFIRfwk%ChIJL6pCbOVhLxMRODH8uDzXLDo");
        }
    }

    private void printFCS(List<ChargingStationBean> chargingStationList) throws ChargingStationNotFoundException, IOException, ParseException {
        int i;
        int notValid;
        for(i=0; i < chargingStationList.size(); i++){
            notValid=0;
            connectorBeanList = ChargingStationController.getChargingAvailability(chargingStationList.get(i).getId());
            int k;
            for(k=0; k < connectorBeanList.size(); k++) {
                if(!Objects.equals(connectorBeanList.get(k).getType().substring(0, 13), cb.getConnectorType().substring(0, 13))){
                    notValid = 1;
                }else{
                    System.out.println(TYPE + connectorBeanList.get(k).getType() + TOTAL + connectorBeanList.get(k).getTotal() + AVAILABLE + connectorBeanList.get(k).getAvailable() + OCCUPIED + connectorBeanList.get(k).getOccupied() + RESERVED + connectorBeanList.get(k).getReserved() + UNKNOWN + connectorBeanList.get(k).getUnknown() + OOS + connectorBeanList.get(k).getOutOfService() + SPACE);
                }
            }
            if(notValid!=1) {
                System.out.println(i + 1 + ". " + chargingStationList.get(i).getName() + ", " + chargingStationList.get(i).getFreeformAddress());
            }
        }
    }

    private void printCS(List<ChargingStationBean> chargingStationList) throws ChargingStationNotFoundException, IOException, ParseException {
        int i;
        for(i=0; i < chargingStationList.size(); i++){
            System.out.println(i+1+". "+chargingStationList.get(i).getName()+", "+chargingStationList.get(i).getFreeformAddress());

            connectorBeanList = ChargingStationController.getChargingAvailability(chargingStationList.get(i).getId());
            int k;
            for(k=0; k < connectorBeanList.size(); k++) {
                System.out.println(TYPE+ connectorBeanList.get(k).getType() + TOTAL + connectorBeanList.get(k).getTotal() + AVAILABLE + connectorBeanList.get(k).getAvailable() + OCCUPIED + connectorBeanList.get(k).getOccupied() + RESERVED + connectorBeanList.get(k).getReserved() + UNKNOWN + connectorBeanList.get(k).getUnknown() + OOS + connectorBeanList.get(k).getOutOfService() + SPACE);
            }
        }
    }
    private void print(){
        System.out.println(EC);
        System.out.println("--------Welcome "+ ub.getUsername() + "!--------");
        System.out.println(W);
        System.out.println("1. Search Charging Stations on a route");
        System.out.println("2. Search perfect route");
        System.out.println("3. Back");
        System.out.println("4. Exit");
    }

    public void init() {
        Scanner input = new Scanner(System.in);
        UserController uc = new UserController();
        cb = uc.getCar(ub.getUsername());
        print();
        do {
            switch (input.nextLine()) {
                case "1" -> {
                    System.out.println("Insert a start position: ");
                    String start = input.nextLine();
                    System.out.println("Insert a destination: ");
                    String end = input.nextLine();
                    searchRoute(start, end, input);
                    print();
                }
                case "2" -> {
                    System.out.println("Insert a start position: ");
                    String start = input.nextLine();
                    System.out.println("Insert a destination: ");
                    String end = input.nextLine();
                    perfectRoute(start, end);
                    print();
                }
                case "3" -> {
                    CLIUserHomeController uhc = new CLIUserHomeController();
                    uhc.init();
                }
                case "4" -> {
                    System.out.println(RED +"Bye bye" + RESET);
                    input.close();
                    return;
                }
                default -> System.out.println(CNF);
            }
            System.out.flush();

        }while(input.hasNext());
    }
}

