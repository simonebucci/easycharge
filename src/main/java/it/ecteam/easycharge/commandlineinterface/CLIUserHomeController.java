package it.ecteam.easycharge.commandlineinterface;

import it.ecteam.easycharge.bean.*;
import it.ecteam.easycharge.controller.*;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import it.ecteam.easycharge.utils.SessionUser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Date;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.ecteam.easycharge.commandlineinterface.CommandLineInterface.*;

public class CLIUserHomeController {
    private UserBean ub = SessionUser.getInstance().getSession();
    private List<ChargingStationBean> chargingStationList = new ArrayList<>();
    private List<ConnectorBean> connectorBeanList = new ArrayList<>();
    private List<ChargingStationBean> favoriteCSB = UserController.getFavorite(ub.getUsername());
    private List<ReportBean> report = new ArrayList<>();
    private List<BusinessBean> chargingStationAds = new ArrayList<>();
    private ReportController reportController = new ReportController();
    private final SecureRandom r = new SecureRandom();
    public static final String WE = "--------Welcome ";
    public static final String WE2 = "!--------";
    protected static final Logger logger = Logger.getLogger("CLI");
    public void nearby(Integer range, Scanner input){
        String csid;

        try {
            chargingStationList = ChargingStationController.getNearby(range); //radius range 1 to 50000
            printChargingStation(chargingStationList);
        } catch (IOException | ParseException | LocationNotFoundException | java.text.ParseException | ChargingStationNotFoundException e) {
            logger.log(Level.WARNING, e.toString());
        }
        System.out.println(EC);
        System.out.println(WE+ ub.getUsername() + WE2);
        System.out.println(W);
        System.out.println(CA);
        System.out.println("2. Report");
        System.out.println("3. Add to favorite");
        System.out.println("4. Filter by my connector");
        System.out.println("5. Back");
        do{

            switch (input.nextLine()) {
                case "1" -> {
                    System.out.println(INSERT);
                    int num = Integer.parseInt(input.nextLine());
                    if(num < chargingStationList.size() && num >0){
                        csid = chargingStationList.get(num-1).getId();
                        try {
                            connectorBeanList = ChargingStationController.getChargingAvailability(csid);
                        } catch (IOException | ChargingStationNotFoundException | ParseException e) {
                            logger.log(Level.WARNING, e.toString());
                        }
                        printConnector(connectorBeanList);
                        chargingStationAds = BusinessController.getCSAds(csid);
                        if (!chargingStationAds.isEmpty()) {
                            int rand = r.nextInt((chargingStationAds).size());
                            System.out.println(chargingStationAds.get(rand).getAd()+" located in " + chargingStationAds.get(rand).getAddress());
                        }
                    }else{
                        System.out.println("Wrong charging station number");
                        return;
                    }
                    System.out.println(EC);
                    System.out.println(WE+ ub.getUsername() + WE2);
                    System.out.println(W);
                    System.out.println(CA);
                    System.out.println("2. Report");
                    System.out.println("3. Add to favorite");
                    System.out.println("4. Back");
                }
                case "2" -> {
                    System.out.println(INSERT);
                    int num = Integer.parseInt(input.nextLine());
                    report = ReportController.getUserReport(String.valueOf(chargingStationList.get(num - 1).getId()));
                    printReport(input, num);
                    System.out.print("Do you want to write a report? (y/n)");
                    if(Objects.equals(input.nextLine(), "y")){
                        System.out.println("Write your report: ");
                        reportController.reportCS(chargingStationList.get(num-1).getId(), ub.getUsername(), input.nextLine());
                        System.out.println("Report sent, thank you!");
                    }
                    return;
                }
                case "3" -> {
                    UserController uc = new UserController();
                    System.out.println(INSERT);
                    int num = Integer.parseInt(input.nextLine());
                    uc.setFavorite(ub.getUsername(), chargingStationList.get(num-1).getId());
                    System.out.println("Charging station added to your favorites!");
                    favoriteCSB = UserController.getFavorite(ub.getUsername());
                    return;
                }
                case "4" -> {
                    UserController uc = new UserController();
                    CarBean cb = uc.getCar(ub.getUsername());
                    connectorFilter(cb);
                    System.out.println(EC);
                    System.out.println(WE+ ub.getUsername() + WE2);
                    System.out.println(W);
                    System.out.println(CA);
                    System.out.println("2. Back");
                    filterCase(input);
                    return;
                }
                case "5" -> {
                    return;
                }
                default -> System.out.println(CNF);
            }
            System.out.flush();
        }while(input.hasNext());
    }

