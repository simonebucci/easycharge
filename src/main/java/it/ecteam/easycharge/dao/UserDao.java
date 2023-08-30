package it.ecteam.easycharge.dao;

import it.ecteam.easycharge.entity.ChargingStation;
import it.ecteam.easycharge.entity.User;
import it.ecteam.easycharge.utils.DataBaseConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;

public class UserDao extends DaoTemplate {

    protected static final String PATH_FAVORITE = System.getProperty("path_favorite");
    protected static final String PATH_USER = System.getProperty("path_user");
    protected static final String USERNAME = "username";
    protected static final String CSID = "csid";
    protected static final String EMAIL = "email";
    protected static final String PASSWORD = "password";
    protected static final String ROLE = "role";
    protected static final String CAR = "car";
    protected static final String FAVORITE = "favorite";

    //create a user sending data on db
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

            //Save on File System
            JSONObject o;
            JSONArray arr;
            Map<String, String> jsonMap;

            o = this.openFile(PATH_USER);

            arr = (JSONArray) o.get("user");

            jsonMap = new HashMap<>();
            jsonMap.put(USERNAME, username);
            jsonMap.put(EMAIL, email);
            jsonMap.put(PASSWORD, password);
            jsonMap.put(ROLE, role);
            jsonMap.put(CAR, car);

            JSONObject newUser = new JSONObject(jsonMap);

            arr.add(newUser);

            this.writeOnFile(PATH_USER, o);

            return true;
            }) != null;
            }

    //search a user on db for login
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

                        String role = rs.getString(ROLE);
                        String usernameLoaded = rs.getString(USERNAME);

                        if(usernameLoaded.equals(username)) {
                            u = new User(usernameLoaded, role);
                        }
                    }
                }

                //FileSystem
                JSONParser parser = new JSONParser();

                try (FileReader fileReader = new FileReader(PATH_USER)) {

                    JSONObject o = (JSONObject) parser.parse(fileReader);
                    JSONArray arr = (JSONArray) o.get("user");
                    if (arr.isEmpty()) {
                        return u;
                    }

                    for (int index = 0; index < arr.size(); index++) {

                        JSONObject object = (JSONObject) arr.get(index);

                        if (object.get(USERNAME).equals(username) && object.get(PASSWORD).equals(password)) {

                            //System.out.println((String) object.get(USERNAME));
                            //System.out.println((String) object.get(ROLE));
                        }

                    }
                } catch (IOException | org.json.simple.parser.ParseException e) {
                    logger.log(Level.WARNING, e.toString());
                }

                return u;
            }
        });
    }

    //add a favorite cs for a user
    public Boolean addFavorite(String username, String csid) {
        return this.execute(() -> {
            Connection con = DataBaseConnection.getConnection();
            String sql = "call easycharge.set_favorite(?, ?);\r\n";


            try (PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setString(1, username);
                stm.setString(2, csid);
                stm.executeUpdate();
            }

            //Save on File System
            JSONObject o;
            JSONArray arr;
            Map<String, String> jsonMap;

            o = this.openFile(PATH_FAVORITE);

            arr = (JSONArray) o.get(FAVORITE);

            jsonMap = new HashMap<>();
            jsonMap.put(USERNAME, username);
            jsonMap.put(CSID, csid);

            JSONObject newFav = new JSONObject(jsonMap);

            arr.add(newFav);

            this.writeOnFile(PATH_FAVORITE, o);

            return true;
        }) != null;
    }

    //remove a favorite cs for a user
    public Boolean removeFavorite(String username, String csid) {
        return this.execute(() -> {
            Connection con = DataBaseConnection.getConnection();
            String sql = "call easycharge.remove_favorite(?, ?);\r\n";


            try (PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setString(1, username);
                stm.setString(2, csid);
                stm.executeUpdate();
            }

            //Remove from File System
            JSONObject o = this.openFile(PATH_FAVORITE);
            JSONArray arr = (JSONArray) o.get(FAVORITE);

            for (int index = 0; index < arr.size(); index++) {

                JSONObject object = (JSONObject) arr.get(index);

                if (object.get(USERNAME).equals(username) && object.get(CSID).equals(csid)) {

                    arr.remove(object);

                    this.writeOnFile(PATH_FAVORITE, o);

                }
            }

            return true;
        }) != null;
    }

    //get the user's favorites charging stations
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

        //FileSystem
        JSONParser parser = new JSONParser();

        try (FileReader fileReader = new FileReader(PATH_FAVORITE)) {

            JSONObject o = (JSONObject) parser.parse(fileReader);
            JSONArray arr = (JSONArray) o.get(FAVORITE);
            if (arr.isEmpty()) {
                return Collections.emptyList();
            }

            for (int index = 0; index < arr.size(); index++) {

                JSONObject object = (JSONObject) arr.get(index);

                if (object.get(USERNAME).equals(username)) {

                    //System.out.println((String) object.get(CSID));
                }

            }
        } catch (IOException | org.json.simple.parser.ParseException e) {
            logger.log(Level.WARNING, e.toString());
        }

        if (ret != null)
            return ret;

        return Collections.emptyList();
    }

    //change the car model of a user
    public Boolean changeUserCar(String username, String model) {
        return this.execute(() -> {
            Connection con = DataBaseConnection.getConnection();
            String sql = "call easycharge.change_car(?, ?);\r\n";


            try (PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setString(1, username);
                stm.setString(2, model);
                stm.executeUpdate();
            }

            //FileSystem
            JSONParser parser = new JSONParser();

            try (FileReader fileReader = new FileReader(PATH_USER)) {

                JSONObject o = (JSONObject) parser.parse(fileReader);
                JSONArray arr = (JSONArray) o.get("user");
                if (arr.isEmpty()) {
                    return null;
                }

                for (int index = 0; index < arr.size(); index++) {

                    JSONObject object = (JSONObject) arr.get(index);

                    if (object.get(USERNAME).equals(username)) {

                        object.put(CAR, model);
                    }

                }
                try (FileWriter fileWriter = new FileWriter(PATH_USER)) {
                    fileWriter.write(o.toJSONString());
                    fileWriter.flush();
                } catch (IOException e) {
                    logger.log(Level.WARNING, e.toString());
                }
            } catch (IOException | org.json.simple.parser.ParseException e) {
                logger.log(Level.WARNING, e.toString());
            }

            return true;
        }) != null;

    }
    }

