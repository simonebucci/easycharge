package map;


import it.ecteam.easycharge.controller.MapController;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMapController {
    @Test
    public void testLocation() {
    //Author: Simone Bucci
        MapController mc;
        String output;
        String attended;


        mc = MapController.getInstance();
        output="";
        attended="No location found";

        try {
            mc.getLocation();
        }catch(LocationNotFoundException | IOException | ParseException e) {
            output=e.getMessage();
        }finally {
            assertEquals(attended,output);
        }
    }

    @Test
    public void testDistance() {
    //Author: Miriana Marchi
        MapController mc;
        String output;
        String attended;
        List<Double> start = null;
        List<Double> end = null;

        mc= MapController.getInstance();
        output="";
        attended="No distance found for this locations";
        start.add(0.00);
        start.add(0.00);
        end.add(0.00);
        end.add(0.00);

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
    //Author: Miriana Marchi
        MapController mc;
        String output;
        String attended;
        String location;

        mc=MapController.getInstance();
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
