package it.ecteam.easycharge.dao;

import it.ecteam.easycharge.entity.Business;
import it.ecteam.easycharge.entity.ChargingStation;
import it.ecteam.easycharge.utils.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BusinessDao extends DaoTemplate{

    //create a business user on the db
    public Boolean createBusinessUser(String username, String password, String email, String role, String business, String address, String ad) {
        return this.execute(() -> {
            Connection con = DataBaseConnection.getConnection();
            String sql = "call easycharge.add_businessuser(?, ?, ?, ?, ?, ?, ?);\r\n";


            try (PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setString(1, username);
                stm.setString(2, email);
                stm.setString(3, password);
                stm.setString(4, role);
                stm.setString(5, business);
                stm.setString(6, address);
                stm.setString(7, ad);
                stm.executeUpdate();
            }

            return true;
        }) != null;
    }

    //get the business user's business name
    public Business getBusiness(String username){
        return this.execute(new DaoAction<Business>() {
            @Override
            public Business act() throws ClassNotFoundException, SQLException {
                Connection conn = null;
                Business business = null;
                conn = DataBaseConnection.getConnection();

                String sql = "call easycharge.get_business(?);\r\n";
                try(PreparedStatement stm = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE)){
                    stm.setString(1, username);
                    try (ResultSet rs = stm.executeQuery()) {

                        if (!rs.first()) // rs not empty
                            return null;

                        boolean moreThanOne = rs.first() && rs.next();
                        assert !moreThanOne;
                        rs.first();

                        String name = rs.getString("business_name");
                        String address = rs.getString("business_address");
                        String id = rs.getString("idbusiness");
                        String ad = rs.getString("ad");
                        business = new Business(name, address, username, id, ad);
                    }
                }

                return business;
            }
        });
    }

    //return true if a cs has an ad for that business
    public Boolean businessAd(String business, String csID) {
        return this.execute(() -> {
            Connection con = DataBaseConnection.getConnection();
            String sql = "call easycharge.business_ad(?, ?);\r\n";


            try (PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setString(1, business);
                stm.setString(2, csID);
                stm.executeUpdate();
            }

            return true;
        }) != null;
    }

    //remove a business ad assigned to a cs
    public Boolean removeAd(String business, String csID) {
        return this.execute(() -> {
            Connection con = DataBaseConnection.getConnection();
            String sql = "call easycharge.remove_ad(?, ?);\r\n";


            try (PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setString(1, business);
                stm.setString(2, csID);
                stm.executeUpdate();
            }

            return true;
        }) != null;
    }

    //get the list of cs that has an ad for specified business
    public List<ChargingStation> getBusinessAds(String business){
        List<ChargingStation> ret = this.execute(new DaoAction<List<ChargingStation>>() {
            @Override
            public List<ChargingStation> act() throws ClassNotFoundException, SQLException {
                Connection conn = null;
                String sql = null;
                PreparedStatement stm = null;
                List<ChargingStation> cs = new ArrayList<>();
                try {
                    conn = DataBaseConnection.getConnection();
                    sql = "call easycharge.get_business_ad(?);\r\n";
                    stm = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
                    stm.setString(1, business);
                    try (ResultSet rs = stm.executeQuery()) {

                        if (!rs.first()) // rs not empty
                            return Collections.emptyList();	//return empty list*/

                        do{
                            String id = rs.getString("charging_station_idcharging_station");

                            cs.add(new ChargingStation(id));
                        } while (rs.next());
                    }
                } finally {
                    assert stm != null;
                    stm.close();
                }
                return cs;
            }
        });
        if (ret != null)
            return ret;

        return Collections.emptyList();
    }

    //get the list of businesses that has an ad to the specified charging station
    public List<Business> getCSAds(String csid){
        List<Business> ret = this.execute(new DaoAction<List<Business>>() {
            @Override
            public List<Business> act() throws ClassNotFoundException, SQLException {
                Connection conn = null;
                String sql = null;
                PreparedStatement stm = null;
                List<Business> b = new ArrayList<>();
                try {
                    conn = DataBaseConnection.getConnection();
                    sql = "call easycharge.get_cs_ad(?);\r\n";
                    stm = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
                    stm.setString(1, csid);
                    try (ResultSet rs = stm.executeQuery()) {

                        if (!rs.first()) // rs not empty
                            return Collections.emptyList();	//return empty list*/

                        do{
                            String name = rs.getString("business_name");
                            String address = rs.getString("business_address");
                            String id = rs.getString("business_idbusiness");
                            String ad = rs.getString("ad");
                            b.add(new Business(name, address, id, ad));
                        } while (rs.next());
                    }
                } finally {
                    assert stm != null;
                    stm.close();
                }
                return b;
            }
        });
        if (ret != null)
            return ret;

        return Collections.emptyList();
    }

    //modify a specified ad
    public Boolean updateAd(String username, String ad) {
        return this.execute(() -> {
            Connection con = DataBaseConnection.getConnection();
            String sql = "call easycharge.update_ad(?, ?);\r\n";

            try (PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setString(1, username);
                stm.setString(2, ad);
                stm.executeUpdate();
            }

            return true;
        }) != null;
    }

}
