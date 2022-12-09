package it.ecteam.easycharge.dao;

import it.ecteam.easycharge.utils.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class BusinessUserDao extends DaoTemplate{
    public Boolean createBusinessUser(String username, String password, String email, String role, String business) {
        return this.execute(() -> {
            Connection con = DataBaseConnection.getConnection();
            String sql = "call easycharge.add_businessuser(?, ?, ?, ?, ?);\r\n";


            try (PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setString(1, username);
                stm.setString(2, email);
                stm.setString(3, password);
                stm.setString(4, role);
                stm.setString(5, business);
                stm.executeUpdate();
            }

            return true;
        }) != null;
    }
}
