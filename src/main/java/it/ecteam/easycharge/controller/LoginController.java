package it.ecteam.easycharge.controller;

import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.dao.UserDao;
import it.ecteam.easycharge.entity.User;
import it.ecteam.easycharge.exceptions.LoginEmptyFieldException;

public class LoginController {

    public UserBean login(UserBean userBean) throws LoginEmptyFieldException {
        UserDao gud = new UserDao();

        if(userBean.getUsername().equals("")) {
            throw new LoginEmptyFieldException("Username necessary");
        }
        if(userBean.getPassword().equals("")) {
            throw new LoginEmptyFieldException("Password necessary");
        }

        User result = gud.findUser(userBean.getUsername(), userBean.getPassword());
        if (result == null)	{
            return null;
        } else {
            UserBean gu = new UserBean();
            gu.setUsername(result.getUsername());
            gu.setPassword(result.getPassword());
            gu.setRole(result.getRole());
            return gu;
        }
    }

    public void createUser(UserBean user){
        UserDao userDao = new UserDao();

        userDao.createUser(user.getUsername(), user.getPassword(), user.getEmail());
    }
}
