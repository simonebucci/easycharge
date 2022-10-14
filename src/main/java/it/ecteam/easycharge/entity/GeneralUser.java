package it.ecteam.easycharge.entity;

public class GeneralUser {
    protected String username;
    protected String password;
    private String role;

    public GeneralUser() {
        this.username = "";
        this.password = "";
        this.role = "";
    }

    public GeneralUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
