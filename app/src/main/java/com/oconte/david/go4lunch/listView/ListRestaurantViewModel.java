package com.oconte.david.go4lunch.listView;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.oconte.david.go4lunch.Injection;
import com.oconte.david.go4lunch.models.ApiNearByResponse;
import com.oconte.david.go4lunch.models.Result;

import java.util.List;

public class ListRestaurantViewModel extends ViewModel {

    private final RestaurantRepository mRestaurantRepository;
    private final MutableLiveData<List<Result>> apiNearByResponseMutableLiveData = new MutableLiveData<>();

    public ListRestaurantViewModel() {
        mRestaurantRepository =  Injection.getRestaurantNearBy(Injection.getService(), Injection.resource);
    }

    public LiveData<List<Result>> getRestaurantLiveData() {
            return apiNearByResponseMutableLiveData;
    }


    // contient l'information du restaurant selectionné
    private final MutableLiveData<Result> selectedRestaurant = new MutableLiveData<Result>();
    public void selectRestaurant(Result result) {
        //mettre a jour l'info
        selectedRestaurant.postValue(result);
    }
    public LiveData<Result> getSelectedRestaurant() {
        //recuperer l'information
        return selectedRestaurant;
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
