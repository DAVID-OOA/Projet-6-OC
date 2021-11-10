package com.oconte.david.go4lunch.models;

public class Restaurant {

    public String urlPicture;
    public String username;
    public String idUser;
    public String idRestaurant;


    public Restaurant( String idRestaurant, String username, String idUser, String urlPicture) {
        this.idRestaurant = idRestaurant;
        this.username = username;
        this.idUser = idUser;
        this.urlPicture = urlPicture;
    }

    // GETTERS
    public String getUrlPicture() {
        return urlPicture;
    }

    public String getUsername() {
        return username;
    }

    public String getIdUser() {
        return idUser;
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

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setIdRestaurant(String idRestaurant) {
        this.idRestaurant = idRestaurant;
    }
}
