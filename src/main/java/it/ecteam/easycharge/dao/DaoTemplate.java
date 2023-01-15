package it.ecteam.easycharge.dao;

import java.sql.SQLException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DaoTemplate {
    protected static final Logger logger = Logger.getLogger("Dao");

    public DaoTemplate() {
    }

    protected final <T> T execute(DaoAction<T> da) {
        try {
            return da.act();
        } catch (SQLException | ClassNotFoundException var3) {
            logger.log(Level.WARNING, var3.toString());
        } catch (Throwable throwable) {
            logger.log(Level.WARNING, (Supplier<String>) throwable);
        }

        return null;
    }
}
