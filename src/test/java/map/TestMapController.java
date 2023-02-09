package map;


import it.ecteam.easycharge.controller.MapController;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//Author: Miriana Marchi
public class TestMapController {

    @Test
    public void testDistance() {

        MapController mc;
        String output;
        String attended;
        List<Double> start = new ArrayList<>();
        List<Double> end = new ArrayList<>();

        mc= MapController.getInstance();
        output="";
        attended="No distance found for this locations";
        start.add(12.00);
        start.add(13.00);
        end.add(24.00);
        end.add(23.00);

        try {
            mc.getDistance(start,end);
        } catch (LocationNotFoundException | IOException | ParseException e) {
            output=e.getMessage();
        } finally {
            assertEquals(attended,output);
        }
    }

    @Test
    public void testCoordinates() {

        MapController mc;
        String output;
        String attended;
        String location;

        mc=MapController.getInstance();
        output="";
        attended="No coordinates found for this location";
        location = "postochenonesiste";

        try {
            mc.getCoordinates(location);
        } catch (LocationNotFoundException | IOException | ParseException e) {
            output=e.getMessage();
        } finally {
            assertEquals(attended,output);
        }
    }
}
