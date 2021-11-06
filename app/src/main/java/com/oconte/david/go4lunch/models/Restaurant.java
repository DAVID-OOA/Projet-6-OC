package com.oconte.david.go4lunch.models;

public class Restaurant {

    public String urlPhoto;
    public String userName;
    public String idUser;
    public String idRestaurant;


    public Restaurant( String idRestaurant, String userName, String idUser, String urlPhoto) {
        this.idRestaurant = idRestaurant;
        this.userName = userName;
        this.idUser = idUser;
        this.urlPhoto = urlPhoto;
    }

    // GETTERS
    public String getUrlPhoto() {
        return urlPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getIdRestaurant() {
        return idRestaurant;
    }

    //SETTERS
    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setIdRestaurantClick(String idRestaurantClick) {
        this.idRestaurant = idRestaurantClick;
    }
}
