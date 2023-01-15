package it.ecteam.easycharge.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileManager {
    protected static final Logger logger = Logger.getLogger("FileManager");
    public void createFile(String name) {
        try {
            File myObj = new File(name + ".txt");
            if (myObj.createNewFile()) {
                logger.log(Level.INFO, "File created: " + myObj.getName());
            } else {
                logger.log(Level.INFO, "File already exists.");
            }
        } catch (IOException e) {
            logger.log(Level.INFO, "An error occurred creating the file.");
            logger.log(Level.WARNING, (Supplier<String>) e);
        }
    }

    public void writeToFile(String filename, String data) {
        try {
            FileWriter myWriter = new FileWriter(filename + ".txt");
            myWriter.write(data);
            myWriter.close();
            logger.log(Level.INFO, "Successfully wrote to the file.");
        } catch (IOException e) {
            logger.log(Level.WARNING, (Supplier<String>) e);
        }
    }

    public String readFile(String filename) {
        String data = "error";
        try {
            File myObj = new File(filename + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
                logger.log(Level.INFO, data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            logger.log(Level.WARNING, (Supplier<String>) e);
        }
        return data;
    }

    public boolean fileExists(String filename) {
        File myObj = new File(filename + ".txt");
        if (myObj.exists()) {
            logger.log(Level.INFO, "File exists: " + myObj.getName());
            return true;
        } else {
            logger.log(Level.INFO, "File doesn't exists.");
            return false;
        }
    }
}
