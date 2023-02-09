package route;

import it.ecteam.easycharge.controller.RouteController;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
//Author: Miriana Marchi
public class TestRouteController {
    @Test
    public void testPerfectRoute() {

        RouteController rc;
        String output;
        String attended;
        List<Double> start = new ArrayList<> (); ;
        List<Double> end = new ArrayList<>();;
        String capacity;

        rc = RouteController.getInstance();
        output="";
        attended="No perfect route found";
        start.add(0.00);
        start.add(0.00);
        end.add(0.00);
        end.add(0.00);
        capacity = "1";

        try {
            rc.getPerfectRoute(start,end, capacity);
        } catch (LocationNotFoundException | IOException | ParseException | ChargingStationNotFoundException e) {
            output=e.getMessage();
        } finally {
            assertEquals(attended,output);
        }
    }
}
