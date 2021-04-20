package com.oconte.david.go4lunch.restoDetails;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.oconte.david.go4lunch.Injection;
import com.oconte.david.go4lunch.listView.RestaurantRepository;
import com.oconte.david.go4lunch.models.ApiNearByResponse;
import com.oconte.david.go4lunch.models.ApiRestaurantDetails;
import com.oconte.david.go4lunch.models.Result;

import java.util.List;

public class DetailsRestaurantViewModel extends ViewModel {

    private final RestaurantDetailRepository mRestaurantDetailRepository;
    private final MutableLiveData<Result> apiDetailsResponseMutableLiveData = new MutableLiveData<>();

    public DetailsRestaurantViewModel() {
        mRestaurantDetailRepository =  Injection.getRestaurantDetail(Injection.getService(), Injection.resource);
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
