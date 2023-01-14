package it.ecteam.easycharge.dao;

public interface DaoAction<T> {
    T act() throws Throwable;
}
