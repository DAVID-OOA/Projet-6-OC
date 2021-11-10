package com.oconte.david.go4lunch.models;

public class User {

    private String idUser;
    private String username;
    private String email;
    private String urlPicture;

    public User() {
    }

    public User(String idUser, String username, String email, String urlPicture) {
        this.idUser = idUser;
        this.username = username;
        this.email = email;
        this.urlPicture = urlPicture;
    }


    // GETTERS
    public String getIdUser() {
        return idUser;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    // SETTERS
    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }


}
