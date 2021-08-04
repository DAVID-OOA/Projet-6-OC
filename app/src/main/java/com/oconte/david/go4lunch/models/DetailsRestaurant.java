package com.oconte.david.go4lunch.models;

import android.widget.ImageView;

import java.io.Serializable;

public class DetailsRestaurant implements Serializable {

    ImageView imageresto;
    String nameRestaurant;
    ImageView imageStar1;
    ImageView imageStar2;
    ImageView imageStar3;
    String adressRestaurant;
    //pickRestaurant;

    public DetailsRestaurant() {
    }

    public DetailsRestaurant(ImageView imageresto, String nameRestaurant, ImageView imageStar1, ImageView imageStar2, ImageView imageStar3, String adressRestaurant) {
        this.imageresto = imageresto;
        this.nameRestaurant = nameRestaurant;
        this.imageStar1 = imageStar1;
        this.imageStar2 = imageStar2;
        this.imageStar3 = imageStar3;
        this.adressRestaurant = adressRestaurant;
    }


    // GETTERS
    public ImageView getImageresto() {
        return imageresto;
    }

    public String getNameRestaurant() {
        return nameRestaurant;
    }

    public ImageView getImageStar1() {
        return imageStar1;
    }

    public ImageView getImageStar2() {
        return imageStar2;
    }

    public ImageView getImageStar3() {
        return imageStar3;
    }

    public String getAdressRestaurant() {
        return adressRestaurant;
    }


    //SETTERS
    public void setImageresto(ImageView imageresto) {
        this.imageresto = imageresto;
    }

    public void setNameRestaurant(String nameRestaurant) {
        this.nameRestaurant = nameRestaurant;
    }

    public void setImageStar1(ImageView imageStar1) {
        this.imageStar1 = imageStar1;
    }

    public void setImageStar2(ImageView imageStar2) {
        this.imageStar2 = imageStar2;
    }

    public void setImageStar3(ImageView imageStar3) {
        this.imageStar3 = imageStar3;
    }

    public void setAdressRestaurant(String adressRestaurant) {
        this.adressRestaurant = adressRestaurant;
    }
}
