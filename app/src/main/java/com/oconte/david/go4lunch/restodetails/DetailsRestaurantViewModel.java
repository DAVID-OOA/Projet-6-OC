package com.oconte.david.go4lunch.restodetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.oconte.david.go4lunch.Injection;
import com.oconte.david.go4lunch.models.Restaurant;
import com.oconte.david.go4lunch.workMates.UserRepository;

import java.util.Objects;

public class DetailsRestaurantViewModel extends ViewModel {



    private final RestaurantDetailRepository restaurantDetailRepository;

    private final MutableLiveData<Restaurant> restaurantMutableLiveData = new MutableLiveData<>();

    public LiveData<Restaurant> getRestaurantsMutableLiveData() {
        return restaurantMutableLiveData;
    }


    public DetailsRestaurantViewModel(UserRepository userRepository, RestaurantDetailRepository restaurantDetailRepository) {
        this.restaurantDetailRepository = restaurantDetailRepository;


    }

    public void getDataRestaurantClick() {

    }
}
