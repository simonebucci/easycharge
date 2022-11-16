package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.MainApplication;
import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.controller.LoginController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterViewController extends MainApplication implements Initializable {
    private Stage stage = new Stage();

    @FXML
    private Button loginBtn;
    @FXML
    private Button homeBtn;
    @FXML
    private Button registerBtn;
    @FXML
    private ComboBox<String> userType;
    @FXML
    private Button routeBtn;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField acTextField;
    @FXML
    private TextField dcTextField;
    @FXML
    private TextField carTextField;
    @FXML
    private TextField batteryTextField;
    @FXML
    private TextField rangeTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Label registerMessageLabel;

    @FXML
    protected void onLoginClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
        stage = (Stage) loginBtn.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scene);
    }

    @FXML
    protected void onHomeClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        stage = (Stage) homeBtn.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scene);
    }

    @FXML
    protected void selectImage() throws IOException {

    }

    @FXML
    protected void onUserTypeClick() {

    }

    @FXML
    protected void onRegisterButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-logged-view.fxml"));
        stage = (Stage) registerBtn.getScene().getWindow();
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
    public void initialize(URL url, ResourceBundle rb){
        this.userType.setItems(FXCollections.observableArrayList("Normal User", "Business User"));


    }

    @FXML
    protected void onRegisterClick() {
        if(!usernameTextField.getText().isBlank() && !passwordPasswordField.getText().isBlank() && !emailTextField.getText().isBlank() && !carTextField.getText().isBlank() && !dcTextField.getText().isBlank() && !acTextField.getText().isBlank() && !batteryTextField.getText().isBlank() && !rangeTextField.getText().isBlank()){
            UserBean user = new UserBean();
            user.setUsername(usernameTextField.getText());
            user.setPassword(passwordPasswordField.getText());
            user.setEmail(emailTextField.getText());

            LoginController loginController = new LoginController();
            loginController.createUser(user);
        }else{
            registerMessageLabel.setText("Please enter all required data.");
        }

    }

    public void validateRegister(){
        //String insertNewUser = "insert into users (username, password, email, type, car, ac, dc, rng, capacity) values ('" + usernameTextField.getText() + "' , '" + passwordPasswordField.getText() +"' , '" + emailTextField.getText() + "' , '" + userType.getSelectionModel().getSelectedItem() + "' , '" + carTextField.getText() + "' , '" + dcTextField.getText() + "' , '" + acTextField.getText() + "' , '" + batteryTextField.getText() + "' , '" + rangeTextField.getText() +"')";
        /*try{
            Statement statement = connectDB.createStatement();
            ResultSet queryUserResult = statement.executeQuery(verifyUsername);

            while(queryUserResult.next()) {
                if(queryUserResult.getInt(1) == 1){
                    registerMessageLabel.setText("Username already in use, please choose a different one!");
                }else{

                    //statement.executeUpdate(verifyRegister);
                    statement.executeUpdate(insertNewUser);
                    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-logged-view.fxml"));
                    stage = (Stage) registerBtn.getScene().getWindow();
                    Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
                    stage.setScene(scene);
            }
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

}