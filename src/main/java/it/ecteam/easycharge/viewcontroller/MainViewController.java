package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.bean.*;
import it.ecteam.easycharge.controller.ReportController;
import it.ecteam.easycharge.controller.UserController;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import it.ecteam.easycharge.controller.MapController;
import it.ecteam.easycharge.utils.SessionUser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.*;

public class MainViewController extends StackPane implements Initializable  {
    private Stage stage = new Stage();

    @FXML
    private Button loginBtn;
    @FXML
    private Button registerBtn;
    @FXML
    private Button homeBtn;
    @FXML
    private Button reportBtn;
    @FXML
    private Button issueBtn;
    @FXML
    private Button favoriteOnBtn;
    @FXML
    private Button favoriteOffBtn;
    @FXML
    private Label userMainLabel = new Label();
    @FXML
    private Button routeBtn;
    @FXML
    private WebView webMap;
    @FXML
    private ListView listView;
    @FXML
    private ListView connectorView;
    @FXML
    private ListView riView;
    @FXML
    private Slider slider;
    @FXML
    private Label rangeLabel;
    @FXML
    private Label csLabel;
    @FXML
    private Label alertLabel;
    @FXML
    private Label pointLabel;
    @FXML
    private TextArea reportTextArea;
    @FXML
    private Pane reportPane;
    @FXML
    private Pane issuePane;
    @FXML
    private CheckBox connectorBox;

    private UserGraphicChange ugc;
    private UserBean ub = SessionUser.getInstance().getSession();

    private List<ChargingStationBean> chargingStationList = new ArrayList<>();
    private List<ConnectorBean> connectorBeanList = new ArrayList<>();
    private List<ReportBean> report = new ArrayList<>();
    private String csid;

    @FXML
    protected void onLoginClick() {
        stage = (Stage) loginBtn.getScene().getWindow();
        this.ugc.toLogin(stage);
    }

    @FXML
    protected void onRegisterClick() {
        stage = (Stage) registerBtn.getScene().getWindow();
        this.ugc.toRegister(stage);
    }

    @FXML
    protected void onHomeClick() {
        stage = (Stage) homeBtn.getScene().getWindow();
        this.ugc.toHome(stage);
    }

    @FXML
    protected void onHomeLoggedClick() {
        stage = (Stage) homeBtn.getScene().getWindow();
        this.ugc.toLoggedHome(stage);
    }

    @FXML
    protected void onUserClick() throws IOException {
        stage = (Stage) homeBtn.getScene().getWindow();
        this.ugc.toUser(stage);
    }

    @FXML
    protected void onRouteLoggedClick() {
        stage = (Stage) homeBtn.getScene().getWindow();
        this.ugc.toRoute(stage);
    }

    @FXML
    protected void onRouteClick() throws IOException {
        stage = (Stage) homeBtn.getScene().getWindow();
        this.ugc.toAuth(stage);
    }

    @FXML
    protected void onSliderMove() {
        rangeLabel.setText(((int) slider.getValue())/1000 + "km");
    }

