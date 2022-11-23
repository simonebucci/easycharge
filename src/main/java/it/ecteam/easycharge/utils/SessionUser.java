package it.ecteam.easycharge.utils;

import it.ecteam.easycharge.bean.UserBean;

public class SessionUser {

    private UserBean userSession=null;
    private static SessionUser myInstance=null;

    private SessionUser() {

    }

    public static SessionUser getInstance() {
        if(myInstance==null) {
            myInstance=new SessionUser();
        }
        return myInstance;
    }


    public void setSession(UserBean userSession) {

        if(this.userSession==null) {
            this.userSession=userSession;
        }

    }

    public UserBean getSession() {
        return this.userSession;
    }


    public void closeSession() {
        this.userSession=null;
    }

}
