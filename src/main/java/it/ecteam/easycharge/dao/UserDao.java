package it.ecteam.easycharge.dao;

import it.ecteam.easycharge.utils.DataBaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao extends DaoTemplate {

    public UserDao() {

    }

    public Boolean createUser(final String username, final String password, final String email) {
        return this.execute(() -> {
            Connection con = DataBaseConnection.getConnection();
            String sql = "call easycharge.add_user(?, ?, ?);\r\n";
            Throwable var3 = null;
            Object var4 = null;

            try {

                try (PreparedStatement stm = con.prepareStatement(sql)) {
                    stm.setString(1, username);
                    stm.setString(2, email);
                    stm.setString(3, password);
                    stm.executeUpdate();
                }
            } catch (Throwable var11) {
                var3 = var11;

                throw var3;
            }

            return true;
        }) != null;
    }
}