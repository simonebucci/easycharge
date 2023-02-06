package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.bean.*;
import it.ecteam.easycharge.controller.BusinessController;
import it.ecteam.easycharge.controller.ChargingStationController;
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
    private Label csLabel;
    @FXML
    private Label bLabel;
    @FXML
    private Label pLabel;

    private BusinessGraphicChange bgc;
    private UserBean ub = SessionUser.getInstance().getSession();
    private List<ChargingStationBean> csList = new ArrayList<>();
    private BusinessBean business = new BusinessBean();
    private int csid;
    private String chargingStationID;


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
        pLabel.setText("Thank you for your purchase, your business is advertised to this charging station's users.");
        String selected = listView.getSelectionModel().getSelectedItems().toString();

        if (selected.charAt(2) == '.'){
            csid = Integer.parseInt(String.valueOf(selected.charAt(1)));
        }else{
            csid = Integer.parseInt(selected.substring(1, 3));
        }
        chargingStationID = csList.get(csid-1).getId();
        ChargingStationBean cs = ChargingStationController.getCSInfo(chargingStationID);
        webMap.getEngine().load("https://www.google.it/maps/dir/?api=1&origin="+ business.getAddress() + "&destination="+cs.getFreeformAddress());
        csLabel.setText(cs.getName());
        pLabel.setVisible(true);
    }

    @FXML
    protected void onRemoveClick(){
        BusinessController bc = new BusinessController();
        bc.removeAd(business.getId(), chargingStationID);
        pLabel.setText("Your subscription has been cancelled.");
        listView.getItems().clear();
        int i;
        csList = BusinessController.getBusinessAds(business.getId());
        for(i=0; i < csList.size(); i++){
            ChargingStationBean cs = new ChargingStationBean();
            try {
                cs = ChargingStationController.getCSInfo(csList.get(i).getId());
            } catch (IOException | ParseException | LocationNotFoundException | org.json.simple.parser.ParseException |
                     ChargingStationNotFoundException e) {
                throw new RuntimeException(e);
            }
            listView.getItems().add(i+1+". "+cs.getName()+"\n"+cs.getFreeformAddress()+"\n     ");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        webMap.getEngine().load("https://www.google.it/maps/");
        pLabel.setVisible(false);

        business = BusinessController.getBusiness(ub.getUsername());
        bLabel.setText(business.getBusiness());
        int i;
        csList = BusinessController.getBusinessAds(business.getId());
        for(i=0; i < csList.size(); i++){
            ChargingStationBean cs = new ChargingStationBean();
            try {
                cs = ChargingStationController.getCSInfo(csList.get(i).getId());
            } catch (IOException | ParseException | LocationNotFoundException | org.json.simple.parser.ParseException |
                     ChargingStationNotFoundException e) {
                throw new RuntimeException(e);
            }
            listView.getItems().add(i+1+". "+cs.getName()+"\n"+cs.getFreeformAddress()+"\n     ");
        }

        this.bgc = BusinessGraphicChange.getInstance();
        //init nameBar

        if (ub != null) {
            userMainLabel.setText(ub.getUsername());
        }
    }
}
