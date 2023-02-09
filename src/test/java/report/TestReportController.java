package report;

import it.ecteam.easycharge.controller.ReportController;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

//Author: Simone Bucci
public class TestReportController {
    @Test
    public void testSendReport() {
        //This test tests if a report is sent successfully to the database

        ReportController rc = new ReportController();
        String station;
        String user;
        String comment;

        station = "123456789";
        user = "user";
        comment = "comment";

        boolean result = rc.reportCS(station, user, comment);

        assertFalse(result);
    }
}
