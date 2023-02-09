package it.ecteam.easycharge.controller;

import it.ecteam.easycharge.bean.CarBean;
import it.ecteam.easycharge.bean.ChargingStationBean;
import it.ecteam.easycharge.dao.CarDao;
import it.ecteam.easycharge.dao.UserDao;
import it.ecteam.easycharge.entity.Car;
import it.ecteam.easycharge.entity.ChargingStation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserController {

    public CarBean getCar(String name) {
        CarDao cd = new CarDao();

        Car result = cd.getCar(name);
        if (result == null)	{
            return null;
        } else {
            CarBean c = new CarBean();
            c.setName(result.getName());
            c.setCapacity(result.getCapacity());
            c.setRange(result.getRange());
            c.setConnectorType(result.getConnectorType());
            return c;
        }
    }

    public void changeCar(String username, String model){
        UserDao userDao = new UserDao();

        userDao.changeUserCar(username, model);
    }

    public void setFavorite(String username, String csid){
        UserDao userDao = new UserDao();

        userDao.addFavorite(username, csid);
    }

    public void deleteFavorite(String username, String csid){
        UserDao userDao = new UserDao();

        userDao.removeFavorite(username, csid);
    }

    public static List<ChargingStationBean> getFavorite(String username){
        UserDao ud = new UserDao();

        List<ChargingStation> result = ud.getFavorite(username);
        List<ChargingStationBean> rbl = new ArrayList<>();
        if(result == null){
            return Collections.emptyList();
        }else{
            int i;
            for(i=0; i < result.size(); i++) {
                ChargingStationBean csb = new ChargingStationBean();
                ChargingStation r = result.get(i);

                csb.setId(r.getId());
                rbl.add(csb);
            }
            return rbl;
        }
    }
}
