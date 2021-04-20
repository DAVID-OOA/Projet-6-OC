package com.oconte.david.go4lunch;

import androidx.test.espresso.idling.CountingIdlingResource;

import com.oconte.david.go4lunch.api.GooglePlaceFactory;
import com.oconte.david.go4lunch.api.GooglePlaceService;
import com.oconte.david.go4lunch.listView.RestaurantRepository;
import com.oconte.david.go4lunch.restoDetails.RestaurantDetailRepository;

public class Injection {

    public static final CountingIdlingResource resource = new CountingIdlingResource("GooglePlaceIdling");

    public static GooglePlaceService getService() {
        return GooglePlaceFactory.getRetrofit().create(GooglePlaceService.class);
    }


    //For Call getRestaurantDetail
    public static RestaurantDetailRepository getRestaurantDetail (GooglePlaceService googlePlaceService, CountingIdlingResource resource) {
        RestaurantDetailRepository restaurantDetails = new RestaurantDetailRepository(googlePlaceService, resource);
        return restaurantDetails;
    }

    //For Call getRestaurantDetail
    public static RestaurantRepository getRestaurantNearBy (GooglePlaceService googlePlaceService, CountingIdlingResource resource) {
        RestaurantRepository restaurantNearBy = new RestaurantRepository(googlePlaceService, resource);
        return restaurantNearBy;
    }




}
