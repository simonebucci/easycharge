package it.ecteam.easycharge.viewcontroller;

import it.ecteam.easycharge.bean.BusinessBean;
import it.ecteam.easycharge.bean.UserBean;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;

import java.util.Objects;

public class RegistrationController {
    private RegistrationController(){}
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
    public static BusinessBean createBusinessBean(javafx.scene.control.TextField usernameTextField, PasswordField passwordPasswordField, javafx.scene.control.TextField emailTextField, ComboBox<String> userType, javafx.scene.control.TextField businessTextField, javafx.scene.control.TextField baddressTextField, javafx.scene.control.TextArea adTextArea) {
        BusinessBean bu = new BusinessBean();
        bu.setUsername(usernameTextField.getText());
        bu.setPassword(passwordPasswordField.getText());
        bu.setRole(userType.getValue());
        bu.setBusiness(businessTextField.getText());
        bu.setEmail(emailTextField.getText());
        bu.setAddress(baddressTextField.getText());
        bu.setAd(adTextArea.getText());
        return bu;
    }
}
