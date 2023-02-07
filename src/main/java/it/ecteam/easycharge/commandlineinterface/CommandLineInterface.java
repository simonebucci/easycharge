package it.ecteam.easycharge.commandlineinterface;

public class CommandLineInterface {

    public static final String RESET = "\033[0m";  // TEXT RESET
    public static final String RED = "\033[0;31m"; // RED
    public static final String GREEN = "\u001B[32m"; //GREEN
    public static final String BOLD = "\033[0;1m"; // BOLD
    public static final String SPACE = "\n     ";
    public static final String TYPE = "Type:";
    public static final String TOTAL = "\nTotal: ";
    public static final String AVAILABLE = "\nAvailable: ";
    public static final String RESERVED = "\nReserved: ";
    public static final String OCCUPIED = "\nOccupied: ";
    public static final String UNKNOWN = "\nUnknown: ";
    public static final String OOS = "\nOutOfService: ";
    public static final  String EC = "----------EasyCharge----------";
    public static final  String G = "------------Guest-------------";
    public static final  String W = "----What can I do for you?----";
    public static final String CA = "1. Charging availability";
    public static final String INSERT ="Insert the charging station number: ";
    public static final String CNF = "Command not found\n";
    public static void main(String[] args){
        CLIHomeController home = new CLIHomeController();
        home.init();
    }
}