    public void favorites(Scanner input){
        ChargingStationBean csb = new ChargingStationBean();
        int i;
        for(i=0; i<favoriteCSB.size(); i++){
            try {
                csb = ChargingStationController.getCSInfo(favoriteCSB.get(i).getId());
            } catch (IOException | java.text.ParseException | LocationNotFoundException | ParseException |
                     ChargingStationNotFoundException e) {
                logger.log(Level.WARNING, e.toString());
            }
            System.out.println(i+1+". "+csb.getName()+", "+csb.getFreeformAddress());
        }

        System.out.println(EC);
        System.out.println(WE+ ub.getUsername() + WE2);
        System.out.println(W);
        System.out.println(CA);
        System.out.println("2. Remove favorite");
        System.out.println("3. Back");
        do {
            switch (input.nextLine()) {
                case "1" -> {
                    System.out.println(INSERT);
                    int num = Integer.parseInt(input.nextLine());
                    try {
                        connectorBeanList = ChargingStationController.getChargingAvailability(favoriteCSB.get(num-1).getId());
                    } catch (IOException | ChargingStationNotFoundException | ParseException e) {
                        logger.log(Level.WARNING, e.toString());
                    }
                    printConnector(connectorBeanList);
                    return;
                }
                case "2" -> {
                    UserController uc = new UserController();
                    System.out.println(INSERT);
                    int num = Integer.parseInt(input.nextLine());
                    uc.deleteFavorite(ub.getUsername(), favoriteCSB.get(num-1).getId());
                    System.out.println("Charging station removed from your favorites!");
                    favoriteCSB = UserController.getFavorite(ub.getUsername());
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

    private void reportLike(Scanner input, int num){
        String author;
        if(Objects.equals(input.nextLine(), "y")){
            System.out.println("Insert report number: ");
            int repnum = Integer.parseInt(input.nextLine());
            List<ReportBean> rb = ReportController.getPointGiver(report.get(repnum-1).getUsername(), chargingStationList.get(num-1).getId(), (Date) report.get(repnum-1).getDate());
            author = report.get(repnum-1).getUsername();
            int k;
            int verify = 0;
            for(k=0; k<rb.size(); k++){
                if(Objects.equals(rb.get(k).getUsername(), ub.getUsername())){
                    verify = 1;
                }
            }
            if (Objects.equals(ub.getUsername(), author)) {
                System.out.println("You can't give a like to your self.");
            } else if(verify == 1){
                System.out.println("You already gave a like to this report.");
            }
            else{
                reportController.givePointToUser(author, chargingStationList.get(num-1).getId(), (Date) report.get(repnum-1).getDate(), ub.getUsername());
                System.out.println("You liked the selected report!");
            }
        }
    }

    private void printConnector(List<ConnectorBean> connectorBeanList){
        int k;
        for(k=0; k < connectorBeanList.size(); k++) {
            System.out.println(TYPE+ connectorBeanList.get(k).getType() + TOTAL + connectorBeanList.get(k).getTotal() + AVAILABLE + connectorBeanList.get(k).getAvailable() + OCCUPIED + connectorBeanList.get(k).getOccupied() + RESERVED + connectorBeanList.get(k).getReserved() + UNKNOWN + connectorBeanList.get(k).getUnknown() + OOS + connectorBeanList.get(k).getOutOfService() + SPACE);
        }
    }

    private void connectorFilter(CarBean cb){
        for (int i = 0; i < chargingStationList.size(); i++) {
            try {
                connectorBeanList = ChargingStationController.getChargingAvailability(chargingStationList.get(i).getId());
            } catch (IOException | ParseException | ChargingStationNotFoundException e) {
                logger.log(Level.WARNING, e.toString());
            }
            for(int k = 0; k < connectorBeanList.size(); k++) {
                if (Objects.equals(connectorBeanList.get(k).getType(), "Chademo")) {
                    System.out.println(i + 1 + ". " + chargingStationList.get(i).getName() + " " + chargingStationList.get(i).getFreeformAddress());
                    k = connectorBeanList.size();
                } else if (Objects.equals(connectorBeanList.get(k).getType().substring(0, 13), cb.getConnectorType().substring(0, 13))) {
                    System.out.println(i + 1 + ". " + chargingStationList.get(i).getName() + " " + chargingStationList.get(i).getFreeformAddress());
                    k = connectorBeanList.size();
                }
            }
        }
    }

    private void printReport(Scanner input, int num){
        if(!report.isEmpty()){
            int j;
            for(j=0; j < report.size(); j++) {
                System.out.println(j+1+". "+report.get(j).getUsername() + "\nsaid: " + report.get(j).getComment() + "\nDate: " + report.get(j).getDate()+ "\nLikes: " + report.get(j).getPoint());
            }
            System.out.println("Do you want to like a report? (y/n)");
            reportLike(input, num);
        }else{
            System.out.println("No report found.");
        }
    }

    private void printChargingStation(List<ChargingStationBean> chargingStationList){
        int i;
        for(i=0; i < chargingStationList.size(); i++){
            System.out.println(i+1+". "+chargingStationList.get(i).getName()+", "+chargingStationList.get(i).getFreeformAddress());
        }
    }

    private void filterCase(Scanner input){
        do {
            switch (input.nextLine()) {
                case "1" -> {
                    System.out.println(INSERT);
                    int num = Integer.parseInt(input.nextLine());
                    try {
                        connectorBeanList = ChargingStationController.getChargingAvailability(favoriteCSB.get(num-1).getId());
                    } catch (IOException | ChargingStationNotFoundException | ParseException e) {
                        logger.log(Level.WARNING, e.toString());
                    }
                    printConnector(connectorBeanList);
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
    private void print(){
        System.out.println(EC);
        System.out.println(WE+ ub.getUsername() + WE2);
        System.out.println(W);
        System.out.println("1. Search for Nearby Charging Stations");
        System.out.println("2. Route");
        System.out.println("3. My favorites");
        System.out.println("4. User settings");
        System.out.println("5. Logout");
        System.out.println("6. Exit");
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
                    CLIRouteController rc = new CLIRouteController();
                    rc.init();
                }
                case "3" -> {
                    favorites(input);
                    print();
                }
                case "4" -> {
                    CLIUserSettingsController usc = new CLIUserSettingsController();
                    usc.init();
                }
                case "5" -> {
                    SessionUser.getInstance().closeSession();
                    System.out.println(RED + "Logged out" + RESET);
                    CLIHomeController hc = new CLIHomeController();
                    hc.init();
                }
                case "6" -> {
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