    @FXML
    protected void onSliderRelease() {
        UserController uc = new UserController();

        rangeLabel.setText(((int) slider.getValue())/1000 + "km");
        listView.getItems().clear();

        try {
            chargingStationList = MapController.getNearby((int) slider.getValue()); //radius range 1 to 50000
            if(connectorBox != null && connectorBox.isSelected()){
                CarBean cb = uc.getCar(userMainLabel.getText());
                int i;
                for (i = 0; i < chargingStationList.size(); i++) {
                    connectorBeanList = MapController.getChargingAvailability(chargingStationList.get(i).getId());
                    int k;
                    for(k = 0; k < connectorBeanList.size(); k++) {
                        if (Objects.equals(connectorBeanList.get(k).getType(), cb.getConnectorType()))
                            listView.getItems().add(i + 1 + ". " + chargingStationList.get(i).getName() + "\n" + chargingStationList.get(i).getFreeformAddress() + "\n     ");
                    }
                }
            }else{
                int i;
                for (i = 0; i < chargingStationList.size(); i++) {
                    listView.getItems().add(i+1+". "+chargingStationList.get(i).getName()+"\n"+chargingStationList.get(i).getFreeformAddress()+"\n     ");
                }
            }

        } catch (IOException | ParseException | LocationNotFoundException | java.text.ParseException | ChargingStationNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onCheckBox() {
        listView.getItems().clear();
        UserController uc = new UserController();
        CarBean cb = uc.getCar(userMainLabel.getText());
        try {
            chargingStationList = MapController.getNearby((int) slider.getValue()); //radius range 1 to 50000
            if(!connectorBox.isSelected()){
                int i;
                for (i = 0; i < chargingStationList.size(); i++) {
                    listView.getItems().add(i+1+". "+chargingStationList.get(i).getName()+"\n"+chargingStationList.get(i).getFreeformAddress()+"\n     ");
                }
            }else{
                int i;
                for (i = 0; i < chargingStationList.size(); i++) {
                    connectorBeanList = MapController.getChargingAvailability(chargingStationList.get(i).getId());
                    int k;
                    for(k = 0; k < connectorBeanList.size(); k++) {
                        if (Objects.equals(connectorBeanList.get(k).getType(), cb.getConnectorType()))
                            listView.getItems().add(i + 1 + ". " + chargingStationList.get(i).getName() + "\n" + chargingStationList.get(i).getFreeformAddress() + "\n     ");
                    }
                }
            }
        } catch (IOException | ParseException | LocationNotFoundException | java.text.ParseException | ChargingStationNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onItemSelected(){
        connectorView.getItems().clear();
        riView.getItems().clear();
        issuePane.setVisible(false);

        String selected = listView.getSelectionModel().getSelectedItems().toString();
        int id;
        if (selected.charAt(2) == '.'){
            id = Integer.parseInt(String.valueOf(selected.charAt(1)));
        }else{
            id = Integer.parseInt(selected.substring(1, 3));
        }
        report = ReportController.getUserReport(String.valueOf(chargingStationList.get(id-1).getId()));
        csid = chargingStationList.get(id-1).getId();

        UserBean ub = SessionUser.getInstance().getSession();
        if (ub != null) {
            reportBtn.setVisible(true);
            reportPane.setVisible(false);
            favoriteOffBtn.setVisible(true);
            favoriteOnBtn.setVisible(false);

            List<ChargingStationBean> favoriteCSB = UserController.getFavorite(ub.getUsername());
            int i;
            for(i=0; i<favoriteCSB.size(); i++){
                if(Objects.equals(favoriteCSB.get(i).getId(), csid)){
                    favoriteOnBtn.setVisible(true);
                    favoriteOffBtn.setVisible(false);

                }
            }

        }

        csLabel.setText(chargingStationList.get(id-1).getName());
        webMap.getEngine().load("https://www.google.it/maps/place/"+chargingStationList.get(id-1).getLatitude()+","+chargingStationList.get(id-1).getLongitude());
        int i;
        try {
            connectorBeanList = MapController.getChargingAvailability(chargingStationList.get(id-1).getId());
        } catch (IOException | ChargingStationNotFoundException | ParseException e) {
            e.printStackTrace();
        }

        for(i=0; i < connectorBeanList.size(); i++) {
            connectorView.getItems().add("Type:"+ connectorBeanList.get(i).getType() + "\nTotal: " + connectorBeanList.get(i).getTotal() + "\nAvailable: " + connectorBeanList.get(i).getAvailable() + "\nOccupied: " + connectorBeanList.get(i).getOccupied() + "\nReserved: " + connectorBeanList.get(i).getReserved() + "\nUnknown: " + connectorBeanList.get(i).getUnknown() + "\nOutOfService: " + connectorBeanList.get(i).getOutOfService() + "\n     ");
        }

        issueBtn.setVisible(!report.isEmpty());
    }

    @FXML
    protected void onSendReportClick() {
        ReportController reportController = new ReportController();
        String selected = listView.getSelectionModel().getSelectedItems().toString();
        int id = Integer.parseInt(String.valueOf(selected.charAt(1)));

        reportController.reportCS(chargingStationList.get(id-1).getId(), userMainLabel.getText(), reportTextArea.getText());

        alertLabel.setText("Report sent, thank you!");
        alertLabel.setVisible(true);
        reportTextArea.clear();

    }

    @FXML
    protected void onReportClick(){
        reportPane.setVisible(true);
        reportBtn.setVisible(false);
    }

    @FXML
    protected void onCloseClick(){
        reportPane.setVisible(false);
        reportTextArea.clear();
        reportBtn.setVisible(true);
        alertLabel.setText("");
    }
    @FXML
    protected void onCloseIssueClick(){
        issuePane.setVisible(false);
    }

    @FXML
    protected void onIssueClick(){
        issuePane.setVisible(true);
        riView.getItems().clear();
        pointLabel.setText("");
        int i;
        for(i=0; i < report.size(); i++) {
            riView.getItems().add(report.get(i).getUsername() + "\nsaid: " + report.get(i).getComment() + "\nDate: " + report.get(i).getDate()+ "\nLikes: " + report.get(i).getPoint() +"\n     ");
        }
    }

    @FXML
    protected void onPointBtnClick(){
        ReportController rc = new ReportController();
        UserBean ub = SessionUser.getInstance().getSession();

        String selected = riView.getSelectionModel().getSelectedItems().toString();
        if(!Objects.equals(selected, "[]")) {
            String[] arrOfStr = selected.split("\n");

            String username = arrOfStr[0];
            username = username.replace("[", "");

            String date = arrOfStr[2];
            date = date.replace("Date: ", "");
            Date dateFormatted = java.sql.Date.valueOf(date);

            if (Objects.equals(ub.getUsername(), username)) {
                pointLabel.setText("You can't give a like to your self.");
                pointLabel.setTextFill(Color.RED);
            } else {
                rc.givePointToUser(username, csid, dateFormatted);
                pointLabel.setText("You liked the selected report!");
                pointLabel.setTextFill(Color.GREEN);
            }
        }else{
            pointLabel.setText("You must select a report.");
            pointLabel.setTextFill(Color.RED);
        }
    }

    @FXML
    protected void onFavoriteOffBtnClick(){
        UserController uc = new UserController();

        uc.setFavorite(userMainLabel.getText(), csid);
        favoriteOnBtn.setVisible(true);
        favoriteOffBtn.setVisible(false);
    }

    @FXML
    protected void onFavoriteOnBtnClick(){
        UserController uc = new UserController();
        UserBean ub = SessionUser.getInstance().getSession();

        uc.deleteFavorite(ub.getUsername(), csid);
        favoriteOnBtn.setVisible(false);
        favoriteOffBtn.setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        issuePane.setVisible(false);
        issueBtn.setVisible(false);

        webMap.getEngine().load("https://www.google.it/maps/");
        rangeLabel.setText(((int) slider.getValue())/1000 + "km");

        try {
            chargingStationList = MapController.getNearby((int)slider.getValue()); //radius range 1 to 50000
            int i;
            for(i=0; i < chargingStationList.size(); i++){
                listView.getItems().add(i+1+". "+chargingStationList.get(i).getName()+"\n"+chargingStationList.get(i).getFreeformAddress()+"\n     ");
            }
        } catch (IOException | ParseException | LocationNotFoundException | java.text.ParseException | ChargingStationNotFoundException e) {
            e.printStackTrace();
        }

        this.ugc = UserGraphicChange.getInstance();
        //init nameBar

        if (ub != null) {
            userMainLabel.setText(ub.getUsername());
            reportPane.setVisible(false);
            reportBtn.setVisible(false);
            favoriteOnBtn.setVisible(false);
            favoriteOffBtn.setVisible(false);
        }
    }

    public void init() {
    }
}