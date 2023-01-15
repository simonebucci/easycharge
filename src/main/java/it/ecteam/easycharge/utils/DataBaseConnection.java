package it.ecteam.easycharge.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBaseConnection {
    private static Connection databaseLink;
    protected static final Logger logger = Logger.getLogger("db");

    public static Connection getConnection(){

        String databaseName = "easycharge";
        String databaseUser = "root";
        String databasePassword = System.getProperty("database.password");
        String url = "jdbc:mysql://localhost/" + databaseName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        }catch (Exception e){
            logger.log(Level.WARNING, (Supplier<String>) e);
        }

        return databaseLink;
    }
}
