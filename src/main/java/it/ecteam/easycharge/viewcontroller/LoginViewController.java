package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.MainApplication;
import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.controller.LoginController;
import it.ecteam.easycharge.exceptions.LoginEmptyFieldException;
import it.ecteam.easycharge.utils.SessionUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;


public class LoginViewController {
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
                        //BusinessGraphicChange.getInstance().toHomepage(this.usernameTextField.getScene());
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

    public void init() {
        this.ugc = UserGraphicChange.getInstance();
    }


}