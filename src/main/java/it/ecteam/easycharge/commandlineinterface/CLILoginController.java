package it.ecteam.easycharge.commandlineinterface;

import it.ecteam.easycharge.bean.CarBean;
import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.controller.LoginController;
import it.ecteam.easycharge.exceptions.EmptyFieldException;
import it.ecteam.easycharge.utils.SessionUser;
import java.util.List;
import java.util.Scanner;
import static it.ecteam.easycharge.commandlineinterface.CommandLineInterface.*;


public class CLILoginController {
    static final String B = "business";
    public void init() {
        Scanner input = new Scanner(System.in);
        menu(input);
    }

    private void menu(Scanner input){
        do {
            System.out.println("-----------EasyCharge------------");
            System.out.println("--------Login/Signup Page--------");
            System.out.println("---------------------------------");
            System.out.println("1. Login");
            System.out.println("2. SignUp");
            System.out.println("3. Back");

            switch (input.nextLine()) {
                case "1" -> {
                    this.login(input);
                }
                case "2" -> {
                    this.register(input);
                }
                case "3" -> {
                    CLIHomeController chc = new CLIHomeController();
                    chc.init();
                    return;
                }
                default -> System.out.println("Command not found\n");
            }
            System.out.flush();

        } while (input.hasNext());
    }

    private void login(Scanner input) {
        System.out.println("---Login into your account---");

        System.out.println("Username:");
        String username = input.nextLine();
        System.out.flush();

        System.out.println("Password:");
        String password = input.nextLine();
        System.out.flush();

        UserBean ub = new UserBean();
        ub.setUsername(username);
        ub.setPassword(password);

        LoginController loginController = new LoginController();
        UserBean gu;
        try {
            gu = loginController.login(ub);
            if (gu == null) {
                System.out.println(RED + "LOGIN FAILED!" + RESET);
                menu(input);
            } else {
                String role = gu.getRole();

                //SET SESSION USER
                SessionUser su = SessionUser.getInstance();
                su.setSession(gu);

                switch (role) {
                    case "user" -> {
                        CLIUserHomeController uhc = new CLIUserHomeController();
                        uhc.init();
                    }
                    case B -> {
                        CLIBusinessHomeController bhc = new CLIBusinessHomeController();
                        bhc.init();
                    }
                    default -> System.out.println("Something went wrong, try again!\n");
                }
            }

        } catch (EmptyFieldException e) {
            System.out.println("\n" + RED + e.getMessage() + RESET + "\n");
        }
    }

    private void register(Scanner input) {
        UserBean ub = new UserBean();
        boolean regResult;

        System.out.println("---Register a new account---");

        System.out.println("Username:");
        String username = input.nextLine();
        System.out.flush();

        System.out.println("Email:");
        String email = input.nextLine();
        System.out.flush();

        System.out.println("Password:");
        String password = input.nextLine();
        System.out.flush();

        System.out.println("Role:");
        System.out.println("1. Normal User");
        System.out.println("2. Business User");
        switch (input.nextLine()) {
            case "1" -> {
                System.out.println("Car model:");
                List<CarBean> cb = LoginController.getCar();
                int i;
                for(i=0; i<cb.size(); i++){
                    System.out.println(i+1+". "+cb.get(i).getName());
                }
                String car = input.nextLine();

                System.out.flush();
                ub.setRole("user");
            }
            case "2" -> {
                System.out.println("Business name:");
                String bname = input.nextLine();
                System.out.flush();
                System.out.println("Business address:");
                String baddress = input.nextLine();
                System.out.flush();
                ub.setRole(B);
            }
            default -> System.out.println("Role not found\n");
        }
        System.out.flush();


        ub.setUsername(username);
        ub.setPassword(password);
        ub.setEmail(email);


        LoginController loginController = new LoginController();
        regResult = loginController.createUser(ub);
        if (!regResult) {
            System.out.println(RED + "REGISTRATION FAILED!" + RESET);
            menu(input);
        } else {
            String role = ub.getRole();

            //SET SESSION USER
            SessionUser su = SessionUser.getInstance();
            su.setSession(ub);

            switch (role) {
                case "user" -> {
                    CLIUserHomeController uhc = new CLIUserHomeController();
                    uhc.init();
                }
                case B -> {
                    CLIBusinessHomeController bhc = new CLIBusinessHomeController();
                    bhc.init();
                }
                default -> System.out.println("Something went wrong, try again!\n");
            }
        }

    }
}
