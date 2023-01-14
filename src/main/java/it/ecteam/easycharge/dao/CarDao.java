package it.ecteam.easycharge.dao;

import it.ecteam.easycharge.entity.Car;
import it.ecteam.easycharge.utils.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarDao extends DaoTemplate{

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

                        String usernameLoaded = rs.getString("username");
                        String name = rs.getString("car_model");
                        String cType = rs.getString("connector_connectorType");
                        String capacity = rs.getString("capacity");
                        String range = rs.getString("rnge");

                        if(usernameLoaded.equals(username)) {
                            car = new Car(name, capacity, range, cType);
                        }
                    }
                }

                return car;
            }
        });
    }

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
                            String model = rs.getString("car_model");
                            car.add(new Car(model));
                        } while (rs.next());
                    }
                } finally {
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
