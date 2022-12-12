package it.ecteam.easycharge.controller;

import it.ecteam.easycharge.bean.BusinessBean;
import it.ecteam.easycharge.bean.ChargingStationBean;
import it.ecteam.easycharge.dao.BusinessDao;
import it.ecteam.easycharge.entity.Business;
import it.ecteam.easycharge.entity.ChargingStation;

import java.util.ArrayList;
import java.util.List;

public class BusinessController {

    public static List<BusinessBean> getBusiness(String username){
        BusinessDao bud = new BusinessDao();

        List<Business> result = bud.getBusiness(username);
        List<BusinessBean> bl = new ArrayList<>();
        if(result == null){
            return null;
        }else{
            int i;
            for(i=0; i < result.size(); i++) {
                BusinessBean bub = new BusinessBean();
                Business r = result.get(i);

                bub.setBusiness(r.getName());
                bub.setAddress(r.getAddress());
                bub.setUsername(r.getUsername());
                bub.setId(r.getId());
                bl.add(bub);
            }
            return bl;
        }
    }

    public boolean businessAd(String business, String csid){
        BusinessDao businessDao = new BusinessDao();

        return businessDao.businessAd(business, csid);
    }

    public static List<BusinessBean> getCSAds(String csid){
        BusinessDao bud = new BusinessDao();

        List<Business> result = bud.getCSAds(csid);
        List<BusinessBean> bl = new ArrayList<>();
        if(result == null){
            return null;
        }else{
            int i;
            for(i=0; i < result.size(); i++) {
                BusinessBean bub = new BusinessBean();
                Business r = result.get(i);

                bub.setBusiness(r.getName());
                bub.setAddress(r.getAddress());
                bub.setId(r.getId());
                bl.add(bub);
            }
            return bl;
        }
    }

    public static List<ChargingStationBean> getBusinessAds(String business){
        BusinessDao bud = new BusinessDao();

        List<ChargingStation> result = bud.getBusinessAds(business);
        List<ChargingStationBean> csl = new ArrayList<>();
        if(result == null){
            return null;
        }else{
            int i;
            for(i=0; i < result.size(); i++) {
                ChargingStationBean csb = new ChargingStationBean();
                ChargingStation cs = result.get(i);

                csb.setId(cs.getId());
                csl.add(csb);
            }
            return csl;
        }
    }

}
