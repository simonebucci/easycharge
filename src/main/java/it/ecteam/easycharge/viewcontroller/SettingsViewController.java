package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.MainApplication;
import it.ecteam.easycharge.bean.CarBean;
import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.controller.LoginController;
import it.ecteam.easycharge.controller.UserController;
import it.ecteam.easycharge.utils.SessionUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SettingsViewController implements Initializable {
    private Stage stage = new Stage();

    @FXML
    private Button homeBtn;
    @FXML
    private TextField carTF;
    @FXML
    private VBox settingsVB;
    @FXML
    private Label carLabel = new Label();
    @FXML
    private Label capacityLabel = new Label();
    @FXML
    private Label rangeLabel = new Label();
    @FXML
    private Label cTypeLabel = new Label();
    @FXML
    private Label usernameLabel = new Label();;
    @FXML
    private Label userLabel;
    @FXML
    private Button modifyBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private ComboBox modelComboBox;

    private UserGraphicChange ugc;

    @FXML
    protected void onHomeLoggedClick() {
        stage = (Stage) homeBtn.getScene().getWindow();
        this.ugc.toLoggedHome(stage);
    }

    @FXML
    protected void saveAction() {
        UserController uc = new UserController();

        settingsVB.setVisible(false);
        uc.changeCar(usernameLabel.getText(), modelComboBox.getValue().toString());
        modifyBtn.setVisible(true);

    }

    @FXML
    protected void modifyAction() {
        settingsVB.setVisible(true);
        modifyBtn.setVisible(false);
    }

    @FXML
    protected void onLogoutClick(){
        SessionUser.getInstance().closeSession();
        stage = (Stage) logoutBtn.getScene().getWindow();
        this.ugc.toLogin(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ugc = UserGraphicChange.getInstance();
        UserBean ub = SessionUser.getInstance().getSession();

        this.usernameLabel.setText(ub.getUsername());

        UserController userController = new UserController();
        CarBean cb = userController.getCar(ub.getUsername());

        this.carLabel.setText(cb.getName());
        this.capacityLabel.setText(cb.getCapacity()+"kWh");
        this.rangeLabel.setText(cb.getRange()+"Km");
        this.cTypeLabel.setText(cb.getConnectorType());

        List<CarBean> modelb = LoginController.getCar();

        ObservableList<String> oal = FXCollections.observableArrayList();
        int i;
        for(i=0; i<modelb.size(); i++){
            oal.add(modelb.get(i).getName());
        }
        this.modelComboBox.setItems(oal);

    }

    public void init() {

    }

}