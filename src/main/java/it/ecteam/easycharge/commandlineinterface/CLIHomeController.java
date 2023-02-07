package it.ecteam.easycharge.commandlineinterface;

import it.ecteam.easycharge.bean.BusinessBean;
import it.ecteam.easycharge.bean.ChargingStationBean;
import it.ecteam.easycharge.bean.ConnectorBean;
import it.ecteam.easycharge.bean.ReportBean;
import it.ecteam.easycharge.controller.BusinessController;
import it.ecteam.easycharge.controller.ChargingStationController;
import it.ecteam.easycharge.controller.ReportController;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static it.ecteam.easycharge.commandlineinterface.CommandLineInterface.*;

public class CLIHomeController {
    private List<ChargingStationBean> chargingStationList = new ArrayList<>();
    private List<ConnectorBean> connectorBeanList = new ArrayList<>();
    private List<ReportBean> report = new ArrayList<>();
    private List<BusinessBean> chargingStationAds = new ArrayList<>();
    private final SecureRandom r = new SecureRandom();

    public void nearby(Integer range, Scanner input){
        String csid;

        try {
            chargingStationList = ChargingStationController.getNearby(range); //radius range 1 to 50000
            int i;
            for(i=0; i < chargingStationList.size(); i++){
                System.out.println(i+1+". "+chargingStationList.get(i).getName()+", "+chargingStationList.get(i).getFreeformAddress());
            }
        } catch (IOException | ParseException | LocationNotFoundException | java.text.ParseException | ChargingStationNotFoundException e) {
            e.printStackTrace();
        }
        printN();
        do{

            switch (input.nextLine()) {
                case "1" -> {
                    System.out.println("Insert the charging station number: ");
                    int num = Integer.parseInt(input.nextLine());
                    if(num < chargingStationList.size() && num >0){
                        csid = chargingStationList.get(num-1).getId();
                        try {
                            connectorBeanList = ChargingStationController.getChargingAvailability(csid);
                        } catch (IOException | ChargingStationNotFoundException | ParseException e) {
                            throw new RuntimeException(e);
                        }
                        int k;
                        for(k=0; k < connectorBeanList.size(); k++) {
                            System.out.println("Type:"+ connectorBeanList.get(k).getType() + "\nTotal: " + connectorBeanList.get(k).getTotal() + "\nAvailable: " + connectorBeanList.get(k).getAvailable() + "\nOccupied: " + connectorBeanList.get(k).getOccupied() + "\nReserved: " + connectorBeanList.get(k).getReserved() + "\nUnknown: " + connectorBeanList.get(k).getUnknown() + "\nOutOfService: " + connectorBeanList.get(k).getOutOfService() + "\n     ");
                        }
                        chargingStationAds = BusinessController.getCSAds(csid);
                        if (!chargingStationAds.isEmpty()) {
                            int rand = r.nextInt((chargingStationAds).size());
                            System.out.println(chargingStationAds.get(rand).getAd()+" located in " + chargingStationAds.get(rand).getAddress());
                        }
                    }else{
                        System.out.println("Wrong charging station number");
                        return;
                    }
                    printN();
                }
                case "2" -> {
                    System.out.println("Insert the charging station number: ");
                    int num = Integer.parseInt(input.nextLine());
                    report = ReportController.getUserReport(String.valueOf(chargingStationList.get(num - 1).getId()));
                    if(!report.isEmpty()){
                        int j;
                        for(j=0; j < report.size(); j++) {
                            System.out.println(report.get(j).getUsername() + "\nsaid: " + report.get(j).getComment() + "\nDate: " + report.get(j).getDate()+ "\nLikes: " + report.get(j).getPoint());
                        }
                    }else{
                        System.out.println("No report found.");
                    }
                    return;
                }
                case "3" -> {
                    return;
                }

                default -> System.out.println(CNF);
            }
            System.out.flush();
        }while(input.hasNext());
    }
    public void printN(){
        System.out.println(EC);
        System.out.println(G);
        System.out.println(W);
        System.out.println(CA);
        System.out.println("2. Report");
        System.out.println("3. Back");
    }
    public void print(){
        System.out.println(EC);
        System.out.println(G);
        System.out.println(W);
        System.out.println("1. Search for Nearby Charging Stations");
        System.out.println("2. Login/SignUp");
        System.out.println("3. Exit");
    }

    public void init(){
        Scanner input = new Scanner(System.in);
        print();
        do {

            switch (input.nextLine()) {
                case "1" -> {
                    System.out.println("Insert a search range in km: ");
                    nearby(Integer.parseInt(input.nextLine())*1000, input);
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
                default -> System.out.println(CNF);
            }
            System.out.flush();

        }while(input.hasNext());

    }
}
