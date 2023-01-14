package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.MainApplication;
import it.ecteam.easycharge.bean.BusinessBean;
import it.ecteam.easycharge.bean.CarBean;
import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.controller.LoginController;
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

    static final String b = "business";

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
        if(Objects.equals(userType.getValue(), b) && userType.getValue() != null){
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
        boolean regResult = false;
        String email = "";
        String username = "";
        String password = "";
        String userRole = "";
        String dataError = "Please enter all required data.";

        if ((Objects.equals(userType.getValue(), null) || usernameTextField.getText().isBlank() || passwordPasswordField.getText().isBlank() || emailTextField.getText().isBlank())){
            registerMessageLabel.setText(dataError);
        }else{
            username = usernameTextField.getText();
            password = passwordPasswordField.getText();
            email = emailTextField.getText();
            userRole = userType.getValue();

            if(userRole.equals("user") && Objects.equals(modelBox.getValue(), null)){
                registerMessageLabel.setText(dataError);
            }else if(userRole.equals(b) && businessTextField.getText().isBlank()){
                registerMessageLabel.setText(dataError);
            }else{

                if (userRole.equals("user")) {
                    UserBean u = new UserBean();
                    u.setUsername(username);
                    u.setPassword(password);
                    u.setEmail(email);
                    u.setRole(userRole);
                    u.setCar(modelBox.getValue());
                    regResult = loginController.createUser(u);
                } else if (userRole.equals(b)) {
                    BusinessBean bu = new BusinessBean();
                    bu.setUsername(username);
                    bu.setPassword(password);
                    bu.setRole(userRole);
                    bu.setBusiness(businessTextField.getText());
                    bu.setEmail(email);
                    bu.setAddress(baddressTextField.getText());
                    regResult = loginController.createBusinessUser(bu);
                }

                if (Boolean.TRUE.equals(regResult)) {
                    this.registerMessageLabel.setText("Registration successfull");
                    switch (userRole) {
                        case "user" -> {
                            stage = (Stage) homeBtn.getScene().getWindow();
                            UserGraphicChange.getInstance().toLoggedHome(stage);
                        }
                        case b ->
                                //set business homepage controller
                                BusinessGraphicChange.getInstance().toLoggedHome(stage);
                        default -> {
                        }
                    }
                } else {
                    this.registerMessageLabel.setText("Registration unsuccessfull! Username already in use, please choose a different one!");
                }
            }
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
        this.userType.setItems(FXCollections.observableArrayList("user", b));
        this.businessTextField.setVisible(false);
        this.baddressTextField.setVisible(false);
    }
}