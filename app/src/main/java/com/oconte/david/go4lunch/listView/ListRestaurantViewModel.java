package com.oconte.david.go4lunch.listView;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.oconte.david.go4lunch.Injection;
import com.oconte.david.go4lunch.api.GooglePlaceService;
import com.oconte.david.go4lunch.models.ApiNearByResponse;
import com.oconte.david.go4lunch.models.Result;

import java.util.List;

import static androidx.test.espresso.intent.Intents.init;

public class ListRestaurantViewModel extends ViewModel {

    private final RestaurantRepository mRestaurantRepository;
    private final MutableLiveData<List<Result>> apiNearByResponseMutableLiveData = new MutableLiveData<>();
    private final LiveData <List<Result>> results = apiNearByResponseMutableLiveData;

    public ListRestaurantViewModel() {
        mRestaurantRepository =  Injection.getRestaurantNearBy(Injection.getService(), Injection.resource);
    }

    public LiveData<List<Result>> getRestaurantLiveData() {
        return results;
    }

    public void getRestaurants() {

        //Classe Anonyme
        mRestaurantRepository.getRestaurantNearBy(new RestaurantRepository.Callbacks() {
            @Override
            public void onResponse(@Nullable ApiNearByResponse apiNearByResponse) {

                if (apiNearByResponse != null) {
                    apiNearByResponseMutableLiveData.postValue(apiNearByResponse.results);
                }

            }

            @Override
            public void onFailure() {

            }
        }, "location");
    }

}
