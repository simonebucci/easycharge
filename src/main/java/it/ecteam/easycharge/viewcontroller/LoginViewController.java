package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.MainApplication;
import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.controller.LoginController;
import it.ecteam.easycharge.exceptions.LoginEmptyFieldException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;


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
    protected void onLoginClick() {

        UserBean gub = new UserBean();
        gub.setUsername(this.usernameTextField.getText());
        gub.setPassword(this.passwordPasswordField.getText());

        LoginController controller = new LoginController();
        UserBean gu;
        try {
            gu=controller.login(gub);
            if(gu==null) {
                this.loginMessageLabel.setText("Wrong username or password");
                this.usernameTextField.setText("");
                this.passwordPasswordField.setText("");
            }else {
                /*/String role=gu.getRole();

                /SET SESSION GENERAL USER
                SessionUser su=SessionUser.getInstance();
                su.setSession(gu);

                switch(role) {
                    case "user":
                        UserGraphicChange.getInstance().toHomepage(this.usernameTextField.getScene());
                        break;
                    case "business":
                        //set business homepage controller
                        ArtistGraphicChange.getInstance().toHomepage(this.usernameTextField.getScene());
                        break;
                    default:
                        break;
                }*/
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-logged-view.fxml"));
                stage = (Stage) loginBtn.getScene().getWindow();
                Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
                stage.setScene(scene);
            }
        }
        catch(LoginEmptyFieldException | IOException e) {
            this.loginMessageLabel.setText(e.getMessage());
        }

    }

}