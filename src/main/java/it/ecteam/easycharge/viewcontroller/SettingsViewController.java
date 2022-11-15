package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.MainApplication;
import it.ecteam.easycharge.utils.FileManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsViewController extends MainApplication implements Initializable {
    private Stage stage = new Stage();

    @FXML
    private Button loginBtn;
    @FXML
    private Button homeBtn;
    @FXML
    private Button registerBtn;
    @FXML
    private TextField carTF;
    @FXML
    private TextField capacityTF;
    @FXML
    private TextField rangeTF;
    @FXML
    private TextField acTF;
    @FXML
    private TextField dcTF;
    @FXML
    private VBox settingsVB;
    @FXML
    private Label carLabel;
    @FXML
    private Label capacityLabel;
    @FXML
    private Label rangeLabel;
    @FXML
    private Label acLabel;
    @FXML
    private Label dcLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label userLabel;
    @FXML
    private Button modifyBtn;

    @FXML
    protected void onLoginClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-logged-view.fxml"));
        stage = (Stage) loginBtn.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scene);
    }

    @FXML
    protected void onHomeLoggedClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-logged-view.fxml"));
        stage = (Stage) homeBtn.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scene);
    }

    @FXML
    protected void saveAction() {
        settingsVB.setVisible(false);
        carLabel.setText(carTF.getText());
        capacityLabel.setText(capacityTF.getText());
        rangeLabel.setText(rangeTF.getText());
        acLabel.setText(acTF.getText());
        dcLabel.setText(dcTF.getText());
        modifyBtn.setVisible(true);

    }

    @FXML
    protected void modifyAction() throws IOException {
        settingsVB.setVisible(true);
        modifyBtn.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FileManager file = new FileManager();
        if(file.fileExists("user")) {
            String name = file.readFile("user");
            usernameLabel.setText(name);
            userLabel.setText(name);
        }

    }
}