package com.oconte.david.go4lunch.models;

import com.google.android.libraries.places.api.model.PhotoMetadata;

import java.io.Serializable;
import java.util.List;

public class PlaceTestForAutocompleteToDetails implements Serializable {

    String idRestaurant;
    String name;
    String adress;
    Double rating;
    String webSite;
    String phoneNumber;
    transient List<PhotoMetadata> metadata;

    public PlaceTestForAutocompleteToDetails(String name, String adress, Double rating, String webSite, String idRestaurant, String phoneNumber, List<PhotoMetadata> metadata) {
        this.name = name;
        this.adress = adress;
        this.metadata = metadata;
        this.rating = rating;
        this.webSite = webSite;
        this.idRestaurant = idRestaurant;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }


    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public List<PhotoMetadata> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<PhotoMetadata> metadata) {
        this.metadata = metadata;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(String idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
