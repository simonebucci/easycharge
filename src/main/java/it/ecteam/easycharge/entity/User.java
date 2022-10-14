package it.ecteam.easycharge.entity;


public class User extends GeneralUser{
    private String name;
    private String surname;
    private String profilePicture;

    public User(String username, String password, String name,
                String surname, String profilePicture) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.profilePicture = profilePicture;
    }

    public User(String username, String name, String surname, String profilePicture) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.profilePicture = profilePicture;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

}
