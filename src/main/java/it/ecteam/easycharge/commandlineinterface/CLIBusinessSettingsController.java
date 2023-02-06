package it.ecteam.easycharge.commandlineinterface;

import it.ecteam.easycharge.bean.BusinessBean;
import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.controller.BusinessController;
import it.ecteam.easycharge.utils.SessionUser;
import java.util.Scanner;

import static it.ecteam.easycharge.commandlineinterface.CommandLineInterface.RED;
import static it.ecteam.easycharge.commandlineinterface.CommandLineInterface.RESET;

public class CLIBusinessSettingsController {
    private UserBean ub = SessionUser.getInstance().getSession();
    private BusinessBean business = new BusinessBean();
    public void print(){
        System.out.println("----------EasyCharge----------");
        System.out.println("--------"+ ub.getUsername() +"--------");
        System.out.println("----What can I do for you?----");
        System.out.println("1. Edit ad");
        System.out.println("2. Back");
        System.out.println("3. Exit");
    }

    public void init(){
        Scanner input = new Scanner(System.in);
        business = BusinessController.getBusiness(ub.getUsername());

        print();
        do {
            switch (input.nextLine()) {
                case "1" -> {
                    System.out.println("Insert your ad phrase:");
                    BusinessController bc = new BusinessController();
                    bc.modifyAd(business.getId(), input.nextLine());
                    System.out.println("Ad updated!");
                    print();
                }
                case "2" -> {
                    CLIBusinessHomeController bhc = new CLIBusinessHomeController();
                    bhc.init();
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
