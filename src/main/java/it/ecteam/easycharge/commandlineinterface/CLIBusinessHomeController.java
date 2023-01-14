package it.ecteam.easycharge.commandlineinterface;

import it.ecteam.easycharge.bean.ChargingStationBean;
import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.controller.MapController;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import it.ecteam.easycharge.utils.SessionUser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLIBusinessHomeController {
    private UserBean ub = SessionUser.getInstance().getSession();
    private List<ChargingStationBean> chargingStationList = new ArrayList<>();

    public void nearby(Integer range){
        try {
            chargingStationList = MapController.getNearby(range); //radius range 1 to 50000
            int i;
            for(i=0; i < chargingStationList.size(); i++){
                System.out.println(i+1+". "+chargingStationList.get(i).getName()+", "+chargingStationList.get(i).getFreeformAddress());
            }
        } catch (IOException | ParseException | LocationNotFoundException | java.text.ParseException | ChargingStationNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void init(){
        Scanner input = new Scanner(System.in);
        do {
            System.out.println("----------EasyCharge----------");
            System.out.println("--------Welcome "+ ub.getUsername() + "!--------");
            System.out.println("----What can I do for you?----");
            System.out.println("1. Search for Nearby Charging Stations");
            System.out.println("2. Business");
            System.out.println("3. Exit");

            switch (input.nextLine()) {
                case "1" -> {
                    System.out.println("Insert a search range in km: ");
                    nearby(Integer.parseInt(input.nextLine())*1000);
                }
                case "2" -> {

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
