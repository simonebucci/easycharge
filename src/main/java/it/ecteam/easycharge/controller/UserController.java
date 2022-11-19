package it.ecteam.easycharge.controller;

import it.ecteam.easycharge.bean.CarBean;
import it.ecteam.easycharge.dao.CarDao;
import it.ecteam.easycharge.entity.Car;

public class UserController {

    public CarBean getCar(String username) {
        CarDao cd = new CarDao();

        Car result = cd.getCar(username);
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

}
