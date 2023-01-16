package it.ecteam.easycharge.controller;

import it.ecteam.easycharge.bean.BusinessBean;
import it.ecteam.easycharge.bean.CarBean;
import it.ecteam.easycharge.bean.ChargingStationBean;
import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.dao.CarDao;
import it.ecteam.easycharge.dao.UserDao;
import it.ecteam.easycharge.entity.Car;
import it.ecteam.easycharge.entity.ChargingStation;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public boolean changeCar(String username, String model){
        UserDao userDao = new UserDao();

        return userDao.changeUserCar(username, model);
    }

    public boolean setFavorite(String username, String csid){
        UserDao userDao = new UserDao();

        return userDao.addFavorite(username, csid);
    }

    public boolean deleteFavorite(String username, String csid){
        UserDao userDao = new UserDao();

        return userDao.removeFavorite(username, csid);
    }

    public static List<ChargingStationBean> getFavorite(String username){
        UserDao ud = new UserDao();

        List<ChargingStation> result = ud.getFavorite(username);
        List<ChargingStationBean> rbl = new ArrayList<>();
        if(result == null){
            return null;
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

    //registrations methods
    public static boolean isInvalidData(ComboBox<String> userType, javafx.scene.control.TextField usernameTextField, PasswordField passwordPasswordField, javafx.scene.control.TextField emailTextField) {
        return Objects.equals(userType.getValue(), null) || usernameTextField.getText().isBlank() || passwordPasswordField.getText().isBlank() || emailTextField.getText().isBlank();
    }

    public static boolean isInvalidDataForUser(String userRole, ComboBox<String> modelBox) {
        return userRole.equals("user") && Objects.equals(modelBox.getValue(), null);
    }
    public static boolean isInvalidDataForBusiness(String userRole, javafx.scene.control.TextField businessTextField) {
        return userRole.equals("business") && businessTextField.getText().isBlank();
    }
    public static UserBean createUserBean(javafx.scene.control.TextField usernameTextField, PasswordField passwordPasswordField, javafx.scene.control.TextField emailTextField, ComboBox<String> userType, ComboBox<String> modelBox) {
        UserBean u = new UserBean();
        u.setUsername(usernameTextField.getText());
        u.setPassword(passwordPasswordField.getText());
        u.setEmail(emailTextField.getText());
        u.setRole(userType.getValue());
        u.setCar(modelBox.getValue());
        return u;
    }
    public static BusinessBean createBusinessBean(javafx.scene.control.TextField usernameTextField, PasswordField passwordPasswordField, javafx.scene.control.TextField emailTextField, ComboBox<String> userType, javafx.scene.control.TextField businessTextField, javafx.scene.control.TextField baddressTextField) {
        BusinessBean bu = new BusinessBean();
        bu.setUsername(usernameTextField.getText());
        bu.setPassword(passwordPasswordField.getText());
        bu.setRole(userType.getValue());
        bu.setBusiness(businessTextField.getText());
        bu.setEmail(emailTextField.getText());
        bu.setAddress(baddressTextField.getText());
        return bu;
    }
}
