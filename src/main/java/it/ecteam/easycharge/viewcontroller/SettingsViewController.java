package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.MainApplication;
import it.ecteam.easycharge.bean.CarBean;
import it.ecteam.easycharge.controller.UserController;
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
    private Button homeBtn;
    @FXML
    private TextField carTF;
    @FXML
    private TextField capacityTF;
    @FXML
    private TextField rangeTF;
    @FXML
    private TextField cTypeTF;
    @FXML
    private VBox settingsVB;
    @FXML
    private Label carLabel;
    @FXML
    private Label capacityLabel;
    @FXML
    private Label rangeLabel;
    @FXML
    private Label cTypeLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label userLabel;
    @FXML
    private Button modifyBtn;

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
        cTypeLabel.setText(cTypeTF.getText());
        modifyBtn.setVisible(true);

    }

    @FXML
    protected void modifyAction() {
        settingsVB.setVisible(true);
        modifyBtn.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserController userController = new UserController();
        CarBean cb = new CarBean();

        cb = userController.getCar("niko");
        this.carLabel.setText(cb.getName());
        this.capacityLabel.setText(cb.getCapacity());
        this.rangeLabel.setText(cb.getRange());
        this.cTypeLabel.setText(cb.getConnectorType());
    }
}