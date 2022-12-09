package it.ecteam.easycharge.entity;


public class User extends GeneralUser{
    private String name;
    private String role;
    private String car;
    private String profilePicture;

    public User(String username, String name, String profilePicture) {
        this.username = username;
        this.name = name;
        this.profilePicture = profilePicture;
    }

    public User(String username, String role){
        this.username = username;
        this.role = role;
    }

    public User(String car){
        this.car = car;
    }

    public String getName() {
        return name;
    }

    public String getCarModel() { return car; }

    public String getRole(){ return role; }

    public String getProfilePicture() {
        return profilePicture;
    }

}
