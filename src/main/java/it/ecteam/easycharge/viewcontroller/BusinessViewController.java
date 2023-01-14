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
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class BusinessViewController implements Initializable {

    private Stage stage = new Stage();

    @FXML
    private Button homeBtn;
    @FXML
    private Button buyBtn;
    @FXML
    private Label userMainLabel = new Label();
    @FXML
    private WebView webMap;
    @FXML
    private ListView listView;
    @FXML
    private Slider slider;
    @FXML
    private Label rangeLabel;
    @FXML
    private Label csLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label distanceLabel;
    @FXML
    private Label desLabel;
    @FXML
    private Label bLabel;
    @FXML
    private Pane paymentPane;


    private BusinessGraphicChange bgc;
    private UserBean ub = SessionUser.getInstance().getSession();

    private List<ChargingStationBean> chargingStationList = new ArrayList<>();
    private List<BusinessBean> businessList = new ArrayList<>();
    private String csid;


    @FXML
    protected void onHomeClick() {
        stage = (Stage) homeBtn.getScene().getWindow();
        this.bgc.toLoggedHome(stage);
    }

    @FXML
    protected void onMyBusinessClick() {
        stage = (Stage) homeBtn.getScene().getWindow();
        this.bgc.toMyBusiness(stage);
    }

    @FXML
    protected void onUserLabelClick() throws IOException {
        stage = (Stage) homeBtn.getScene().getWindow();
        this.bgc.toUser(stage);
    }

    @FXML
    protected void onSliderMove() {
        rangeLabel.setText(((int) slider.getValue())/1000 + "km");
    }

    @FXML
    protected void onSliderRelease() {
        rangeLabel.setText(((int) slider.getValue())/1000 + "km");
        listView.getItems().clear();

        try {
            chargingStationList = MapController.getNearby((int) slider.getValue()); //radius range 1 to 50000
                int i;
                for (i = 0; i < chargingStationList.size(); i++) {
                    listView.getItems().add(i+1+". "+chargingStationList.get(i).getName()+"\n"+chargingStationList.get(i).getFreeformAddress()+"\n     ");
                }
        } catch (IOException | ParseException | LocationNotFoundException | java.text.ParseException | ChargingStationNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onItemSelected() {
        buyBtn.setVisible(true);
        desLabel.setVisible(true);
        bLabel.setVisible(false);

        String selected = listView.getSelectionModel().getSelectedItems().toString();
        int id;
        if (selected.charAt(2) == '.'){
            id = Integer.parseInt(String.valueOf(selected.charAt(1)));
        }else{
            id = Integer.parseInt(selected.substring(1, 3));
        }
        csid = chargingStationList.get(id-1).getId();

        csLabel.setText(chargingStationList.get(id-1).getName());
        webMap.getEngine().load("https://www.google.it/maps/place/"+chargingStationList.get(id-1).getLatitude()+","+chargingStationList.get(id-1).getLongitude());
        businessList = BusinessController.getBusiness(userMainLabel.getText());
        assert businessList != null;
        addressLabel.setText(businessList.get(0).getAddress());
        try {
            Long distance = MapController.getDistance(MapController.getCoordinates((businessList.get(0).getAddress())), MapController.getCoordinates(chargingStationList.get(id - 1).getFreeformAddress()));
            distanceLabel.setText("This charging station is "+(distance)/1000+"km from your business");
            List<ChargingStationBean> chargingStationAds = BusinessController.getBusinessAds(businessList.get(0).getId());
            int i;
            for(i=0; i < Objects.requireNonNull(chargingStationAds).size(); i++){
                if(Objects.equals(chargingStationAds.get(i).getId(), csid)){
                    buyBtn.setVisible(false);
                    desLabel.setVisible(false);
                    bLabel.setVisible(true);
                }
            }
        }catch(IOException | ParseException | LocationNotFoundException e){
            e.printStackTrace();
        }
    }

    @FXML
    protected void onBuyClicked(){
        paymentPane.setVisible(true);
    }

    @FXML
    protected void onPaymentClose(){
        paymentPane.setVisible(false);
    }

    @FXML
    protected void onConfirmationClick(){
        BusinessController bc = new BusinessController();
        bc.businessAd(businessList.get(0).getId(), csid);
        paymentPane.setVisible(false);
        buyBtn.setVisible(false);
        desLabel.setVisible(false);
        bLabel.setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        webMap.getEngine().load("https://www.google.it/maps/");
        rangeLabel.setText(((int) slider.getValue())/1000 + "km");
        paymentPane.setVisible(false);
        buyBtn.setVisible(false);
        desLabel.setVisible(false);
        bLabel.setVisible(false);

        try{
            chargingStationList = MapController.getNearby((int)slider.getValue()); //radius range 1 to 50000
            int i;
            for(i=0; i < chargingStationList.size(); i++){
                listView.getItems().add(i+1+". "+chargingStationList.get(i).getName()+"\n"+chargingStationList.get(i).getFreeformAddress()+"\n     ");
            }
        } catch (IOException | ParseException | LocationNotFoundException | java.text.ParseException | ChargingStationNotFoundException e) {
            e.printStackTrace();
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
