package it.ecteam.easycharge.dao;

import it.ecteam.easycharge.entity.ChargingStation;
import it.ecteam.easycharge.entity.User;
import it.ecteam.easycharge.utils.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserDao extends DaoTemplate {

    public Boolean createUser(String username, String password, String email, String role, String car) {
        return this.execute(() -> {
            Connection con = DataBaseConnection.getConnection();
            String sql = "call easycharge.add_user(?, ?, ?, ?, ?);\r\n";


                try (PreparedStatement stm = con.prepareStatement(sql)) {
                    stm.setString(1, username);
                    stm.setString(2, email);
                    stm.setString(3, password);
                    stm.setString(4, role);
                    stm.setString(5, car);
                    stm.executeUpdate();
                }

            return true;
            }) != null;
            }

    public User findUser(String username, String password) {
        return this.execute(new DaoAction<User>() {
            @Override
            public User act() throws ClassNotFoundException, SQLException {
                Connection conn = null;
                User u = null;
                conn = DataBaseConnection.getConnection();

                String sql = "call easycharge.login(?, ?);\r\n";
                try(PreparedStatement stm = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE)){
                    stm.setString(1, username);
                    stm.setString(2, password);
                    try (ResultSet rs = stm.executeQuery()) {

                        if (!rs.first()) // rs not empty
                            return null;

                        boolean moreThanOne = rs.first() && rs.next();
                        assert !moreThanOne;
                        rs.first();

                        String role = rs.getString("role");
                        String usernameLoaded = rs.getString("username");

                        if(usernameLoaded.equals(username)) {
                            u = new User(usernameLoaded, role);
                        }
                    }
                }

                return u;
            }
        });
    }

    public Boolean addFavorite(String username, String csid) {
        return this.execute(() -> {
            Connection con = DataBaseConnection.getConnection();
            String sql = "call easycharge.set_favorite(?, ?);\r\n";


            try (PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setString(1, username);
                stm.setString(2, csid);
                stm.executeUpdate();
            }

            return true;
        }) != null;
    }

    public Boolean removeFavorite(String username, String csid) {
        return this.execute(() -> {
            Connection con = DataBaseConnection.getConnection();
            String sql = "call easycharge.remove_favorite(?, ?);\r\n";


            try (PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setString(1, username);
                stm.setString(2, csid);
                stm.executeUpdate();
            }

            return true;
        }) != null;
    }

    public List<ChargingStation> getFavorite(String username){
        List<ChargingStation> ret = this.execute(new DaoAction<List<ChargingStation>>() {
            @Override
            public List<ChargingStation> act() throws ClassNotFoundException, SQLException {
                Connection conn = null;
                String sql = null;
                PreparedStatement stm = null;
                List<ChargingStation> cs = new ArrayList<>();
                try {
                    conn = DataBaseConnection.getConnection();
                    sql = "call easycharge.get_favorite(?);\r\n";
                    stm = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
                    stm.setString(1, username);
                    try (ResultSet rs = stm.executeQuery()) {

                            if (!rs.first()) // rs not empty
                                return Collections.emptyList();	//return empty list*/

                            do{
                                String stationLoaded = rs.getString("charging_station_idcharging_station");
                                cs.add(new ChargingStation(stationLoaded));
                            } while (rs.next());
                        }
                } finally {
                    assert stm != null;
                    stm.close();
                }
                return cs;
            }
        });
        if (ret != null)
            return ret;

        return Collections.emptyList();
    }

    public Boolean changeUserCar(String username, String model) {
        return this.execute(() -> {
            Connection con = DataBaseConnection.getConnection();
            String sql = "call easycharge.change_car(?, ?);\r\n";


            try (PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setString(1, username);
                stm.setString(2, model);
                stm.executeUpdate();
            }

            return true;
        }) != null;
    }
    }

