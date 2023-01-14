package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.MainApplication;
import it.ecteam.easycharge.bean.*;
import it.ecteam.easycharge.controller.*;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import it.ecteam.easycharge.utils.SessionUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class RouteViewController extends StackPane implements Initializable {
    private Stage stage = new Stage();

    @FXML
    private Button loginBtn;
    @FXML
    private Button homeBtn;
    @FXML
    private Button mainBtn;
    @FXML
    private Button routeBtn;
    @FXML
    private Label userLabel;
    @FXML
    private TextField startField;
    @FXML
    private TextField destField;
    @FXML
    private WebView webMap;
    @FXML
    private Button issueBtn;
    @FXML
    private Button favoriteOnBtn;
    @FXML
    private Button favoriteOffBtn;
    @FXML
    private Label userMainLabel = new Label();
    @FXML
    private ListView listView;
    @FXML
    private ListView connectorView;
    @FXML
    private ListView riView;
    @FXML
    private Label csLabel;
    @FXML
    private Label pointLabel = new Label();
    @FXML
    private Label adsLabel;
    @FXML
    private Pane issuePane;
    @FXML
    private Pane adsPane;
    @FXML
    private CheckBox connectorBox;
    @FXML
    private CheckBox perfectBox;

    private UserGraphicChange ugc;
    private List<ChargingStationBean> chargingStationList = new ArrayList<>();
    private List<ChargingStationBean> perfectRouteList = new ArrayList<>();
    private List<ConnectorBean> connectorBeanList = new ArrayList<>();
    private List<ReportBean> report = new ArrayList<>();
    private List<Double> start = new ArrayList<>();
    private List<Double> end = new ArrayList<>();
    private CarBean cb = new CarBean();
    private String csid;
    private String space = "\n     ";

    @FXML
    protected void onLoginClick() throws IOException {
        stage = (Stage) loginBtn.getScene().getWindow();
        this.ugc.toLogin(stage);
    }

    @FXML
    protected void onRegisterClick() throws IOException {
        stage = (Stage) loginBtn.getScene().getWindow();
        this.ugc.toRegister(stage);
    }

    @FXML
    protected void onHomeClick() throws IOException {
        stage = (Stage) mainBtn.getScene().getWindow();
        this.ugc.toHome(stage);
    }

    @FXML
    protected void onHomeLoggedClick() throws IOException {
        stage = (Stage) homeBtn.getScene().getWindow();
        this.ugc.toLoggedHome(stage);
    }

    @FXML
    protected void onUserClick() throws IOException{
        stage = (Stage) homeBtn.getScene().getWindow();
        this.ugc.toUser(stage);
    }

    @FXML
    protected void onSearchRouteClick() throws IOException, ParseException, LocationNotFoundException {
        start = MapController.getCoordinates(startField.getText());
        end = MapController.getCoordinates(destField.getText());
        listView.getItems().clear();

        try {
            chargingStationList = MapController.getOnRoute(start, end);
            int i;
            for(i=0; i < chargingStationList.size(); i++){
                listView.getItems().add(i + 1 + ". " + chargingStationList.get(i).getName() + "\n" + chargingStationList.get(i).getFreeformAddress() + space);
                connectorBeanList = MapController.getChargingAvailability(chargingStationList.get(i).getId());
            }
        } catch (IOException | ParseException | ChargingStationNotFoundException e) {
            e.printStackTrace();
        }
        webMap.getEngine().load("https://www.google.com/maps/dir/?api=1&origin="+ startField.getText().replaceAll("\\s","+") +"&destination="+destField.getText().replaceAll("\\s","+")+"&travelmode=driving&waypoint_place_ids=ChIJ5zZx3kNjLxMRAIuXSFIRfwk%ChIJL6pCbOVhLxMRODH8uDzXLDo");
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
            riView.getItems().add(report.get(i).getUsername() + "\nsaid: " + report.get(i).getComment() + "\nDate: " + report.get(i).getDate()+ "\nLikes: " + report.get(i).getPoint() + space);
        }
    }

    @FXML
    protected void onCheckBox() {
        listView.getItems().clear();
        csLabel.setText("");

        try {
            //chargingStationList = MapController.getOnRoute(start, end);
            if(!connectorBox.isSelected()){
                int i;
                for (i = 0; i < chargingStationList.size(); i++) {
                    listView.getItems().add(i+1+". "+chargingStationList.get(i).getName()+"\n"+chargingStationList.get(i).getFreeformAddress()+space);
                }
            }else{
                int i;
                for (i = 0; i < chargingStationList.size(); i++) {
                    connectorBeanList = MapController.getChargingAvailability(chargingStationList.get(i).getId());
                    int k;
                    for(k = 0; k < connectorBeanList.size(); k++) {
                        if(Objects.equals(connectorBeanList.get(k).getType(), "Chademo")){
                            if (Objects.equals(connectorBeanList.get(k).getType(), cb.getConnectorType())){
                                listView.getItems().add(i + 1 + ". " + chargingStationList.get(i).getName() + "\n" + chargingStationList.get(i).getFreeformAddress() + space);
                                k = connectorBeanList.size();
                            }
                        }else if(Objects.equals(connectorBeanList.get(k).getType().substring(0, 13), cb.getConnectorType().substring(0, 13))){
                            listView.getItems().add(i + 1 + ". " + chargingStationList.get(i).getName() + "\n" + chargingStationList.get(i).getFreeformAddress() + space);
                            k = connectorBeanList.size();
                        }
                    }
                }
            }
        } catch (IOException | ParseException | ChargingStationNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onPerfectBox() throws LocationNotFoundException, ChargingStationNotFoundException, IOException, ParseException {

        if(!perfectBox.isSelected()){
            listView.getItems().clear();
            int i;
            for(i=0; i < chargingStationList.size(); i++){
                listView.getItems().add(i + 1 + ". " + chargingStationList.get(i).getName() + "\n" + chargingStationList.get(i).getFreeformAddress() + space);
            }
            return;
        }
        listView.getItems().clear();
        csLabel.setText("");

        try {
            perfectRouteList = RouteController.getPerfectRoute(chargingStationList, start, end, Integer.parseInt(cb.getRange()), cb.getConnectorType());
            if(!perfectRouteList.isEmpty()) {
                int i;
                for (i = 0; i < perfectRouteList.size(); i++) {
                    listView.getItems().add(i + 1 + ". " + perfectRouteList.get(i).getName() + "\n" + perfectRouteList.get(i).getFreeformAddress() + space);
                }
            }else{
                listView.getItems().add("Your car can reach the destination without any recharge.");
                connectorView.getItems().clear();
            }
        } catch (IOException | ParseException | ChargingStationNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onItemSelected() {
        connectorView.getItems().clear();
        riView.getItems().clear();
        issuePane.setVisible(false);
        adsLabel.setText("");

        String selected = listView.getSelectionModel().getSelectedItems().toString();
        int id;
        if (selected.charAt(2) == '.'){
            id = Integer.parseInt(String.valueOf(selected.charAt(1)));
        }else if(selected.charAt(3) == '.'){
            id = Integer.parseInt(selected.substring(1, 3));
        }else{
            return;
        }
        report = ReportController.getUserReport(String.valueOf(chargingStationList.get(id - 1).getId()));
        csid = chargingStationList.get(id-1).getId();

        UserBean ub = SessionUser.getInstance().getSession();
        if (ub != null) {
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

        int i;
        try {
            if(!perfectBox.isSelected()) {
                connectorBeanList = MapController.getChargingAvailability(chargingStationList.get(id - 1).getId());
                csLabel.setText(chargingStationList.get(id-1).getName());
                webMap.getEngine().load("https://www.google.it/maps/place/"+chargingStationList.get(id-1).getLatitude()+","+chargingStationList.get(id-1).getLongitude());
            }else{
                connectorBeanList = MapController.getChargingAvailability(perfectRouteList.get(id - 1).getId());
                csLabel.setText(perfectRouteList.get(id-1).getName());
                webMap.getEngine().load("https://www.google.it/maps/place/"+perfectRouteList.get(id-1).getLatitude()+","+perfectRouteList.get(id-1).getLongitude());
            }
        } catch (IOException | ChargingStationNotFoundException | ParseException e) {
            e.printStackTrace();
        }

        for(i=0; i < connectorBeanList.size(); i++) {
            connectorView.getItems().add("Type:"+ connectorBeanList.get(i).getType() + "\nTotal: " + connectorBeanList.get(i).getTotal() + "\nAvailable: " + connectorBeanList.get(i).getAvailable() + "\nOccupied: " + connectorBeanList.get(i).getOccupied() + "\nReserved: " + connectorBeanList.get(i).getReserved() + "\nUnknown: " + connectorBeanList.get(i).getUnknown() + "\nOutOfService: " + connectorBeanList.get(i).getOutOfService() + space);
        }

        issueBtn.setVisible(!report.isEmpty());

        List<BusinessBean> chargingStationAds = BusinessController.getCSAds(csid);
        assert chargingStationAds != null;
        if (!chargingStationAds.isEmpty()) {
            adsPane.setVisible(true);
            for(i=0; i < Objects.requireNonNull(chargingStationAds).size(); i++) {
                adsLabel.setText("While you are charging try "+ chargingStationAds.get(i).getBusiness() + " " + chargingStationAds.get(i).getAddress());
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ugc = UserGraphicChange.getInstance();

        webMap.getEngine().load("https://www.google.com/maps/dir/?api=1&origin=&destination=&travelmode=driving&waypoint_place_ids=ChIJ5zZx3kNjLxMRAIuXSFIRfwk%ChIJL6pCbOVhLxMRODH8uDzXLDo");

        //init nameBar
        UserBean ub = SessionUser.getInstance().getSession();

        UserController uc = new UserController();
        userMainLabel.setText(ub.getUsername());
        cb = uc.getCar(userMainLabel.getText());

        userLabel.setText(ub.getUsername());
        issuePane.setVisible(false);
        issueBtn.setVisible(false);
        favoriteOnBtn.setVisible(false);
        favoriteOffBtn.setVisible(false);
        adsPane.setVisible(false);
    }
}