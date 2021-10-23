package com.oconte.david.go4lunch.restodetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.oconte.david.go4lunch.Injection;
import com.oconte.david.go4lunch.models.Restaurant;
import com.oconte.david.go4lunch.models.User;
import com.oconte.david.go4lunch.workMates.UserRepository;

import java.util.List;

public class DetailsRestaurantViewModel extends ViewModel {

    /*private final UserRepository mUserRepository;

    private final MutableLiveData<User> users = new MutableLiveData<>();

    public DetailsRestaurantViewModel(UserRepository mUserRepository) {
        this.mUserRepository = Injection.getUserRepository();
    }

    public LiveData<User> getUsers() {
        return users;
    }*/

    // contient l'information du restaurant cliqué
    private String myClickRestaurant = null;
    public void setMyClickRestaurant(String myClickRestaurant) {
        this.myClickRestaurant = myClickRestaurant;
    }

    public String getMyClickRestaurant() {
        return myClickRestaurant;
    }

}
