package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.controller.LoginController;
import it.ecteam.easycharge.exceptions.LoginEmptyFieldException;
import it.ecteam.easycharge.utils.SessionUser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginViewController implements Initializable {
    private Stage stage = new Stage();

    @FXML
    private Button loginBtn;
    @FXML
    private Button registerBtn;
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

    private UserGraphicChange ugc;

    @FXML
    protected void onHomeClick() throws IOException {
        stage = (Stage) registerBtn.getScene().getWindow();
        this.ugc.toHome(stage);
    }

    @FXML
    protected void onRouteClick() throws IOException{
        stage = (Stage) registerBtn.getScene().getWindow();
        this.ugc.toAuth(stage);
    }

    @FXML
    protected void onRegisterClick() {
        stage = (Stage) registerBtn.getScene().getWindow();
        this.ugc.toRegister(stage);
    }

    @FXML
    protected void onLoginClick() {

        UserBean ub = new UserBean();
        ub.setUsername(this.usernameTextField.getText());
        ub.setPassword(this.passwordPasswordField.getText());

        LoginController controller = new LoginController();
        UserBean u;
        try {
            u=controller.login(ub);
            if(u==null) {
                this.loginMessageLabel.setText("Wrong username or password");
                this.usernameTextField.setText("");
                this.passwordPasswordField.setText("");
            }else {
                String role=u.getRole();

                //SET SESSION GENERAL USER
                SessionUser su=SessionUser.getInstance();
                su.setSession(u);

                switch(role) {
                    case "user":
                        stage = (Stage) homeBtn.getScene().getWindow();
                        UserGraphicChange.getInstance().toLoggedHome(stage);
                        break;
                    case "business":
                        //set business homepage controller
                        stage = (Stage) homeBtn.getScene().getWindow();
                        BusinessGraphicChange.getInstance().toLoggedHome(stage);
                        break;
                    default:
                        break;
                }

            }
        }
        catch(LoginEmptyFieldException e) {
            this.loginMessageLabel.setText(e.getMessage());

        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ugc = UserGraphicChange.getInstance();
    }
    public void init() {
    }


}