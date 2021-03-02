package com.oconte.david.go4lunch;

import androidx.annotation.Nullable;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.oconte.david.go4lunch.api.GooglePlaceService;
import com.oconte.david.go4lunch.models.ApiRestaurantDetails;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GooglePlaceCallRestaurantDetails {

    private final GooglePlaceService service;
    private final CountingIdlingResource resource;

    /**
     * It's the Call to API New York Time for see the Top Stories categories.
     */

    // Creating a callback
    public interface Callbacks {
        void onResponse(@Nullable ApiRestaurantDetails apiRestaurantDetails);
        void onFailure();
    }

    public GooglePlaceCallRestaurantDetails(GooglePlaceService service, CountingIdlingResource resource) {
        this.service = service;
        this.resource = resource;
    }

    // Public methode to start fetching
    public void getRestaurantDetail(GooglePlaceCallRestaurantDetails.Callbacks callbacks, String placeId) {

        resource.increment();
        // weak reference to callback (avoid memory leaks)
        final WeakReference<GooglePlaceCallRestaurantDetails.Callbacks> callbacksWeakReference = new WeakReference<GooglePlaceCallRestaurantDetails.Callbacks>(callbacks);

        // The call on NYT API
        Call<ApiRestaurantDetails> call = service.getRestaurantDetail(placeId);

        // Start the Call
        call.enqueue(new Callback<ApiRestaurantDetails>() {
            @Override
            public void onResponse(Call<ApiRestaurantDetails> call, Response<ApiRestaurantDetails> apiDetailResponse) {

                // Call the proper callback used in controller mainfragment
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(apiDetailResponse.body());
                resource.decrement();
            }

            @Override
            public void onFailure(Call<ApiRestaurantDetails> call, Throwable t) {

                // Call the proper callback used in controller mainfragment
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
                resource.decrement();
            }
        });

    }
}
