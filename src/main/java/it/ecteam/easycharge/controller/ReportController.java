package it.ecteam.easycharge.controller;

import it.ecteam.easycharge.bean.ReportBean;
import it.ecteam.easycharge.dao.ReportDao;
import it.ecteam.easycharge.dao.UserDao;
import it.ecteam.easycharge.entity.Report;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ReportController {

    public boolean reportCS(String station, String user, String comment){
        ReportDao reportDao = new ReportDao();

        return reportDao.reportCS(user, station, comment);
    }

    public static List<ReportBean> getUserReport(String station){
        ReportDao reportDao = new ReportDao();

        List<Report> result = reportDao.getReport(station);
        List<ReportBean> rbl = new ArrayList<>();
        if(result == null){
            return null;
        }else{
            int i;
            for(i=0; i < result.size(); i++) {
                ReportBean rb = new ReportBean();
                Report r = result.get(i);

                rb.setChargingStationID(r.getChargingStationID());
                rb.setUsername(r.getUsername());
                rb.setComment(r.getComment());
                rb.setDate(r.getDate());
                rb.setPoint(r.getPoint());
                rbl.add(rb);
            }
            return rbl;
        }
    }

    public boolean givePointToUser(String user, String station, Date date){
        ReportDao reportDao = new ReportDao();

        return reportDao.givePoint(user, station, date);
    }

}
