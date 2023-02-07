package it.ecteam.easycharge.controller;

import it.ecteam.easycharge.bean.BusinessBean;
import it.ecteam.easycharge.bean.CarBean;
import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.dao.BusinessDao;
import it.ecteam.easycharge.dao.CarDao;
import it.ecteam.easycharge.dao.UserDao;
import it.ecteam.easycharge.entity.Car;
import it.ecteam.easycharge.entity.User;
import it.ecteam.easycharge.exceptions.LoginEmptyFieldException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoginController {

    public UserBean login(UserBean userBean) throws LoginEmptyFieldException {
        UserDao ud = new UserDao();

        if(userBean.getUsername().equals("")) {
            throw new LoginEmptyFieldException("Username necessary");
        }
        if(userBean.getPassword().equals("")) {
            throw new LoginEmptyFieldException("Password necessary");
        }

        User result = ud.findUser(userBean.getUsername(), userBean.getPassword());
        if (result == null)	{
            return null;
        } else {
            UserBean u = new UserBean();
            u.setUsername(result.getUsername());
            u.setPassword(result.getPassword());
            u.setRole(result.getRole());
            return u;
        }
    }



    public boolean createUser(UserBean user){
        UserDao userDao = new UserDao();

        return userDao.createUser(user.getUsername(), user.getPassword(), user.getEmail(), user.getRole(), user.getCar());
    }

    public boolean createBusinessUser(BusinessBean user){
        BusinessDao businessDao = new BusinessDao();

        return businessDao.createBusinessUser(user.getUsername(), user.getPassword(), user.getEmail(), user.getRole(), user.getBusiness(), user.getAddress(), user.getAd());
    }

    public static List<CarBean> getCar() {
        CarDao cd = new CarDao();

        List<Car> result = cd.getModel();

        if (result == null)	{
            return Collections.emptyList();
        } else {
            List<CarBean> lcb = new ArrayList<>();

            int i;
            for(i=0; i < result.size(); i++) {
                CarBean cb = new CarBean();
                cb.setName(result.get(i).getName());
                lcb.add(cb);
            }

            return lcb;
        }
    }
}
