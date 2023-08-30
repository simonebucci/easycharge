package it.ecteam.easycharge.dao;

import it.ecteam.easycharge.entity.Car;
import it.ecteam.easycharge.utils.DataBaseConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class CarDao extends DaoTemplate{

    protected static final String PATH_USER = System.getProperty("path_user");
    protected static final String PATH_CAR = System.getProperty("path_car");
    protected static final String USERNAME = "username";
    protected static final String CAR = "car_model";
    protected static final String CTYPE = "connector_connectorType";
    protected static final String CAP = "capacity";
    protected static final String RANGE = "rnge";

    //get a user's car
    public Car getCar(String username){
        return this.execute(new DaoAction<Car>() {
            @Override
            public Car act() throws ClassNotFoundException, SQLException {
                Connection conn = null;
                Car car = null;
                conn = DataBaseConnection.getConnection();

                String sql = "call easycharge.get_car(?);\r\n";
                try(PreparedStatement stm = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE)){
                    stm.setString(1, username);
                    try (ResultSet rs = stm.executeQuery()) {

                        if (!rs.first()) // rs not empty
                            return null;

                        boolean moreThanOne = rs.first() && rs.next();
                        assert !moreThanOne;
                        rs.first();

                        String usernameLoaded = rs.getString(USERNAME);
                        String name = rs.getString(CAR);
                        String cType = rs.getString(CTYPE);
                        String capacity = rs.getString(CAP);
                        String range = rs.getString(RANGE);

                        if(usernameLoaded.equals(username)) {
                            car = new Car(name, capacity, range, cType);
                        }
                    }
                }

                getCarFS(username);

                return car;
            }
        });
    }

    private Object getCarFS(String username){
        //FileSystem
        JSONParser parser = new JSONParser();
        String model=null;
        try (FileReader fileReader = new FileReader(PATH_USER)) {

            JSONObject o = (JSONObject) parser.parse(fileReader);
            JSONArray arr = (JSONArray) o.get("user");
            if (arr.isEmpty()) {
                return null;
            }

            for (int index = 0; index < arr.size(); index++) {

                JSONObject object = (JSONObject) arr.get(index);

                if (object.get(USERNAME).equals(username)) {
                    model =(String) object.get(CAR);
                }

            }
        } catch (IOException | org.json.simple.parser.ParseException e) {
            logger.log(Level.WARNING, e.toString());
        }

        try (FileReader fileReader = new FileReader(PATH_CAR)) {

            JSONObject o = (JSONObject) parser.parse(fileReader);
            JSONArray arr = (JSONArray) o.get("car");
            if (arr.isEmpty()) {
                return null;
            }

            for (int index = 0; index < arr.size(); index++) {

                JSONObject object = (JSONObject) arr.get(index);

                if (object.get(CAR).equals(model)) {
                    //OUTPUT removed for sonarcloud
                }

            }
        } catch (IOException | org.json.simple.parser.ParseException e) {
            logger.log(Level.WARNING, e.toString());
        }
        return null;
    }

    //get the car models list available on the db
    public List<Car> getModel(){
        List <Car> ret = this.execute(new DaoAction<List<Car>>() {
            @Override
            public List<Car> act() throws ClassNotFoundException, SQLException {
                Connection conn = null;
                String sql = null;
                PreparedStatement stm = null;
                List<Car> car = new ArrayList<>();
                try {
                    conn = DataBaseConnection.getConnection();
                    sql = "call easycharge.get_model();\r\n";
                    stm = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);

                    try (ResultSet rs = stm.executeQuery()) {

                        if (!rs.first()) // rs not empty
                            return Collections.emptyList();	//return empty list
                        do{
                            String model = rs.getString(CAR);
                            car.add(new Car(model));
                        } while (rs.next());
                    }
                } finally {
                    assert stm != null;
                    stm.close();
                }
                return car;
            }
        });
        if (ret != null)
            return ret;

        return Collections.emptyList();
    }


}
