package it.ecteam.easycharge.controller;

import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.dao.UserDao;
import it.ecteam.easycharge.entity.User;
import it.ecteam.easycharge.exceptions.LoginEmptyFieldException;

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

        return userDao.createUser(user.getUsername(), user.getPassword(), user.getEmail());
    }
}
