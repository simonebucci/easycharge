package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.bean.*;
import it.ecteam.easycharge.controller.BusinessController;
import it.ecteam.easycharge.controller.MapController;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import it.ecteam.easycharge.utils.SessionUser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MyBusinessViewController implements Initializable {

    private Stage stage = new Stage();

    @FXML
    private Button homeBtn;
    @FXML
    private Label userMainLabel = new Label();
    @FXML
    private WebView webMap;
    @FXML
    private ListView listView;
    @FXML
    private ListView csView;
    @FXML
    private Label csLabel;


    private BusinessGraphicChange bgc;
    private UserBean ub = SessionUser.getInstance().getSession();

    private List<ChargingStationBean> csList = new ArrayList<>();
    private List<BusinessBean> businessList = new ArrayList<>();
    private String bid;
    private int csid;
    private int id;


    @FXML
    protected void onHomeClick() {
        stage = (Stage) homeBtn.getScene().getWindow();
        this.bgc.toLoggedHome(stage);
    }

    @FXML
    protected void onUserLabelClick() throws IOException {
        stage = (Stage) homeBtn.getScene().getWindow();
        this.bgc.toUser(stage);
    }


    @FXML
    protected void onItemSelected() throws ChargingStationNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException, LocationNotFoundException {
        csView.getItems().clear();
        String selected = listView.getSelectionModel().getSelectedItems().toString();

        if (selected.charAt(2) == '.'){
            id = Integer.parseInt(String.valueOf(selected.charAt(1)));
        }else{
            id = Integer.parseInt(selected.substring(1, 3));
        }
        bid = businessList.get(id-1).getId();

        csLabel.setText(businessList.get(id-1).getBusiness());
        webMap.getEngine().load("https://www.google.it/maps/place/"+businessList.get(id-1).getAddress());

        csList = BusinessController.getBusinessAds(businessList.get(id-1).getId());
        int i;
        for(i=0; i < csList.size(); i++){
            ChargingStationBean cs = new ChargingStationBean();
            cs = MapController.getCSInfo(csList.get(i).getId());
            csView.getItems().add(i+1+". "+cs.getName()+"\n"+cs.getFreeformAddress()+"\n     ");
        }
    }

    @FXML
    protected void onCSSelected() throws ChargingStationNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException, LocationNotFoundException {
        String selected = csView.getSelectionModel().getSelectedItems().toString();

        if (selected.charAt(2) == '.'){
            csid = Integer.parseInt(String.valueOf(selected.charAt(1)));
        }else{
            csid = Integer.parseInt(selected.substring(1, 3));
        }
        String chargingStationID = csList.get(csid-1).getId();
        ChargingStationBean cs = MapController.getCSInfo(chargingStationID);
        webMap.getEngine().load("https://www.google.it/maps/dir/?api=1&origin="+ businessList.get(id-1).getAddress() + "&destination="+cs.getFreeformAddress());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        webMap.getEngine().load("https://www.google.it/maps/");


        businessList = BusinessController.getBusiness(ub.getUsername());
        int i;
        for(i=0; i < businessList.size(); i++){
            listView.getItems().add(i + 1 + ". " + businessList.get(i).getBusiness() + "\n" + businessList.get(i).getAddress() + "\n     ");
        }

        this.bgc = BusinessGraphicChange.getInstance();
        //init nameBar

        if (ub != null) {
            userMainLabel.setText(ub.getUsername());
        }
    }
    public void init() {

    }
}
