package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.MainApplication;
import it.ecteam.easycharge.utils.DataBaseConnection;
import it.ecteam.easycharge.utils.FileManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginViewController extends MainApplication {
    private Stage stage = new Stage();

    @FXML
    private Button loginBtn;
    @FXML
    private Button homeBtn;
    @FXML
    private Button routeBtn;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Label loginMessageLabel;


    @FXML
    protected void onHomeClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        stage = (Stage) homeBtn.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scene);
    }

    @FXML
    protected void onRouteClick() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("auth-view.fxml"));
        stage = (Stage) routeBtn.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scene);
    }

    @FXML
    protected void onLoginClick(ActionEvent e) {

        if(!usernameTextField.getText().isBlank() && !passwordPasswordField.getText().isBlank()){
            validateLogin();
        }else{
            loginMessageLabel.setText("Please enter username and password.");
        }
    }

    public void validateLogin(){
        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "select count(1) from users where username = '" + usernameTextField.getText() + "' and password = '" + passwordPasswordField.getText() + "'";

        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()) {
                if(queryResult.getInt(1) == 1){
                    FileManager file = new FileManager();
                    if(!file.fileExists("user")){
                        file.createFile("user");
                        file.writeToFile("user",usernameTextField.getText());
                    }
                    getID();
                    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-logged-view.fxml"));
                    stage = (Stage) loginBtn.getScene().getWindow();
                    Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
                    stage.setScene(scene);
                }else{
                    loginMessageLabel.setText("Invalid login!");
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getID(){
        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getConnection();

        String getID = "select idusers from users where username = '" + usernameTextField.getText() + "' and password = '" + passwordPasswordField.getText() + "'";

        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getID);

            while(queryResult.next()) {
                    FileManager file = new FileManager();
                    if(!file.fileExists("userid")){
                        file.createFile("userid");
                    }
                   int id = queryResult.getInt(1);
                //file.writeToFile("userid",id);
                System.out.println(id);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}