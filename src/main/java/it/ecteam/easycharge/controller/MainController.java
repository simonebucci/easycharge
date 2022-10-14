package it.ecteam.easycharge.controller;

import it.ecteam.easycharge.MainApplication;
import it.ecteam.easycharge.exceptions.FileNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import it.ecteam.easycharge.utils.FileManager;
import it.ecteam.easycharge.utils.GoogleMapBoundary;
import it.ecteam.easycharge.utils.JsonParser;
import it.ecteam.easycharge.utils.JsonRequest;
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
        //FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("auth-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("route-logged-view.fxml"));
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

        //webMap.getEngine().load("https://www.google.it/maps/search/ev+charging+stations/");
        webMap.getEngine().load("https://www.google.com/maps/search/?api=1&map_action=map&query=ev+charging+stations");
/*        try {
            System.out.println(GoogleMapBoundary.locateAddress("via fosso acqua mariana 97"));
        } catch (IOException | ParseException | LocationNotFoundException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }*/
        try {
            GoogleMapBoundary.getNearby(4000);
        } catch (IOException | ParseException | LocationNotFoundException | java.text.ParseException e) {
            e.printStackTrace();
        }

/*        try {
            JsonRequest.getNearby(4000);
        } catch (IOException | java.text.ParseException | LocationNotFoundException | ParseException e) {
            e.printStackTrace();
        }

        try {
            JsonParser.parseNearby(0);
        } catch (LocationNotFoundException | ParseException | FileNotFoundException e) {
            e.printStackTrace();
        }*/
    }

}