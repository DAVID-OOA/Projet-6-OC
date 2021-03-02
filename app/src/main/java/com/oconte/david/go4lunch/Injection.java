package com.oconte.david.go4lunch;

import androidx.test.espresso.idling.CountingIdlingResource;

import com.oconte.david.go4lunch.api.GooglePlaceFactory;
import com.oconte.david.go4lunch.api.GooglePlaceService;
import com.oconte.david.go4lunch.listView.GooglePlaceCallRestaurantNearBy;

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

    //For Call getRestaurantDetail
    public static GooglePlaceCallRestaurantNearBy getRestaurantNearBy (GooglePlaceService googlePlaceService, CountingIdlingResource resource) {
        GooglePlaceCallRestaurantNearBy restaurantNearBy = new GooglePlaceCallRestaurantNearBy(googlePlaceService, resource);
        return restaurantNearBy;
    }




}
