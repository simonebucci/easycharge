package it.ecteam.easycharge.bean;

import java.io.Serializable;

public class UserBean implements Serializable {

    protected String username;
    protected String password;
    protected String email;
    protected String role;
    protected String car;

    public void setUsername(String username) { this.username = username; }

    public String getUsername() { return this.username; }

    public void setPassword(String password) { this.password = password; }

    public String getPassword() { return this.password; }

    public void setEmail(String email) { this.email = email; }

    public String getEmail() { return this.email; }

    public void setRole(String role) { this.role = role; }

    public String getRole() { return this.role; }

    public void setCar(String car) { this.car = car; }

    public String getCar() { return this.car; }

}
