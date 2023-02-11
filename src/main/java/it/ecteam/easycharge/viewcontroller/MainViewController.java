package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.bean.*;
import it.ecteam.easycharge.controller.*;
import it.ecteam.easycharge.controller.UserController;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
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
import java.security.SecureRandom;
import java.sql.Date;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private Label pointLabel = new Label();
    @FXML
    private Label adsLabel;
    @FXML
    private TextArea reportTextArea;
    @FXML
    private Pane reportPane;
    @FXML
    private Pane issuePane;
    @FXML
    private Pane adsPane;
    @FXML
    private CheckBox connectorBox;
    @FXML
    private CheckBox favoriteBox;

    private UserGraphicChange ugc;
    private UserBean ub = SessionUser.getInstance().getSession();

    private List<ChargingStationBean> chargingStationList = new ArrayList<>();
    private List<ConnectorBean> connectorBeanList = new ArrayList<>();
    private List<ReportBean> report = new ArrayList<>();
    private String csid;
    private static final  String SPACE = "\n     ";
    private final SecureRandom r = new SecureRandom();
    protected static final Logger logger = Logger.getLogger("GUI");
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
            chargingStationList = ChargingStationController.getNearby((int) slider.getValue()); //radius range 1 to 50000
            if(connectorBox != null && connectorBox.isSelected()){
                CarBean cb = uc.getCar(userMainLabel.getText());
                int i;
                for (i = 0; i < chargingStationList.size(); i++) {
                    connectorBeanList = ChargingStationController.getChargingAvailability(chargingStationList.get(i).getId());
                    int k;
                    for(k = 0; k < connectorBeanList.size(); k++) {
                        if (Objects.equals(connectorBeanList.get(k).getType(), cb.getConnectorType()))
                            listView.getItems().add(i + 1 + ". " + chargingStationList.get(i).getName() + "\n" + chargingStationList.get(i).getFreeformAddress() + SPACE);
                    }
                }
            }else{
                int i;
                for (i = 0; i < chargingStationList.size(); i++) {
                    listView.getItems().add(i+1+". "+chargingStationList.get(i).getName()+"\n"+chargingStationList.get(i).getFreeformAddress()+ SPACE);
                }
            }

        } catch (IOException | ParseException | LocationNotFoundException | java.text.ParseException | ChargingStationNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onConnectorBox() throws ChargingStationNotFoundException, IOException, ParseException {
        listView.getItems().clear();
        UserController uc = new UserController();
        CarBean cb = uc.getCar(userMainLabel.getText());
        try {
            chargingStationList = ChargingStationController.getNearby((int) slider.getValue()); //radius range 1 to 50000
        } catch (IOException | ParseException | LocationNotFoundException | java.text.ParseException | ChargingStationNotFoundException e) {
            e.printStackTrace();
        }
        StationsFilterController.filterByConnector(connectorBox, chargingStationList, listView, SPACE, cb);

    }

    @FXML
    protected void onItemSelected(){
        connectorView.getItems().clear();
        riView.getItems().clear();
        issuePane.setVisible(false);
        adsPane.setVisible(false);
        adsLabel.setText("");

        String selected = listView.getSelectionModel().getSelectedItems().toString();
        int id;
        if (selected.charAt(2) == '.'){
            id = Integer.parseInt(String.valueOf(selected.charAt(1)));
        }else{
            id = Integer.parseInt(selected.substring(1, 3));
        }
        report = ReportController.getUserReport(String.valueOf(chargingStationList.get(id - 1).getId()));
        csid = chargingStationList.get(id-1).getId();

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
            connectorBeanList = ChargingStationController.getChargingAvailability(chargingStationList.get(id-1).getId());

        } catch (IOException | ChargingStationNotFoundException | ParseException e) {
            e.printStackTrace();
        }

        for(i=0; i < connectorBeanList.size(); i++) {
            connectorView.getItems().add("Type:"+ connectorBeanList.get(i).getType() + "\nTotal: " + connectorBeanList.get(i).getTotal() + "\nAvailable: " + connectorBeanList.get(i).getAvailable() + "\nOccupied: " + connectorBeanList.get(i).getOccupied() + "\nReserved: " + connectorBeanList.get(i).getReserved() + "\nUnknown: " + connectorBeanList.get(i).getUnknown() + "\nOutOfService: " + connectorBeanList.get(i).getOutOfService() + "\nProvider: InstantEnergy 0,60€/kWh" + SPACE);
        }

        issueBtn.setVisible(!report.isEmpty());

        List<BusinessBean> chargingStationAds = BusinessController.getCSAds(csid);
        if (!chargingStationAds.isEmpty()) {
            adsPane.setVisible(true);
            int rand = r.nextInt((chargingStationAds).size());
            adsLabel.setText(chargingStationAds.get(rand).getAd()+" located in " + chargingStationAds.get(rand).getAddress());
        }
    }

    @FXML
    protected void onSendReportClick() {
        ReportController reportController = new ReportController();
        String selected = listView.getSelectionModel().getSelectedItems().toString();
        int id = Integer.parseInt(String.valueOf(selected.charAt(1)));

        boolean res = reportController.reportCS(chargingStationList.get(id-1).getId(), userMainLabel.getText(), reportTextArea.getText());

        if(res){
            alertLabel.setText("Report sent, thank you!");
        }else{
            alertLabel.setText("Something went wrong, try again!");
        }
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
            riView.getItems().add(report.get(i).getUsername() + "\nsaid: " + report.get(i).getComment() + "\nDate: " + report.get(i).getDate()+ "\nLikes: " + report.get(i).getPoint() + SPACE);
        }
    }

    @FXML
    protected void onPointBtnClick(){
        ReportController rc = new ReportController();

        String selected = riView.getSelectionModel().getSelectedItems().toString();
        if(!Objects.equals(selected, "[]")) {
            String[] arrOfStr = selected.split("\n");

            String username = arrOfStr[0];
            username = username.replace("[", "");

            String date = arrOfStr[2];
            date = date.replace("Date: ", "");
            Date dateFormatted = java.sql.Date.valueOf(date);
            List<ReportBean> rb = ReportController.getPointGiver(username, csid, dateFormatted);

            int i;
            int verify = 0;
            for(i=0; i<rb.size(); i++){
                if(Objects.equals(rb.get(i).getUsername(), ub.getUsername())){
                    verify = 1;
                }
            }
            if (Objects.equals(ub.getUsername(), username)) {
                pointLabel.setText("You can't give a like to your self.");
                pointLabel.setTextFill(Color.RED);
            } else if(verify == 1){
                pointLabel.setText("You already gave a like to this report.");
                pointLabel.setTextFill(Color.RED);
            }
            else{
                rc.givePointToUser(username, csid, dateFormatted, ub.getUsername());
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

        uc.deleteFavorite(ub.getUsername(), csid);
        favoriteOnBtn.setVisible(false);
        favoriteOffBtn.setVisible(true);
    }

    @FXML
    protected void onMyFavoriteClick(){
        listView.getItems().clear();
        List<ChargingStationBean> favoriteCSB = UserController.getFavorite(ub.getUsername());
        ChargingStationBean csb = new ChargingStationBean();
        int i;
        if (!favoriteBox.isSelected()) {
            for(i=0; i<chargingStationList.size(); i++) {
                listView.getItems().add(i + 1 + ". " + chargingStationList.get(i).getName() + "\n" + chargingStationList.get(i).getFreeformAddress() + SPACE);
            }
        }else {
            for (i = 0; i < favoriteCSB.size(); i++) {
                try {
                    csb = ChargingStationController.getCSInfo(favoriteCSB.get(i).getId());
                } catch (IOException | java.text.ParseException | LocationNotFoundException | ParseException |
                         ChargingStationNotFoundException e) {
                    logger.log(Level.WARNING, e.toString());
                }
                listView.getItems().add(i + 1 + ". " + csb.getName() + "\n" + csb.getFreeformAddress() + SPACE);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        issuePane.setVisible(false);
        issueBtn.setVisible(false);
        adsPane.setVisible(false);

        webMap.getEngine().load("https://www.google.it/maps/");
        rangeLabel.setText(((int) slider.getValue())/1000 + "km");

        try {
            chargingStationList = ChargingStationController.getNearby((int)slider.getValue()); //radius range 1 to 50000
            int i;
            for(i=0; i < chargingStationList.size(); i++){
                listView.getItems().add(i+1+". "+chargingStationList.get(i).getName()+"\n"+chargingStationList.get(i).getFreeformAddress()+ SPACE);
            }
        } catch (IOException | ParseException | LocationNotFoundException | java.text.ParseException | ChargingStationNotFoundException e) {
            logger.log(Level.WARNING, e.toString());
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
}