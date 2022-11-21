package it.ecteam.easycharge.dao;

import it.ecteam.easycharge.entity.Car;
import it.ecteam.easycharge.utils.DataBaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

                        String usernameLoaded = rs.getString("user_username");
                        String name = rs.getString("car_model");
                        String cType = rs.getString("connector_type");
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


}