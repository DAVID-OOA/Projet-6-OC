package com.oconte.david.go4lunch.repositories;

import androidx.annotation.Nullable;
import com.oconte.david.go4lunch.models.ApiNearByResponse;

public interface RestaurantRepository {

    interface GetRestaurantsCallback {
        void onResponse(@Nullable ApiNearByResponse apiNearByResponse);
        void onFailure();
    }
    void getRestaurantNearBy(GetRestaurantsCallback getRestaurantsCallback, String location);
}
