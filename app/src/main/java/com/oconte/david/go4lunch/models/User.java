package com.oconte.david.go4lunch.models;

public class User {

    private String uid;
    private String username;
    private String email;
    private String urlPicture;
    private String idRestaurantPicked;

    public User() {
    }

    public User(String uid, String username, String email, String urlPicture, String idRestaurantPicked) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.urlPicture = urlPicture;
        this.idRestaurantPicked = idRestaurantPicked;
    }


    // GETTERS
    public String getUid() {
        return uid;
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

    public String getIdRestaurantPicked() {
        return idRestaurantPicked;
    }

    // SETTERS
    public void setUid(String uid) {
        this.uid = uid;
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

    public void setIdRestaurantPicked(String idRestaurantPicked) {
        this.idRestaurantPicked = idRestaurantPicked;
    }
}
