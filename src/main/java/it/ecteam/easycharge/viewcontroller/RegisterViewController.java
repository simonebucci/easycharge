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
    protected void onRegisterClick() throws IOException {
        LoginController loginController = new LoginController();
        boolean regResult;

            if (!usernameTextField.getText().isBlank() && !passwordPasswordField.getText().isBlank() && !emailTextField.getText().isBlank() && !carTextField.getText().isBlank() && !dcTextField.getText().isBlank() && !acTextField.getText().isBlank() && !batteryTextField.getText().isBlank() && !rangeTextField.getText().isBlank()) {
                UserBean user = new UserBean();
                user.setUsername(usernameTextField.getText());
                user.setPassword(passwordPasswordField.getText());
                user.setEmail(emailTextField.getText());


                regResult = loginController.createUser(user);
                if(Boolean.TRUE.equals(regResult)) {
                    this.registerMessageLabel.setText("Registration successfull");
                    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-logged-view.fxml"));
                    stage = (Stage) registerBtn.getScene().getWindow();
                    Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
                    stage.setScene(scene);
                }else{
                    this.registerMessageLabel.setText("Registration unsuccessfull! Username already in use, please choose a different one!");
                }

            } else {
                registerMessageLabel.setText("Please enter all required data.");
            }

    }

}