package it.ecteam.easycharge.dao;

import it.ecteam.easycharge.utils.DataBaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao extends DaoTemplate {


    public Boolean createUser(String username, String password, String email) {
        return this.execute(() -> {
            Connection con = DataBaseConnection.getConnection();
            String sql = "call easycharge.add_user(?, ?, ?, ?);\r\n";


                try (PreparedStatement stm = con.prepareStatement(sql)) {
                    stm.setString(1, username);
                    stm.setString(2, email);
                    stm.setString(3, password);
                    stm.executeUpdate();
                }

            return true;
            }) != null;
            }
    }

