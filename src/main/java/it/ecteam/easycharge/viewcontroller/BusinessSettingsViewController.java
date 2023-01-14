package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.bean.BusinessBean;
import it.ecteam.easycharge.bean.CarBean;
import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.controller.BusinessController;
import it.ecteam.easycharge.controller.LoginController;
import it.ecteam.easycharge.controller.UserController;
import it.ecteam.easycharge.utils.SessionUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BusinessSettingsViewController implements Initializable {
    private Stage stage = new Stage();

    @FXML
    private Button homeBtn;
    @FXML
    private VBox settingsVB;
    @FXML
    private Label businessLabel = new Label();
    @FXML
    private Label addressLabel = new Label();
    @FXML
    private Label usernameLabel = new Label();
    @FXML
    private Label userLabel;
    @FXML
    private Button modifyBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private ComboBox modelComboBox;

    private BusinessGraphicChange ugc;

    @FXML
    protected void onHomeLoggedClick() {
        stage = (Stage) homeBtn.getScene().getWindow();
        this.ugc.toLoggedHome(stage);
    }

    @FXML
    protected void onMyBusinessClick() {
        stage = (Stage) homeBtn.getScene().getWindow();
        this.ugc.toMyBusiness(stage);
    }

    @FXML
    protected void saveAction() {
        UserController uc = new UserController();

        settingsVB.setVisible(false);
        if (modelComboBox.getValue() != null) {
            uc.changeCar(usernameLabel.getText(), modelComboBox.getValue().toString());
        }
        modifyBtn.setVisible(true);

    }

    @FXML
    protected void modifyAction() {
        settingsVB.setVisible(true);
        modifyBtn.setVisible(false);
    }

    @FXML
    protected void onLogoutClick() {
        SessionUser.getInstance().closeSession();
        stage = (Stage) logoutBtn.getScene().getWindow();
        this.ugc.toLogin(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ugc = BusinessGraphicChange.getInstance();
        UserBean ub = SessionUser.getInstance().getSession();

        this.usernameLabel.setText(ub.getUsername());

        BusinessController businessController = new BusinessController();
        List<BusinessBean> bb = businessController.getBusiness(ub.getUsername());

        this.businessLabel.setText(bb.get(0).getBusiness());
        this.addressLabel.setText(bb.get(0).getAddress());
    }
}

