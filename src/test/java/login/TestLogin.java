package login;

import it.ecteam.easycharge.bean.UserBean;
import it.ecteam.easycharge.controller.LoginController;
import it.ecteam.easycharge.exceptions.LoginEmptyFieldException;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

//Author: Simone Bucci
public class TestLogin {
        @Test
        public void testUsernameEmptyLogin() {

            LoginController lc;
            String output;
            String attended;
            String username;
            String password;
            UserBean gb;

            gb=new UserBean();
            lc=new LoginController();
            output="";
            attended="Username necessary";
            username="";
            password="password";
            gb.setPassword(password);
            gb.setUsername(username);


            try {
                lc.login(gb);
            }catch(LoginEmptyFieldException e) {
                output=e.getMessage();
            }finally {
                assertEquals(attended,output);
            }
        }
    }

