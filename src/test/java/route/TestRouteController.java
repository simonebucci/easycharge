package route;

import it.ecteam.easycharge.controller.RouteController;
import it.ecteam.easycharge.exceptions.ChargingStationNotFoundException;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
//Author: Miriana Marchi
public class TestRouteController {
    @Test
    public void testPerfectRoute() {

        RouteController rc;
        String output;
        String attended;
        List<Double> start = null;
        List<Double> end = null;
        String capacity;

        rc=new RouteController();
        output="";
        attended="No perfect route found";
        start.add(0.00);
        start.add(0.00);
        end.add(0.00);
        end.add(0.00);
        capacity = "200";

        try {
            rc.getPerfectRoute(start,end, capacity);
        } catch (LocationNotFoundException | IOException | ParseException | ChargingStationNotFoundException e) {
            output=e.getMessage();
        } finally {
            assertEquals(attended,output);
        }
    }
}
