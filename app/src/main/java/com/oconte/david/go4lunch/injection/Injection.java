package com.oconte.david.go4lunch.injection;

import androidx.test.espresso.idling.CountingIdlingResource;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oconte.david.go4lunch.api.GooglePlaceFactory;
import com.oconte.david.go4lunch.api.GooglePlaceService;
import com.oconte.david.go4lunch.listView.RestaurantRepository;
import com.oconte.david.go4lunch.restodetails.RestaurantDetailRepository;
import com.oconte.david.go4lunch.restodetails.ViewModelFactory;
import com.oconte.david.go4lunch.workMates.UserRepository;

public class Injection {

    public static final CountingIdlingResource resource = new CountingIdlingResource("GooglePlaceIdling");

    public static GooglePlaceService getService() {
        return GooglePlaceFactory.getRetrofit().create(GooglePlaceService.class);
    }

    //For Call getRestaurantNearBy
    public static RestaurantRepository getRestaurantNearBy (GooglePlaceService googlePlaceService, CountingIdlingResource resource) {
        return new RestaurantRepository(googlePlaceService, resource);
    }

    //For Call getUserRepository
    public static UserRepository getUserRepository (FirebaseAuth firebaseAuth, FirebaseFirestore firebaseFirestore) {
        return new UserRepository(firebaseAuth, firebaseFirestore);
    }

    public static RestaurantDetailRepository provideRestaurantDetailsRepository(FirebaseAuth firebaseAuth, FirebaseFirestore firebaseFirestore) {
        return new RestaurantDetailRepository(firebaseAuth, firebaseFirestore);
    }

    //For Call ViewModelFactory
    public static ViewModelFactory provideViewModelFactory(FirebaseAuth firebaseAuth, FirebaseFirestore firebaseFirestore){
        RestaurantDetailRepository restaurantDetailRepository = provideRestaurantDetailsRepository(firebaseAuth,firebaseFirestore);
        UserRepository userRepository = getUserRepository(firebaseAuth, firebaseFirestore);

        return  new ViewModelFactory(restaurantDetailRepository, userRepository);
    }

}
