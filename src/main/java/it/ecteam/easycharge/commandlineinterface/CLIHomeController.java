package it.ecteam.easycharge.commandlineinterface;

import it.ecteam.easycharge.bean.ChargingStationBean;
import it.ecteam.easycharge.bean.ConnectorBean;
import it.ecteam.easycharge.controller.MapController;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static it.ecteam.easycharge.commandlineinterface.CommandLineInterface.*;

public class CLIHomeController {
    private List<ChargingStationBean> chargingStationList = new ArrayList<>();
    private List<ConnectorBean> connectorBeanList = new ArrayList<>();

    public void nearby(Integer range){
        try {
            chargingStationList = MapController.getNearby(range); //radius range 1 to 50000
            int i;
            for(i=0; i < chargingStationList.size(); i++){
                System.out.println(i+1+". "+chargingStationList.get(i).getName()+", "+chargingStationList.get(i).getFreeformAddress());
                int k;
                try {
                    connectorBeanList = MapController.getChargingAvailability(chargingStationList.get(i).getId());

                } catch (IOException | ChargingStationNotFoundException | ParseException e) {
                    e.printStackTrace();
                }

                for(k=0; k < connectorBeanList.size(); k++) {
                    System.out.println("Type:"+ connectorBeanList.get(k).getType() + "\nTotal: " + connectorBeanList.get(k).getTotal() + "\nAvailable: " + connectorBeanList.get(k).getAvailable() + "\nOccupied: " + connectorBeanList.get(k).getOccupied() + "\nReserved: " + connectorBeanList.get(k).getReserved() + "\nUnknown: " + connectorBeanList.get(k).getUnknown() + "\nOutOfService: " + connectorBeanList.get(k).getOutOfService() + "\n     ");
                }
            }
        } catch (IOException | ParseException | LocationNotFoundException | java.text.ParseException | ChargingStationNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void print(){
        System.out.println("----------EasyCharge----------");
        System.out.println("------------Guest-------------");
        System.out.println("----What can I do for you?----");
        System.out.println("1. Search for Nearby Charging Stations");
        System.out.println("2. Login/SignUp");
        System.out.println("3. Exit");
    }

    public void init(){
        Scanner input = new Scanner(System.in);
        do {
            System.out.println("----------EasyCharge----------");
            System.out.println("--------Welcome guest!--------");
            System.out.println("----What can I do for you?----");
            System.out.println("1. Search for Nearby Charging Stations");
            System.out.println("2. Login/SignUp");
            System.out.println("3. Exit");

            switch (input.nextLine()) {
                case "1" -> {
                    System.out.println("Insert a search range in km: ");
                    nearby(Integer.parseInt(input.nextLine())*1000);
                    print();
                }
                case "2" -> {
                    CLILoginController lc = new CLILoginController();
                    lc.init();
                }
                case "3" -> {
                    System.out.println(RED + "Bye bye" + RESET);
                    input.close();
                    return;
                }
                default -> System.out.println("Command not found\n");
            }
            System.out.flush();

        }while(input.hasNext());

    }
}
