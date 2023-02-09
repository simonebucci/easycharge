package chargingstation;

import it.ecteam.easycharge.controller.ChargingStationController;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

//Author: Simone Bucci
public class TestChargingStationController {
    @Test
    public void testNoChargingStation() {

        ChargingStationController csc;
        String output;
        String attended;
        String id;

        csc = ChargingStationController.getInstance();
        output="";
        attended="No EV charging station availability found";
        id = "123456789";

        try {
            csc.getChargingAvailability(id);
        } catch (ChargingStationNotFoundException | IOException | ParseException e) {
            output=e.getMessage();
        } finally {
            assertEquals(attended,output);
        }
    }
}
