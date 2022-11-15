package it.ecteam.easycharge.controller;

import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.dao.UserDao;
import it.ecteam.easycharge.entity.User;

public class RegisterController {

    public void createUser(UserBean user){
        UserDao userDao = new UserDao();

        userDao.createUser(user.getUsername(), user.getPassword(), user.getEmail());
    }
}
