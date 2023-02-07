package it.ecteam.easycharge.dao;

import java.sql.SQLException;

public interface DaoAction<T> {
    T act() throws SQLException,ClassNotFoundException;
}
