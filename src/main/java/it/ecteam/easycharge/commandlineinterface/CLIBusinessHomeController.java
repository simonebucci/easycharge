package it.ecteam.easycharge.commandlineinterface;

import it.ecteam.easycharge.bean.BusinessBean;
import it.ecteam.easycharge.bean.ChargingStationBean;
import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.controller.BusinessController;
import it.ecteam.easycharge.controller.ChargingStationController;
import it.ecteam.easycharge.controller.MapController;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import it.ecteam.easycharge.utils.SessionUser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static it.ecteam.easycharge.commandlineinterface.CommandLineInterface.*;

public class CLIBusinessHomeController {
    private UserBean ub = SessionUser.getInstance().getSession();
    private List<ChargingStationBean> chargingStationList = new ArrayList<>();
    private List<ChargingStationBean> csAdsList = new ArrayList<>();
    private BusinessBean business = new BusinessBean();

    public void nearby(Integer range, Scanner input){
        BusinessController bc = new BusinessController();
        String csid;


        try {
            chargingStationList = ChargingStationController.getNearby(range); //radius range 1 to 50000
            int i;
            for(i=0; i < chargingStationList.size(); i++){
                Long distance = MapController.getDistance(MapController.getCoordinates((business.getAddress())), MapController.getCoordinates(chargingStationList.get(i).getFreeformAddress()));
                System.out.println(i+1+". "+chargingStationList.get(i).getName()+", "+chargingStationList.get(i).getFreeformAddress() + " distance from  your business: "+ distance);
            }
        } catch (IOException | ParseException | LocationNotFoundException | java.text.ParseException | ChargingStationNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(EC);
        System.out.println("--------"+ ub.getUsername() +"--------");
        System.out.println(W);
        System.out.println("1. Buy an ad");
        System.out.println("2. Back");
        do{

            switch (input.nextLine()) {
                case "1" -> {
                    System.out.println("Insert the charging station number: ");
                    int num = Integer.parseInt(input.nextLine());
                    if(num < chargingStationList.size() && num >0){
                        csid = chargingStationList.get(num-1).getId();
                    }else{
                        System.out.println("Wrong charging station number");
                        return;
                    }

                    List<ChargingStationBean> chargingStationAds = BusinessController.getBusinessAds(business.getId());
                    int j;
                    for(j=0; j < Objects.requireNonNull(chargingStationAds).size(); j++){
                        if(Objects.equals(chargingStationAds.get(j).getId(), csid)){
                            System.out.println("You already bought an ad for this charging station.");
                            return;
                        }
                    }

                    System.out.println("---PayPal Payment---");
                    System.out.println("username: ");
                    input.nextLine();
                    System.out.println("password: ");
                    input.nextLine();
                    System.out.println("Confirm payment? (y/n)");
                    if(Objects.equals(input.nextLine(), "y")){
                        bc.businessAd(business.getId(), csid);
                        System.out.println("Thank you!");
                    }else{
                        System.out.println("Payment cancelled!");
                    }
                    return;
                }
                case "2" -> {
                    return;
                }
                default -> System.out.println(CNF);
            }
            System.out.flush();
        }while(input.hasNext());
    }
    void print(){
        System.out.println(EC);
        System.out.println("--------Welcome "+ ub.getUsername() + "!--------");
        System.out.println(W);
        System.out.println("1. Search for Nearby Charging Stations");
        System.out.println("2. My charging stations ads");
        System.out.println("3. Business Settings");
        System.out.println("4. Logout");
        System.out.println("5. Exit");
    }

    public void init(){
        Scanner input = new Scanner(System.in);
        business = BusinessController.getBusiness(ub.getUsername());
        do {
            print();

            switch (input.nextLine()) {
                case "1" -> {
                    System.out.println("Insert a search range in km: ");
                    this.nearby(Integer.parseInt(input.nextLine())*1000, input);
                    this.print();
                }
                case "2" -> {
                    csAdsList = BusinessController.getBusinessAds(business.getId());
                    int i;
                    for(i=0; i < Objects.requireNonNull(csAdsList).size(); i++){
                        ChargingStationBean cs = new ChargingStationBean();
                        try {
                            cs = ChargingStationController.getCSInfo(csAdsList.get(i).getId());
                        } catch (IOException | java.text.ParseException | LocationNotFoundException | ParseException |
                                 ChargingStationNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println(i+1+". "+cs.getName()+" "+cs.getFreeformAddress()+"\n     ");
                    }

                }
                case "3" -> {
                    CLIBusinessSettingsController bsc = new CLIBusinessSettingsController();
                    bsc.init();
                }
                case "4" -> {
                    System.out.println(RED + "Logged out" + RESET);
                    SessionUser.getInstance().closeSession();
                    CLIHomeController hc = new CLIHomeController();
                    hc.init();
                }
                case "5" -> {
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
