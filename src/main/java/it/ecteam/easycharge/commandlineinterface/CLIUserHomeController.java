package it.ecteam.easycharge.commandlineinterface;

import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.utils.SessionUser;
import it.ecteam.easycharge.viewcontroller.UserGraphicChange;

import java.util.Scanner;

public class CLIUserHomeController {
    private UserBean ub = SessionUser.getInstance().getSession();

    public void init(){
        Scanner input = new Scanner(System.in);
        do {
            System.out.println("----------EasyCharge----------");
            System.out.println("--------Welcome "+ ub.getUsername() + "!--------");
            System.out.println("----What can I do for you?----");
            System.out.println("1. Search for Nearby Charging Stations");
            System.out.println("2. Login/SignUp");
            System.out.println("3. Exit");

            switch (input.nextLine()) {
                case "1" -> {

                }
                case "2" -> {
                    CLILoginController lc = new CLILoginController();
                    lc.init();
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
