package com.oconte.david.go4lunch.listView;

import androidx.annotation.Nullable;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.oconte.david.go4lunch.api.GooglePlaceService;
import com.oconte.david.go4lunch.models.ApiNearByResponse;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {

    private final GooglePlaceService service;
    private final CountingIdlingResource resource;

    /*private MutableLiveData<List<Result>> apiNearByResponseMutableLiveData;
    public MutableLiveData<List<Result>> getResults() {
        return apiNearByResponseMutableLiveData;
    }*/

    /**
     * It's the Call to API GooglePlace.
     */

    // Creating a callback
    public interface Callbacks {
        void onResponse(@Nullable ApiNearByResponse apiNearByResponse);
        void onFailure();
    }

    public RestaurantRepository(GooglePlaceService service, CountingIdlingResource resource) {
        this.service = service;
        this.resource = resource;
    }

    // Public methode to start fetching
    public void getRestaurantNearBy(RestaurantRepository.Callbacks callbacks, String location) {
        resource.increment();
        // weak reference to callback (avoid memory leaks)

        // The call on NYT API
        Call<ApiNearByResponse> call = service.getRestaurantNearBy(location);

        // Start the Call
        call.enqueue(new Callback<ApiNearByResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApiNearByResponse> call, @NotNull Response<ApiNearByResponse> apiNearByResponseResponse) {
                // Call the proper callback used in controller mainfragment
                callbacks.onResponse(apiNearByResponseResponse.body());
                resource.decrement();
            }

            @Override
            public void onFailure(@NotNull Call<ApiNearByResponse> call, @NotNull Throwable t) {
                // Call the proper callback used in controller mainfragment
                callbacks.onFailure();
                resource.decrement();
            }
        });

    }
}
