package it.ecteam.easycharge.commandlineinterface;

import it.ecteam.easycharge.bean.CarBean;
import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.controller.LoginController;
import it.ecteam.easycharge.controller.UserController;
import it.ecteam.easycharge.utils.SessionUser;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static it.ecteam.easycharge.commandlineinterface.CommandLineInterface.RED;
import static it.ecteam.easycharge.commandlineinterface.CommandLineInterface.RESET;

public class CLIUserSettingsController {
    private UserBean ub = SessionUser.getInstance().getSession();

    public void print(){
        System.out.println("----------EasyCharge----------");
        System.out.println("--------"+ ub.getUsername() +"--------");
        System.out.println("----What can I do for you?----");
        System.out.println("1. Change my car");
        System.out.println("2. Back");
        System.out.println("3. Exit");
    }

    public void init(){
        Scanner input = new Scanner(System.in);
        UserController uc = new UserController();
        print();
        do {
            switch (input.nextLine()) {
                case "1" -> {
                    System.out.println("Car model:");
                    List<CarBean> cb = LoginController.getCar();
                    int i;
                    for(i=0; i< Objects.requireNonNull(cb).size(); i++){
                        System.out.println(i+1+". "+cb.get(i).getName());
                    }
                    int car = Integer.parseInt(input.nextLine());
                    ub.setCar(cb.get(car-1).getName());
                    uc.changeCar(ub.getUsername(), ub.getCar());
                    System.out.println("Your car model has been changed!");
                    print();
                }
                case "2" -> {
                    CLIUserHomeController uhc = new CLIUserHomeController();
                    uhc.init();
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
