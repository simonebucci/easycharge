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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

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
    private Label alertLabel;
    @FXML
    private Label pointLabel = new Label();
    @FXML
    private Label adsLabel;
    @FXML
    private Pane issuePane;
    @FXML
    private Pane alertPane;
    @FXML
    private Pane adsPane;
    @FXML
    private CheckBox connectorBox;
    @FXML
    private CheckBox perfectBox;

    private UserGraphicChange ugc;
    private List<ChargingStationBean> chargingStationList = new ArrayList<>();
    private List<ConnectorBean> connectorBeanList = new ArrayList<>();
    private List<ReportBean> report = new ArrayList<>();
    List<Double> start = new ArrayList<>();
    List<Double> end = new ArrayList<>();
    CarBean cb = new CarBean();
    private String csid;

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

        try {
            chargingStationList = MapController.getOnRoute(start, end);
            int i;
            for(i=0; i < chargingStationList.size(); i++){
                listView.getItems().add(i + 1 + ". " + chargingStationList.get(i).getName() + "\n" + chargingStationList.get(i).getFreeformAddress() + "\n     ");
                System.out.println(chargingStationList.get(i).getName());
                System.out.println(chargingStationList.get(i).getId());
                System.out.println(chargingStationList.get(i).getFreeformAddress());
                System.out.println(chargingStationList.get(i).getLatitude());
                System.out.println(chargingStationList.get(i).getLongitude());
                connectorBeanList = MapController.getChargingAvailability(chargingStationList.get(i).getId());
                int k;
                for(k=0; k < connectorBeanList.size(); k++) {
                    System.out.println("Type:"+ connectorBeanList.get(k).getType());
                    System.out.println("Total:"+ connectorBeanList.get(k).getTotal());
                    System.out.println("Available:"+ connectorBeanList.get(k).getAvailable());
                    System.out.println("Occupied:"+ connectorBeanList.get(k).getOccupied());
                    System.out.println("Reserved:"+ connectorBeanList.get(k).getReserved());
                    System.out.println("Unknown:"+ connectorBeanList.get(k).getUnknown());
                    System.out.println("OutOfService:"+ connectorBeanList.get(k).getOutOfService());
                    System.out.println("-----");
                }
                System.out.println("--------------");
            }
        } catch (IOException | ParseException | ChargingStationNotFoundException e) {
            e.printStackTrace();
        }
        webMap.getEngine().load("https://www.google.com/maps/dir/?api=1&origin="+ startField.getText().replaceAll("%20","\\s") +"&destination="+destField.getText().replaceAll("%20"," ")+"&travelmode=driving&waypoint_place_ids=ChIJ5zZx3kNjLxMRAIuXSFIRfwk%ChIJL6pCbOVhLxMRODH8uDzXLDo");
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
            riView.getItems().add(report.get(i).getUsername() + "\nsaid: " + report.get(i).getComment() + "\nDate: " + report.get(i).getDate()+ "\nLikes: " + report.get(i).getPoint() +"\n     ");
        }
    }

    @FXML
    protected void onCheckBox() throws IOException, ParseException, LocationNotFoundException {
        listView.getItems().clear();

        try {
            chargingStationList = MapController.getOnRoute(start, end);
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
        } catch (IOException | ParseException | ChargingStationNotFoundException e) {
            e.printStackTrace();
        }

    }

    @FXML
    protected void onPerfectBox() throws LocationNotFoundException {
        listView.getItems().clear();

        try {
            chargingStationList = MapController.getOnRoute(start, end);
            chargingStationList = RouteController.getPerfectRoute(chargingStationList, start, end, Integer.parseInt(cb.getRange()));
            if(!chargingStationList.isEmpty()) {
                int i;
                for (i = 0; i < chargingStationList.size(); i++) {
                    connectorBeanList = MapController.getChargingAvailability(chargingStationList.get(i).getId());
                    int k;
                    for (k = 0; k < connectorBeanList.size(); k++) {
                        if (Objects.equals(connectorBeanList.get(k).getType(), cb.getConnectorType()))
                            listView.getItems().add(i + 1 + ". " + chargingStationList.get(i).getName() + "\n" + chargingStationList.get(i).getFreeformAddress() + "\n     ");
                    }
                }
            }else{
                alertPane.setVisible(true);
                alertLabel.setText("Your car can reach the destination without any recharge.");
                /*wait(3000);
                alertPane.setVisible(false);*/
            }
        } catch (IOException | ParseException | ChargingStationNotFoundException e) {
            e.printStackTrace();
        }

    }

    @FXML
    protected void onItemSelected() throws IOException, ParseException, LocationNotFoundException {
        connectorView.getItems().clear();
        riView.getItems().clear();
        issuePane.setVisible(false);
        //adsLabel.setText("");

        String selected = listView.getSelectionModel().getSelectedItems().toString();
        int id;
        if (selected.charAt(2) == '.'){
            id = Integer.parseInt(String.valueOf(selected.charAt(1)));
        }else{
            id = Integer.parseInt(selected.substring(1, 3));
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

        /*List<BusinessBean> chargingStationAds = BusinessController.getCSAds(csid);
        assert chargingStationAds != null;
        if (!chargingStationAds.isEmpty()) {
            adsPane.setVisible(true);
            for(i=0; i < Objects.requireNonNull(chargingStationAds).size(); i++) {
                adsLabel.setText("While you are charging try "+ chargingStationAds.get(i).getBusiness() + " " + chargingStationAds.get(i).getAddress());
            }
        }*/
        RouteController.getPerfectRoute(chargingStationList, start, end, Integer.parseInt(cb.getRange()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ugc = UserGraphicChange.getInstance();

        try {
            webMap.getEngine().load("https://www.google.com/maps/dir/?api=1&origin="+ MapController.getLocation() +"&destination=Milan,italy&travelmode=driving&waypoint_place_ids=ChIJ5zZx3kNjLxMRAIuXSFIRfwk%ChIJL6pCbOVhLxMRODH8uDzXLDo");
        } catch (IOException | LocationNotFoundException | ParseException e) {
            e.printStackTrace();
        }

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
        alertPane.setVisible(false);
    }

    public void init() {

    }
}