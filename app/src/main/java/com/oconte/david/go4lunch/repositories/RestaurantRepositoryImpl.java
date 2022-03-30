package com.oconte.david.go4lunch.repositories;

import androidx.test.espresso.idling.CountingIdlingResource;

import com.oconte.david.go4lunch.api.GooglePlaceService;
import com.oconte.david.go4lunch.models.ApiNearByResponse;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepositoryImpl implements RestaurantRepository{

    private final GooglePlaceService service;
    private final CountingIdlingResource resource;

    public RestaurantRepositoryImpl(GooglePlaceService service, CountingIdlingResource resource) {
        this.service = service;
        this.resource = resource;
    }

    @Override
    public void getRestaurantNearBy(GetRestaurantsCallback getRestaurantsCallback, String location) {
        resource.increment();
        // weak reference to callback (avoid memory leaks)

        // The call on NYT API
        Call<ApiNearByResponse> call = service.getRestaurantNearBy(location);

        // Start the Call
        call.enqueue(new Callback<ApiNearByResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApiNearByResponse> call, @NotNull Response<ApiNearByResponse> apiNearByResponseResponse) {
                // Call the proper callback used in controller mainfragment
                getRestaurantsCallback.onResponse(apiNearByResponseResponse.body());
                resource.decrement();
            }

            @Override
            public void onFailure(@NotNull Call<ApiNearByResponse> call, @NotNull Throwable t) {
                // Call the proper callback used in controller mainfragment
                getRestaurantsCallback.onFailure();
                resource.decrement();
            }
        });

    }
}
