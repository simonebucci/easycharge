package it.ecteam.easycharge.dao;

import it.ecteam.easycharge.entity.User;
import it.ecteam.easycharge.utils.DataBaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao extends DaoTemplate {

    public Boolean createUser(String username, String password, String email) {
        return this.execute(() -> {
            Connection con = DataBaseConnection.getConnection();
            String sql = "call easycharge.add_user(?, ?, ?);\r\n";


                try (PreparedStatement stm = con.prepareStatement(sql)) {
                    stm.setString(1, username);
                    stm.setString(2, email);
                    stm.setString(3, password);
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
                            u = new User(usernameLoaded, "", role,"","");
                        }
                    }
                }

                return u;
            }
        });
    }
    }

