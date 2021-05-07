package com.oconte.david.go4lunch;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.oconte.david.go4lunch.models.ApiRestaurantDetails;
import com.oconte.david.go4lunch.models.Result;
import com.oconte.david.go4lunch.restoDetails.RestaurantDetailRepository;

public class MainActivityViewModel extends ViewModel {

    private final RestaurantDetailRepository mRestaurantDetailRepository;
    private final MutableLiveData<Result> apiDetailsResponseMutableLiveData = new MutableLiveData<>();


    public MainActivityViewModel(RestaurantDetailRepository mRestaurantDetailRepository) {
        this.mRestaurantDetailRepository = mRestaurantDetailRepository;
    }

    public LiveData<Result> getRestaurantDetailsLiveData() {
        return apiDetailsResponseMutableLiveData;
    }

    public void getRestaurantsDetails() {

        //Classe Anonyme
        mRestaurantDetailRepository.getRestaurantDetails(new RestaurantDetailRepository.Callbacks() {
            @Override
            public void onResponse(@Nullable ApiRestaurantDetails apiRestaurantDetails) {

                if (apiRestaurantDetails != null) {
                    apiDetailsResponseMutableLiveData.postValue(apiRestaurantDetails.getResult());
                }
            }

            @Override
            public void onFailure() {

            }
        }, "placeId");

    }
}
