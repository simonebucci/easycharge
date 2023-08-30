package it.ecteam.easycharge.dao;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DaoTemplate {
    protected static final Logger logger = Logger.getLogger("Dao");

    protected DaoTemplate() {
    }

    protected JSONObject openFile(String path){

        JSONParser parser = new JSONParser();
        JSONObject object = null;

        try {
            object = (JSONObject) parser.parse(new FileReader(path));
        } catch (IOException | org.json.simple.parser.ParseException e) {
            logger.log(Level.WARNING, e.toString());
        }
        return object;
    }

    protected void writeOnFile(String path, JSONObject object){

        try (FileWriter file = new FileWriter(path)) {
            file.write(object.toString());
        } catch (IOException e) {
            logger.log(Level.WARNING, e.toString());
        }
    }

    protected final <T> T execute(DaoAction<T> da) {
        try {
            return da.act();
        } catch (SQLException|ClassNotFoundException e) {
            logger.log(Level.WARNING, e.toString());
        }

        return null;
    }
}
