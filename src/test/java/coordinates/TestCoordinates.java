package coordinates;

import it.ecteam.easycharge.controller.MapController;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

//Author: Miriana Marchi
public class TestCoordinates {
    @Test
    public void testNoChargingStation() {

        MapController mc;
        String output;
        String attended;
        String location;

        mc=new MapController();
        output="";
        attended="No coordinates found for this location";
        location = "roma";

        try {
            mc.getCoordinates(location);
        } catch (LocationNotFoundException | IOException | ParseException e) {
            output=e.getMessage();
        } finally {
            assertEquals(attended,output);
        }
    }
}