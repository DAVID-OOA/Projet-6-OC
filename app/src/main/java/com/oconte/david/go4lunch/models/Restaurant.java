package com.oconte.david.go4lunch.models;

public class Restaurant {

    public String urlPicture;
    public String username;
    public String uid;
    public String idRestaurant;


    public Restaurant(String idRestaurant, String username, String uid, String urlPicture) {
        this.idRestaurant = idRestaurant;
        this.username = username;
        this.uid = uid;
        this.urlPicture = urlPicture;
    }

    // GETTERS
    public String getUrlPicture() {
        return urlPicture;
    }

    public String getUsername() {
        return username;
    }

    public String getUid() {
        return uid;
    }

    public String getIdRestaurant() {
        return idRestaurant;
    }

    // SETTERS
    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setIdRestaurant(String idRestaurant) {
        this.idRestaurant = idRestaurant;
    }
}
