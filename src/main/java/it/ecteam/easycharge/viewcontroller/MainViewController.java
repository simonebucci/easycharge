package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.MainApplication;
import it.ecteam.easycharge.bean.ChargingStationBean;
import it.ecteam.easycharge.bean.ConnectorBean;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import it.ecteam.easycharge.utils.FileManager;
import it.ecteam.easycharge.controller.MapController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainViewController extends StackPane implements Initializable  {
    private Stage stage = new Stage();

    @FXML
    private Button loginBtn;
    @FXML
    private Button homeBtn;
    @FXML
    private Label userLabel;
    @FXML
    private Button routeBtn;
    @FXML
    private WebView webMap;
    @FXML
    private ListView listView;

    @FXML
    protected void onLoginClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
        stage = (Stage) loginBtn.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scene);
    }

    @FXML
    protected void onRegisterClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("register-view.fxml"));
        stage = (Stage) loginBtn.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scene);
    }

    @FXML
    protected void onHomeClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        stage = (Stage) homeBtn.getScene().getWindow();
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
    protected void onUserClick() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("settings-view.fxml"));
        stage = (Stage) userLabel.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scene);
    }

    @FXML
    protected void onRouteLoggedClick() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("route-logged-view.fxml"));
        stage = (Stage) userLabel.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scene);
    }

    @FXML
    protected void onRouteClick() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("auth-view.fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("route-logged-view.fxml"));
        stage = (Stage) routeBtn.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<ChargingStationBean> chargingStationList = new ArrayList<>();
        List<ConnectorBean> connectorBeanList = new ArrayList<>();

        webMap.getEngine().load("https://www.google.it/maps/search/ev+charging+stations/");

        try {
            chargingStationList = MapController.getNearby(4000); //radius range 1 to 50000
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
        } catch (IOException | ParseException | LocationNotFoundException | java.text.ParseException | ChargingStationNotFoundException e) {
            e.printStackTrace();
        }

    }

}