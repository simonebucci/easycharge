package it.ecteam.easycharge.dao;

import it.ecteam.easycharge.entity.Report;
import it.ecteam.easycharge.utils.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportDao extends DaoTemplate{

    //write a user report for cs to the db
    public Boolean reportCS(String username, String csID, String comment) {
        return this.execute(() -> {
            Connection con = DataBaseConnection.getConnection();
            String sql = "call easycharge.report(?, ?, ?);\r\n";


            try (PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setString(1, username);
                stm.setString(2, csID);
                stm.setString(3, comment);
                stm.executeUpdate();
            }

            return true;
        }) != null;
    }

    //get the charging station's report list
    public List<Report> getReport(String station){
        List<Report> ret = this.execute(new DaoAction<List<Report>>() {
            @Override
            public List<Report> act() throws ClassNotFoundException, SQLException {
                Connection conn = null;
                String sql = null;
                PreparedStatement stm = null;
                List<Report> r = new ArrayList<>();
                try {
                    conn = DataBaseConnection.getConnection();
                    sql = "call easycharge.get_report(?);\r\n";
                    stm = conn.prepareStatement(sql);
                    stm.setString(1, station);
                    try (ResultSet rs = stm.executeQuery()) {

                        if(rs.next()){
                            do{
                                String stationLoaded = rs.getString("charging_station_idcharging_station");
                                String user = rs.getString("user_username");
                                String comment = rs.getString("comment");
                                Date date = rs.getDate("date");
                                int point = rs.getInt("point");
                                r.add(new Report(stationLoaded, user, comment, date, point));
                            } while (rs.next());
                        }else{
                            return Collections.emptyList();	//return empty list
                        }
                    }
                } finally {
                    assert stm != null;
                    stm.close();
                }
                return r;
            }
        });
        if (ret != null)
            return ret;

        return Collections.emptyList();
    }

    //give a like to a user's report
    public Boolean givePoint(String username, String csID, Date date, String giver) {
        return this.execute(() -> {
            Connection con = DataBaseConnection.getConnection();
            String sql = "call easycharge.give_point(?, ?, ?, ?);\r\n";


            try (PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setString(1, username);
                stm.setString(2, csID);
                stm.setDate(3, date);
                stm.setString(4, giver);
                stm.executeUpdate();
            }

            return true;
        }) != null;
    }

    //get usernames that liked a report
    public List<Report> getPointGiver(String username, String csID, Date date){
        List<Report> ret = this.execute(new DaoAction<List<Report>>() {
            @Override
            public List<Report> act() throws ClassNotFoundException, SQLException {
                Connection conn = null;
                String sql = null;
                PreparedStatement stm = null;
                List<Report> r = new ArrayList<>();
                try {
                    conn = DataBaseConnection.getConnection();
                    sql = "call easycharge.get_point(?, ?, ?);\r\n";
                    stm = conn.prepareStatement(sql);
                    stm.setString(1, username);
                    stm.setString(2, csID);
                    stm.setDate(3, date);
                    try (ResultSet rs = stm.executeQuery()) {

                        if(rs.next()){
                            do{
                                String user = rs.getString("giver_username");
                                r.add(new Report(user));
                            } while (rs.next());
                        }else{
                            return Collections.emptyList();	//return empty list
                        }
                    }
                } finally {
                    assert stm != null;
                    stm.close();
                }
                return r;
            }
        });
        if (ret != null)
            return ret;

        return Collections.emptyList();
    }


}
