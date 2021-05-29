package com.oconte.david.go4lunch.restoDetails;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.oconte.david.go4lunch.api.GooglePlaceService;
import com.oconte.david.go4lunch.models.ApiRestaurantDetails;
import com.oconte.david.go4lunch.models.Result;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetailRepository {

    private final GooglePlaceService service;
    private final CountingIdlingResource resource;

    private MutableLiveData<List<Result>> apiDetailsResponseMutableLiveData;

    public MutableLiveData<List<Result>> getResults() {
        return apiDetailsResponseMutableLiveData;
    }

    /**
     * It's the Call to API GooglePlace.
     */

    // Creating a callback
    public interface Callbacks {
        void onResponse(@Nullable ApiRestaurantDetails apiRestaurantDetails);
        void onFailure();
    }

    public RestaurantDetailRepository(GooglePlaceService service, CountingIdlingResource resource) {
        this.service = service;
        this.resource = resource;
    }

    // Public methode to start fetching
    public void getRestaurantDetails(RestaurantDetailRepository.Callbacks callbacks, String location) {

        resource.increment();
        // weak reference to callback (avoid memory leaks)
        //final WeakReference<RestaurantRepository.Callbacks> callbacksWeakReference = new WeakReference<RestaurantRepository.Callbacks>(callbacks);

        // The call on NYT API
        Call<ApiRestaurantDetails> call = service.getRestaurantDetail(location);

        // Start the Call
        call.enqueue(new Callback<ApiRestaurantDetails>() {
            @Override
            public void onResponse(@NotNull Call<ApiRestaurantDetails> call, @NotNull Response<ApiRestaurantDetails> apiRestaurantDetailsResponse) {

                // Call the proper callback used in controller mainfragment
                callbacks.onResponse(apiRestaurantDetailsResponse.body());
                resource.decrement();
            }

            @Override
            public void onFailure(@NotNull Call<ApiRestaurantDetails> call, @NotNull Throwable t) {

                // Call the proper callback used in controller mainfragment
                callbacks.onFailure();
                resource.decrement();
            }
        });

    }
}
