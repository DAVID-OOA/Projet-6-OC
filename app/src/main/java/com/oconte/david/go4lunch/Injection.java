package com.oconte.david.go4lunch;

import androidx.test.espresso.idling.CountingIdlingResource;

public class Injection {

    public static final CountingIdlingResource resource = new CountingIdlingResource("GooglePlaceIdling");

    public static GooglePlaceService getService() {
        return GooglePlaceFactory.getRetrofit().create(GooglePlaceService.class);
    }


    //For Call getRestaurantDetail
    public static GooglePlaceCallRestaurantDetails getRestaurantDetail (GooglePlaceService googlePlaceService, CountingIdlingResource resource) {
        GooglePlaceCallRestaurantDetails restaurantDetails = new GooglePlaceCallRestaurantDetails(googlePlaceService, resource);
        return restaurantDetails;
    }




}
