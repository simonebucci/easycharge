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
        attended="Server returned HTTP response code: 400 for URL: https://api.tomtom.com/routing/1/calculateRoute/12.493321%2C12.493321%3A37.828789%2C-122.485964/json?key=csPBICaGiqrDG1YIKGXg4alunzBPez4I";
        start.add(0,12.493321);
        start.add(1,12.493321);
        end.add(0,37.828789);
        end.add(1,-122.485964);

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
