package it.ecteam.easycharge.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileManager {

    public void createFile(String name) {
        try {
            File myObj = new File(name + ".txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred creating the file.");
            e.printStackTrace();
        }
    }

    public void writeToFile(String filename, String data) {
        try {
            FileWriter myWriter = new FileWriter(filename + ".txt");
            myWriter.write(data);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred writing to the file.");
            e.printStackTrace();
        }
    }

    public String readFile(String filename) {
        String data = "error";
        try {
            File myObj = new File(filename + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred reading the file.");
            e.printStackTrace();
        }
        return data;
    }

    public boolean fileExists(String filename) {
        File myObj = new File(filename + ".txt");
        if (myObj.exists()) {
            System.out.println("File exists: " + myObj.getName());
            return true;
        } else {
            System.out.println("File doesn't exists.");
            return false;
        }
    }
}
