package it.ecteam.easycharge.controller;

import it.ecteam.easycharge.MainApplication;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import it.ecteam.easycharge.utils.FileManager;
import it.ecteam.easycharge.utils.MapBoundary;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends StackPane implements Initializable  {
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
        FileManager file = new FileManager();
        if(file.fileExists("user")) {
            String name = file.readFile("user");
            userLabel.setText(name);
        }
        webMap.getEngine().load("https://www.google.it/maps/search/ev+charging+stations/");
        //webMap.getEngine().load("https://www.google.com/maps/search/?api=1&map_action=map&query=ev+charging+stations");
        //UserDao.createUser("spillo","spillo","spillo@miao.it");

        try {
            MapBoundary.getNearby(4000);
        } catch (IOException | ParseException | LocationNotFoundException | java.text.ParseException | ChargingStationNotFoundException e) {
            e.printStackTrace();
        }

    }

}