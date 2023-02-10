package it.ecteam.easycharge.entity;


public class User{
    protected String username;

    protected String password;
    private String role;
    private String car;

    public User(String username, String role){
        this.username = username;
        this.role = role;
    }

    public User(String car){
        this.car = car;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public String getCar() { return car; }

    public String getRole(){ return role; }

}
