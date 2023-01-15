package it.ecteam.easycharge.controller;

import it.ecteam.easycharge.bean.CarBean;
import it.ecteam.easycharge.bean.ChargingStationBean;
import it.ecteam.easycharge.bean.ConnectorBean;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class StationsController {

    public static void filterByConnector(CheckBox connectorBox, List<ChargingStationBean> chargingStationList, List<ConnectorBean> connectorBeanList, ListView listView, String SPACE, CarBean cb) throws ChargingStationNotFoundException, IOException, ParseException {
        if(!connectorBox.isSelected()){
            int i;
            for (i = 0; i < chargingStationList.size(); i++) {
                listView.getItems().add(i+1+". "+chargingStationList.get(i).getName() + "\n"+ chargingStationList.get(i).getFreeformAddress() + SPACE);
            }
        }else{
            int i;
            for (i = 0; i < chargingStationList.size(); i++) {
                connectorBeanList = MapController.getChargingAvailability(chargingStationList.get(i).getId());
                int k;
                for(k = 0; k < connectorBeanList.size(); k++) {
                    if(Objects.equals(connectorBeanList.get(k).getType(), "Chademo")){
                        if (Objects.equals(connectorBeanList.get(k).getType(), cb.getConnectorType())){
                            listView.getItems().add(i + 1 + ". " + chargingStationList.get(i).getName() + "\n" + chargingStationList.get(i).getFreeformAddress() + SPACE);
                            k = connectorBeanList.size();
                        }
                    }else if(Objects.equals(connectorBeanList.get(k).getType().substring(0, 13), cb.getConnectorType().substring(0, 13))){
                        listView.getItems().add(i + 1 + ". " + chargingStationList.get(i).getName() + "\n" + chargingStationList.get(i).getFreeformAddress() + SPACE);
                        k = connectorBeanList.size();
                    }
                }
            }
        }
    }
}
