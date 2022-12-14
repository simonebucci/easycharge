package it.ecteam.easycharge.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseConnection {
    private static Connection databaseLink;

    public static Connection getConnection(){
        String databaseName = "easycharge";
        String databaseUser = "root";
        String databasePassword = "spillo";
        String url = "jdbc:mysql://localhost/" + databaseName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        }catch (Exception e){
            e.printStackTrace();
        }

        return databaseLink;
    }
}
