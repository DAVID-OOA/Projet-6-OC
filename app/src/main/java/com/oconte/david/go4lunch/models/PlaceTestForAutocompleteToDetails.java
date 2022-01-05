package com.oconte.david.go4lunch.models;

import android.net.Uri;

import com.google.android.libraries.places.api.model.PhotoMetadata;

import java.io.Serializable;
import java.util.List;

public class PlaceTestForAutocompleteToDetails implements Serializable {

    String name;
    String adress;
    String iconUrl;
    Double rating;
    String webSite;

    public PlaceTestForAutocompleteToDetails(String name, String adress, Double rating, String webSite) {
        this.name = name;
        this.adress = adress;
        //this.iconUrl = iconUrl;
        this.rating = rating;
        this.webSite = webSite;
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

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

}
