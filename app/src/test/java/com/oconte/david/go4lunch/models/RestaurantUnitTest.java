package com.oconte.david.go4lunch.models;

import static org.junit.Assert.assertEquals;

import com.oconte.david.go4lunch.models.Restaurant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RestaurantUnitTest {

    private Restaurant restaurant;

    public String urlPicture;
    public String username;
    public String uid;
    public String idRestaurant;
    public String addressRestaurant;

    @Before
    public void setup() {
        username = "Miam Miam";
        uid = "123456789";
        idRestaurant = "Ch_123456789";
        urlPicture = "http://photo";
        addressRestaurant = "38 rte du repas";

        restaurant = new Restaurant(idRestaurant,username,uid,urlPicture, addressRestaurant);
    }

    @Test
    public void getCorrectInfoFromRestaurant() {
        assertEquals(uid, restaurant.getUid());
        assertEquals(username, restaurant.getUsername());
        assertEquals(idRestaurant, restaurant.getIdRestaurant());
        assertEquals(urlPicture, restaurant.getUrlPicture());
    }


}
