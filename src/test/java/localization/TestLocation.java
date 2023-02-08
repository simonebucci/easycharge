package localization;


import it.ecteam.easycharge.controller.MapController;
import it.ecteam.easycharge.exceptions.LocationNotFoundException;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

//Author: Simone Bucci
public class TestLocation {
    @Test
    public void testNoLocation() {

        MapController mc;
        String output;
        String attended;


        mc=new MapController();
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
}
