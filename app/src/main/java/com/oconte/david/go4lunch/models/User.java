package com.oconte.david.go4lunch.models;

public class User {

    private String uid;
    private String username;
    private String email;
    private String urlPicture;
    private String idRestaurantPicked;
    private String nameRestaurantPicked;
    private String adressRestaurantPicked;
    private String photoUrlRestaurantpicked;

    public User() {
    }

    public User(String uid, String username, String email, String urlPicture, String idRestaurantPicked, String nameRestaurantPicked, String adressRestaurantPicked, String photoUrlRestaurantpicked) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.urlPicture = urlPicture;
        this.idRestaurantPicked = idRestaurantPicked;
        this.nameRestaurantPicked = nameRestaurantPicked;
        this.adressRestaurantPicked = adressRestaurantPicked;
        this.photoUrlRestaurantpicked = photoUrlRestaurantpicked;
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

    public String getNameRestaurantPicked() {
        return nameRestaurantPicked;
    }

    public String getAdressRestaurantPicked() {
        return adressRestaurantPicked;
    }

    public String getPhotoUrlRestaurantpicked() {
        return photoUrlRestaurantpicked;
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

    public void setNameRestaurantPicked(String nameRestaurantPicked) {
        this.nameRestaurantPicked = nameRestaurantPicked;
    }

    public void setAdressRestaurantPicked(String adressRestaurantPicked) {
        this.adressRestaurantPicked = adressRestaurantPicked;
    }

    public void setPhotoUrlRestaurantpicked(String photoUrlRestaurantpicked) {
        this.photoUrlRestaurantpicked = photoUrlRestaurantpicked;
    }
}
