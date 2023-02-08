package distance;

import it.ecteam.easycharge.controller.MapController;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//Author: Miriana Marchi
public class TestDistance {
    @Test
    public void testNoCoordinates() {

        MapController mc;
        String output;
        String attended;
        List<Double> start = null;
        List<Double> end = null;

        mc=new MapController();
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
}