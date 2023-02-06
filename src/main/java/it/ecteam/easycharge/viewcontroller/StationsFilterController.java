package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.bean.CarBean;
import it.ecteam.easycharge.bean.ChargingStationBean;
import it.ecteam.easycharge.bean.ConnectorBean;
import it.ecteam.easycharge.controller.ChargingStationController;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class StationsFilterController {

    public static void filterByConnector(CheckBox connectorBox, List<ChargingStationBean> chargingStationList, ListView listView, String space, CarBean cb) throws ChargingStationNotFoundException, IOException, ParseException {
        for (int i = 0; i < chargingStationList.size(); i++) {
            if (!connectorBox.isSelected() || checkConnectorAvailability(chargingStationList.get(i).getId(), cb)) {
                listView.getItems().add(i + 1 + ". " + chargingStationList.get(i).getName() + "\n" + chargingStationList.get(i).getFreeformAddress() + space);
            }
        }
    }

    public static boolean checkConnectorAvailability(String chargingStationId, CarBean cb) throws ChargingStationNotFoundException, IOException, ParseException {
        List<ConnectorBean> connectorBeanList = ChargingStationController.getChargingAvailability(chargingStationId);
        for(int k = 0; k < connectorBeanList.size(); k++) {
            if (Objects.equals(connectorBeanList.get(k).getType(), "Chademo")) {
                return Objects.equals(connectorBeanList.get(k).getType(), cb.getConnectorType());
            } else if (Objects.equals(connectorBeanList.get(k).getType().substring(0, 13), cb.getConnectorType().substring(0, 13))) {
                return true;
            }
        }
        return false;
    }
}
