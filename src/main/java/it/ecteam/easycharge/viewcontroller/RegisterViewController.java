package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.MainApplication;
import it.ecteam.easycharge.bean.BusinessBean;
import it.ecteam.easycharge.bean.CarBean;
import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.controller.LoginController;
import it.ecteam.easycharge.controller.UserController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterViewController implements Initializable {
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
    private ComboBox<String> modelBox;
    @FXML
    private Button routeBtn;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField businessTextField;
    @FXML
    private TextField baddressTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Label registerMessageLabel;

    static final String B = "business";
    protected static final Logger logger = Logger.getLogger("gui");

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
    protected void onUserTypeClick() {
        if(Objects.equals(userType.getValue(), B) && userType.getValue() != null){
            modelBox.setVisible(false);
            businessTextField.setVisible(true);
            this.baddressTextField.setVisible(true);
        }else{
            modelBox.setVisible(true);
            businessTextField.setVisible(false);
            this.baddressTextField.setVisible(false);
        }
    }

    @FXML
    protected void onRegisterButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("logged-view.fxml"));
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
    protected void onRegisterClick() throws IOException {
        LoginController loginController = new LoginController();
        boolean regResult;
        String userRole = userType.getValue();
        String dataError = "Please enter all required data.";

        if (UserController.isInvalidData(userType, usernameTextField, passwordPasswordField, emailTextField)) {
            registerMessageLabel.setText(dataError);
            return;
        }
        if (UserController.isInvalidDataForUser(userRole, modelBox)) {
            registerMessageLabel.setText(dataError);
            return;
        }
        if (UserController.isInvalidDataForBusiness(userRole, businessTextField)) {
            registerMessageLabel.setText(dataError);
            return;
        }

        regResult = userRole.equals("user") ? loginController.createUser(UserController.createUserBean(usernameTextField, passwordPasswordField, emailTextField, userType, modelBox)) : loginController.createBusinessUser(UserController.createBusinessBean(usernameTextField,passwordPasswordField, emailTextField, userType, businessTextField, baddressTextField));

        if (Boolean.TRUE.equals(regResult)) {
            this.registerMessageLabel.setText("Registration successfull");
            stage = (Stage) homeBtn.getScene().getWindow();
            if (userRole.equals("user")) {
                UserGraphicChange.getInstance().toLoggedHome(stage);
            } else {
                BusinessGraphicChange.getInstance().toLoggedHome(stage);
            }
        } else {
            this.registerMessageLabel.setText("Registration unsuccessfull! Username already in use, please choose a different one!");
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb){
        List<CarBean> cb = LoginController.getCar();

        ObservableList<String> oal = FXCollections.observableArrayList();
        int i;
        for(i=0; i<cb.size(); i++){
            oal.add(cb.get(i).getName());
        }
        this.modelBox.setItems(oal);
        this.userType.setItems(FXCollections.observableArrayList("user", B));
        this.businessTextField.setVisible(false);
        this.baddressTextField.setVisible(false);
    }
}