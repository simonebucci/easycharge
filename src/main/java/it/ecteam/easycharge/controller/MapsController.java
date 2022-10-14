package it.ecteam.easycharge.controller;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.service.geocoding.GeocodingService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;

import javax.swing.JFrame;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MapsController implements Initializable{

    private GoogleMap map;

    private GeocodingService geocodingService;
    private double lat;
    private double lon;

    @FXML
    private Pane mapPane;
    @FXML
    private WebView web;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        GoogleMapView mapView = new GoogleMapView(Locale.getDefault().getLanguage(), "AIzaSyDMIX9Bb7__E4QHG19h9jzH-SDEbN7IE5k");
        //geocodingService = new GeocodingService();
        mapPane.getChildren().add(mapView);
    }


}