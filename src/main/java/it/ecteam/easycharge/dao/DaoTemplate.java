package it.ecteam.easycharge.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DaoTemplate {
    protected static final Logger logger = Logger.getLogger("Dao");

    protected DaoTemplate() {
    }

    protected final <T> T execute(DaoAction<T> da) {
        try {
            return da.act();
        } catch (Throwable e) {
            logger.log(Level.WARNING, e.toString());
        }

        return null;
    }
}
