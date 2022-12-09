package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.MainApplication;
import it.ecteam.easycharge.bean.ChargingStationBean;
import it.ecteam.easycharge.bean.ConnectorBean;
import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import it.ecteam.easycharge.controller.MapController;
import it.ecteam.easycharge.utils.SessionUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RouteViewController extends StackPane implements Initializable {
    private Stage stage = new Stage();

    @FXML
    private Button loginBtn;
    @FXML
    private Button homeBtn;
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

    private UserGraphicChange ugc;

    @FXML
    protected void onLoginClick() throws IOException {
        //fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-view.fxml")));
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
        stage = (Stage) loginBtn.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scene);
    }

    @FXML
    protected void onRegisterClick() throws IOException {
        //fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-view.fxml")));
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("register-view.fxml"));
        stage = (Stage) loginBtn.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scene);
    }

    @FXML
    protected void onHomeClick() throws IOException {
        stage = (Stage) homeBtn.getScene().getWindow();
        this.ugc.toLoggedHome(stage);
    }

    @FXML
    protected void onHomeLoggedClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("logged-view.fxml"));
        stage = (Stage) homeBtn.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scene);
    }

    @FXML
    protected void onUserClick() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("settings-view.fxml"));
        stage = (Stage) userLabel.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scene);
    }
    @FXML
    protected void onRouteClick() throws IOException, ParseException, LocationNotFoundException {
        List<Double> start = MapController.getCoordinates(startField.getText());
        List<Double> end =MapController.getCoordinates(destField.getText());

        List<ChargingStationBean> chargingStationList = new ArrayList<>();
        List<ConnectorBean> connectorBeanList = new ArrayList<>();
        try {
            chargingStationList = MapController.getOnRoute(start, end);
            int i;
            for(i=0; i < chargingStationList.size(); i++){
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

        userLabel.setText(ub.getUsername());
    }

    public void init() {

    }
}