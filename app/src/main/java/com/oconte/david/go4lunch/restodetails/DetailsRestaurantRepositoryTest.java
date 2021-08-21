package com.oconte.david.go4lunch.restodetails;

import com.oconte.david.go4lunch.models.DetailsRestaurant;
import com.oconte.david.go4lunch.models.Result;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public final class DetailsRestaurantRepositoryTest {

    private static volatile DetailsRestaurantRepositoryTest instance;
    private DetailsRestaurant detailsRestaurant;

    public DetailsRestaurantRepositoryTest() {
    }

    // contient l'information du restaurant click
    private final MutableLiveData<Result> clickRestaurant = new MutableLiveData<Result>();
    public void clickRestaurant(Result result) {
        //mettre a jour l'info
        clickRestaurant.postValue(result);
    }

    public LiveData<Result> getClickRestaurant() {
        //recuperer l'information pour l'utiliser
        return clickRestaurant;
    }
}
